package com.shopease.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.shopease.dao.OrderDAO;
import com.shopease.dao.OrderItemDAO;
import com.shopease.dao.PaymentDAO;
import com.shopease.daoimpl.OrderDAOImpl;
import com.shopease.daoimpl.OrderItemDAOImpl;
import com.shopease.daoimpl.PaymentDAOImpl;
import com.shopease.model.Order;
import com.shopease.model.OrderItem;
import com.shopease.model.Payment;
import com.shopease.utility.DBConnection;

/**
 * Order service.
 * placeOrder() uses ONE JDBC connection and one transaction so order rows,
 * order items, stock changes, payment record and cart clearing succeed/fail
 * together. All involved MySQL tables must use InnoDB.
 */
public class OrderService {

    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final OrderItemDAO orderItemDAO = new OrderItemDAOImpl();
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();

    /**
     * Places an order atomically for COD, UPI or CARD.
     * Online payments are simulated in this interview project. A new order
     * starts with PENDING payment status; the payment workflow must update it
     * only after the simulated payment step is completed.
     */
    /** Backward-compatible boolean method for existing callers. */
    public boolean placeOrder(int userId, int addressId, String paymentMethod) {
        return placeOrderAndGetId(userId, addressId, paymentMethod) > 0;
    }

    /** Backward-compatible boolean method for existing callers. */
    public boolean placeSingleProductOrder(int userId, int addressId,
            String paymentMethod, int productId, int quantity) {
        return placeSingleProductOrderAndGetId(
            userId, addressId, paymentMethod, productId, quantity
        ) > 0;
    }

    public int placeOrderAndGetId(int userId, int addressId, String paymentMethod) {
        if (userId <= 0 || addressId <= 0 || !isValidPaymentMethod(paymentMethod)) {
            System.err.println("Order rejected: invalid user, address or payment method.");
            return 0;
        }

        paymentMethod = paymentMethod.trim().toUpperCase();

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                // 1. Confirm the address belongs to the logged-in customer.
                String addressSql =
                    "SELECT address_id FROM addresses WHERE address_id = ? AND user_id = ? FOR UPDATE";
                try (PreparedStatement ps = con.prepareStatement(addressSql)) {
                    ps.setInt(1, addressId);
                    ps.setInt(2, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            System.err.println("Order rejected: delivery address does not belong to user.");
                            return 0;
                        }
                    }
                }

