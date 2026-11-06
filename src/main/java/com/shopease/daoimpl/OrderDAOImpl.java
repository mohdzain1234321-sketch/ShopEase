package com.shopease.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopease.dao.OrderDAO;
import com.shopease.model.Order;
import com.shopease.utility.DBConnection;

public class OrderDAOImpl implements OrderDAO {

    // 1. Add Order
    @Override
    public boolean addOrder(Order order) {

        String sql = "INSERT INTO orders "
                + "(user_id, address_id, total_amount, order_status, payment_status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getAddressId());
            ps.setBigDecimal(3, order.getTotalAmount());
            ps.setString(4, order.getOrderStatus());
            ps.setString(5, order.getPaymentStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 2. Get Order By ID
    @Override
    public Order getOrderById(int orderId) {

        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return extractOrder(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 3. Get All Orders
    @Override
    public List<Order> getAllOrders() {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders ORDER BY order_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orders.add(extractOrder(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // 4. Get Orders By User ID
    @Override
    public List<Order> getOrdersByUserId(int userId) {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders "
                + "WHERE user_id = ? ORDER BY order_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    orders.add(extractOrder(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // 5. Get Orders By Status
    @Override
    public List<Order> getOrdersByStatus(String orderStatus) {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders "
                + "WHERE order_status = ? ORDER BY order_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, orderStatus);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    orders.add(extractOrder(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // 6. Update Order
    @Override
    public boolean updateOrder(Order order) {

        String sql = "UPDATE orders SET "
                + "user_id = ?, address_id = ?, total_amount = ?, "
                + "order_status = ?, payment_status = ? "
                + "WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getAddressId());
            ps.setBigDecimal(3, order.getTotalAmount());
            ps.setString(4, order.getOrderStatus());
            ps.setString(5, order.getPaymentStatus());
            ps.setInt(6, order.getOrderId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 7. Update Order Status
    @Override
    public boolean updateOrderStatus(int orderId, String orderStatus) {

        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, orderStatus);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 8. Update Payment Status
    @Override
    public boolean updatePaymentStatus(int orderId, String paymentStatus) {

        String sql = "UPDATE orders SET payment_status = ? WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 9. Delete Order
    @Override
    public boolean deleteOrder(int orderId) {

        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 10. Get Order Count By User ID
    @Override
    public int getOrderCountByUserId(int userId) {

        String sql = "SELECT COUNT(*) AS total_orders "
                + "FROM orders WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("total_orders");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Extract Order From ResultSet
    private Order extractOrder(ResultSet rs) throws SQLException {

        Order order = new Order();

        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setAddressId(rs.getInt("address_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setOrderStatus(rs.getString("order_status"));
        order.setPaymentStatus(rs.getString("payment_status"));
        order.setOrderDate(rs.getTimestamp("order_date"));

        return order;
    }
    
    @Override
    public int getOrderCount() {

        String sql = "SELECT COUNT(*) FROM orders";

        try (java.sql.Connection connection = DBConnection.getConnection();
             java.sql.PreparedStatement ps = connection.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}