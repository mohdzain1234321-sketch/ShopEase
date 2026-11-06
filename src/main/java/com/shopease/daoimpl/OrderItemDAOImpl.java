package com.shopease.daoimpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopease.dao.OrderItemDAO;
import com.shopease.model.OrderItem;
import com.shopease.utility.DBConnection;

public class OrderItemDAOImpl implements OrderItemDAO {

    // 1. Add Order Item
    @Override
    public boolean addOrderItem(OrderItem orderItem) {

        String sql = "INSERT INTO order_items "
                + "(order_id, product_id, variant_id, quantity, price) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getProductId());

            if (orderItem.getVariantId() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, orderItem.getVariantId());
            }

            ps.setInt(4, orderItem.getQuantity());
            ps.setBigDecimal(5, orderItem.getPrice());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 2. Get Order Item By ID
    @Override
    public OrderItem getOrderItemById(int orderItemId) {

        String sql = "SELECT * FROM order_items WHERE order_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItemId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return extractOrderItem(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 3. Get All Order Items
    @Override
    public List<OrderItem> getAllOrderItems() {

        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT * FROM order_items";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(extractOrderItem(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    // 4. Get Order Items By Order ID
    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {

        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT * FROM order_items WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    items.add(extractOrderItem(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    // 5. Get Order Items By Product ID
    @Override
    public List<OrderItem> getOrderItemsByProductId(int productId) {

        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT * FROM order_items WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    items.add(extractOrderItem(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    // 6. Update Order Item
    @Override
    public boolean updateOrderItem(OrderItem orderItem) {

        String sql = "UPDATE order_items SET "
                + "order_id = ?, product_id = ?, variant_id = ?, "
                + "quantity = ?, price = ? "
                + "WHERE order_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getProductId());

            if (orderItem.getVariantId() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, orderItem.getVariantId());
            }

            ps.setInt(4, orderItem.getQuantity());
            ps.setBigDecimal(5, orderItem.getPrice());
            ps.setInt(6, orderItem.getOrderItemId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 7. Delete Order Item By ID
    @Override
    public boolean deleteOrderItem(int orderItemId) {

        String sql = "DELETE FROM order_items WHERE order_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItemId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 8. Delete Order Items By Order ID
    @Override
    public boolean deleteOrderItemsByOrderId(int orderId) {

        String sql = "DELETE FROM order_items WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 9. Calculate Order Total
    @Override
    public BigDecimal getOrderTotal(int orderId) {

        String sql = "SELECT COALESCE(SUM(quantity * price), 0) "
                + "AS order_total FROM order_items WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getBigDecimal("order_total");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    // 10. Get Total Quantity By Order ID
    @Override
    public int getTotalQuantityByOrderId(int orderId) {

        String sql = "SELECT COALESCE(SUM(quantity), 0) "
                + "AS total_quantity FROM order_items WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("total_quantity");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Extract Order Item From ResultSet
    private OrderItem extractOrderItem(ResultSet rs) throws SQLException {

        OrderItem item = new OrderItem();

        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setProductId(rs.getInt("product_id"));

        int variantId = rs.getInt("variant_id");

        if (rs.wasNull()) {
            item.setVariantId(null);
        } else {
            item.setVariantId(variantId);
        }

        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getBigDecimal("price"));

        return item;
    }
}