                // 2. Lock the customer's cart.
                int cartId;
                String cartSql = "SELECT cart_id FROM cart WHERE user_id = ? FOR UPDATE";
                try (PreparedStatement ps = con.prepareStatement(cartSql)) {
                    ps.setInt(1, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            System.err.println("Order rejected: cart not found.");
                            return 0;
                        }
                        cartId = rs.getInt("cart_id");
                    }
                }

                // 3. Read and lock all cart rows.
                java.util.ArrayList<CartLine> lines = new java.util.ArrayList<>();
                String cartItemsSql =
                    "SELECT product_id, variant_id, quantity FROM cart_items WHERE cart_id = ? FOR UPDATE";
                try (PreparedStatement ps = con.prepareStatement(cartItemsSql)) {
                    ps.setInt(1, cartId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int variantValue = rs.getInt("variant_id");
                            Integer variantId = rs.wasNull() ? null : variantValue;
                            int quantity = rs.getInt("quantity");
                            if (quantity <= 0) {
                                con.rollback();
                                System.err.println("Order rejected: invalid cart quantity.");
                                return 0;
                            }
                            lines.add(new CartLine(rs.getInt("product_id"), variantId, quantity));
                        }
                    }
                }

                if (lines.isEmpty()) {
                    con.rollback();
                    System.err.println("Order rejected: cart is empty.");
                    return 0;
                }

                // 4. Lock products/variants, validate stock, and calculate total.
                BigDecimal total = BigDecimal.ZERO;
                for (CartLine line : lines) {
                    String productSql =
                        "SELECT price, discount, stock FROM products WHERE product_id = ? FOR UPDATE";
                    BigDecimal basePrice;
                    BigDecimal discountPercent;
                    int productStock;

                    try (PreparedStatement ps = con.prepareStatement(productSql)) {
                        ps.setInt(1, line.productId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) {
                                con.rollback();
                                System.err.println("Order rejected: product " + line.productId + " no longer exists.");
                                return 0;
                            }
                            basePrice = rs.getBigDecimal("price");
                            discountPercent = rs.getBigDecimal("discount");
                            productStock = rs.getInt("stock");
                        }
                    }

                    if (basePrice == null || basePrice.signum() < 0) {
                        con.rollback();
                        System.err.println("Order rejected: invalid product price.");
                        return 0;
                    }
                    if (discountPercent == null) discountPercent = BigDecimal.ZERO;
                    if (discountPercent.signum() < 0 || discountPercent.compareTo(new BigDecimal("100")) > 0) {
                        con.rollback();
                        System.err.println("Order rejected: invalid product discount.");
                        return 0;
                    }

                    BigDecimal unitPrice = basePrice.subtract(
                        basePrice.multiply(discountPercent)
                            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                    );

                    if (line.variantId != null) {
                        String variantSql =
                            "SELECT product_id, additional_price, stock FROM product_variants WHERE variant_id = ? FOR UPDATE";
                        try (PreparedStatement ps = con.prepareStatement(variantSql)) {
                            ps.setInt(1, line.variantId);
                            try (ResultSet rs = ps.executeQuery()) {
                                if (!rs.next()
                                        || rs.getInt("product_id") != line.productId
                                        || rs.getInt("stock") < line.quantity) {
                                    con.rollback();
                                    System.err.println("Order rejected: selected product variant is unavailable or out of stock.");
                                    return 0;
                                }
                                BigDecimal extra = rs.getBigDecimal("additional_price");
                                if (extra != null) unitPrice = unitPrice.add(extra);
                            }
                        }
                    } else if (productStock < line.quantity) {
                        con.rollback();
                        System.err.println("Order rejected: insufficient stock for product " + line.productId + ".");
                        return 0;
                    }

                    line.unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
                    total = total.add(line.unitPrice.multiply(BigDecimal.valueOf(line.quantity)));
                }

                // 5. Insert order and retrieve its ID from this exact connection.
                int orderId;
                String orderSql =
                    "INSERT INTO orders (user_id, address_id, total_amount, order_status, payment_status, order_date) "
                    + "VALUES (?, ?, ?, 'PLACED', 'PENDING', CURRENT_TIMESTAMP)";
                try (PreparedStatement ps = con.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, addressId);
                    ps.setBigDecimal(3, total.setScale(2, RoundingMode.HALF_UP));
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Order header could not be inserted.");
                    }
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("MySQL did not return the new order ID.");
                        orderId = keys.getInt(1);
                    }
                }

                // 6. Insert order lines and deduct stock using guarded updates.
                String orderItemSql =
                    "INSERT INTO order_items (order_id, product_id, variant_id, quantity, price) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement itemPs = con.prepareStatement(orderItemSql)) {
                    for (CartLine line : lines) {
                        itemPs.setInt(1, orderId);
                        itemPs.setInt(2, line.productId);
                        if (line.variantId == null) itemPs.setNull(3, java.sql.Types.INTEGER);
                        else itemPs.setInt(3, line.variantId);
                        itemPs.setInt(4, line.quantity);
                        itemPs.setBigDecimal(5, line.unitPrice);
                        if (itemPs.executeUpdate() != 1) {
                            throw new SQLException("An order item could not be saved.");
                        }

                        String stockSql = line.variantId == null
                            ? "UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?"
                            : "UPDATE product_variants SET stock = stock - ? WHERE variant_id = ? AND stock >= ?";
                        try (PreparedStatement stockPs = con.prepareStatement(stockSql)) {
                            stockPs.setInt(1, line.quantity);
                            stockPs.setInt(2, line.variantId == null ? line.productId : line.variantId);
                            stockPs.setInt(3, line.quantity);
                            if (stockPs.executeUpdate() != 1) {
                                throw new SQLException("Stock changed while placing the order. Please retry.");
                            }
                        }
                    }
                }

                // 7. Create the pending COD payment record.
                String paymentSql =
                    "INSERT INTO payments (order_id, payment_method, payment_status, transaction_reference, payment_date) "
                    + "VALUES (?, ?, 'PENDING', NULL, CURRENT_TIMESTAMP)";
                try (PreparedStatement ps = con.prepareStatement(paymentSql)) {
                    ps.setInt(1, orderId);
                    ps.setString(2, paymentMethod);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Payment record could not be created.");
                    }
                }

                // 8. Empty cart only after every other step succeeds.
                try (PreparedStatement ps =
                         con.prepareStatement("DELETE FROM cart_items WHERE cart_id = ?")) {
                    ps.setInt(1, cartId);
                    ps.executeUpdate();
                }

                con.commit();
                System.out.println("ShopEase order placed successfully. Order ID: " + orderId);
                return orderId;

            } catch (SQLException | RuntimeException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                System.err.println("ShopEase order placement failed; transaction rolled back.");
                e.printStackTrace();
                return 0;
            } finally {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException resetError) {
                    resetError.printStackTrace();
                }
            }

        } catch (SQLException e) {
            System.err.println("Could not connect to database while placing order.");
            e.printStackTrace();
            return 0;
        }
    }
    
    public int placeSingleProductOrderAndGetId(int userId, int addressId,
            String paymentMethod, int productId, int quantity) {

        if (userId <= 0 || addressId <= 0 || productId <= 0 || quantity <= 0
                || !isValidPaymentMethod(paymentMethod)) {
            return 0;
        }

        paymentMethod = paymentMethod.trim().toUpperCase();

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                // Verify that the address belongs to this user.
                String addressSql =
                    "SELECT address_id FROM addresses WHERE address_id = ? AND user_id = ? FOR UPDATE";

                try (PreparedStatement ps = con.prepareStatement(addressSql)) {
                    ps.setInt(1, addressId);
                    ps.setInt(2, userId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return 0;
                        }
                    }
                }

                // Lock the product and check its stock.
                BigDecimal price;
                BigDecimal discount;
                String productSql =
                    "SELECT price, discount, stock FROM products WHERE product_id = ? FOR UPDATE";

                try (PreparedStatement ps = con.prepareStatement(productSql)) {
                    ps.setInt(1, productId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() || rs.getInt("stock") < quantity) {
                            con.rollback();
                            return 0;
                        }

                        price = rs.getBigDecimal("price");
                        discount = rs.getBigDecimal("discount");
                    }
                }

                if (price == null || price.signum() < 0) {
                    con.rollback();
                    return 0;
                }

                if (discount == null) {
                    discount = BigDecimal.ZERO;
                }

                if (discount.signum() < 0
                        || discount.compareTo(new BigDecimal("100")) > 0) {
                    con.rollback();
                    return 0;
                }

                BigDecimal discountAmount = price.multiply(discount)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

                BigDecimal unitPrice = price.subtract(discountAmount)
                    .setScale(2, RoundingMode.HALF_UP);

                BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(quantity));

                // Create the order.
                int orderId;
                String orderSql =
                    "INSERT INTO orders (user_id, address_id, total_amount, order_status, payment_status, order_date) "
                    + "VALUES (?, ?, ?, 'PLACED', 'PENDING', CURRENT_TIMESTAMP)";

                try (PreparedStatement ps =
                        con.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, userId);
                    ps.setInt(2, addressId);
                    ps.setBigDecimal(3, total);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Order could not be created.");
                    }

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Order ID was not generated.");
                        }
                        orderId = keys.getInt(1);
                    }
                }

                // Save the order item. Buy Now uses no variant.
                String itemSql =
                    "INSERT INTO order_items (order_id, product_id, variant_id, quantity, price) "
                    + "VALUES (?, ?, NULL, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(itemSql)) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, productId);
                    ps.setInt(3, quantity);
                    ps.setBigDecimal(4, unitPrice);
                    ps.executeUpdate();
                }

                // Deduct stock safely.
                String stockSql =
                    "UPDATE products SET stock = stock - ? "
                    + "WHERE product_id = ? AND stock >= ?";

                try (PreparedStatement ps = con.prepareStatement(stockSql)) {
                    ps.setInt(1, quantity);
                    ps.setInt(2, productId);
                    ps.setInt(3, quantity);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Insufficient stock.");
                    }
                }

                // Create COD payment record.
                String paymentSql =
                    "INSERT INTO payments (order_id, payment_method, payment_status, transaction_reference, payment_date) "
                    + "VALUES (?, ?, 'PENDING', NULL, CURRENT_TIMESTAMP)";

                try (PreparedStatement ps = con.prepareStatement(paymentSql)) {
                    ps.setInt(1, orderId);
                    ps.setString(2, paymentMethod);
                    ps.executeUpdate();
                }

                // Do not clear the customer's cart for Buy Now.
                con.commit();

                System.out.println("Buy Now order placed. Order ID: " + orderId);
                return orderId;

            } catch (SQLException | RuntimeException e) {
                con.rollback();
                e.printStackTrace();
                return 0;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** Accept only the payment methods offered by the checkout page. */
    private boolean isValidPaymentMethod(String paymentMethod) {
        if (paymentMethod == null) {
            return false;
        }

        String method = paymentMethod.trim().toUpperCase();
        return "COD".equals(method) || "UPI".equals(method) || "CARD".equals(method);
    }

    private static class CartLine {
        private final int productId;
        private final Integer variantId;
        private final int quantity;
        private BigDecimal unitPrice;

        private CartLine(int productId, Integer variantId, int quantity) {
            this.productId = productId;
            this.variantId = variantId;
            this.quantity = quantity;
        }
    }

    // Existing order-reading and management methods retained for the other pages.
    public Order getOrderById(int orderId) {
        return orderId > 0 ? orderDAO.getOrderById(orderId) : null;
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public List<Order> getOrdersByUserId(int userId) {
        return userId > 0 ? orderDAO.getOrdersByUserId(userId) : null;
    }

    public List<OrderItem> getOrderItems(int orderId) {
        return orderId > 0 ? orderItemDAO.getOrderItemsByOrderId(orderId) : null;
    }

    public boolean updateOrderStatus(int orderId, String status) {
        return orderId > 0 && status != null && !status.trim().isEmpty()
            && orderDAO.updateOrderStatus(orderId, status.trim());
    }

    public boolean updatePaymentStatus(int orderId, String status) {
        return orderId > 0 && status != null && !status.trim().isEmpty()
            && orderDAO.updatePaymentStatus(orderId, status.trim());
    }

    public int getOrderCount() {
        return orderDAO.getOrderCount();
    }

    public BigDecimal getOrderTotal(int orderId) {
        return orderId > 0 ? orderItemDAO.getOrderTotal(orderId) : BigDecimal.ZERO;
    }

    public Payment getPaymentByOrderId(int orderId) {
        return orderId > 0 ? paymentDAO.getPaymentByOrderId(orderId) : null;
    }
}
