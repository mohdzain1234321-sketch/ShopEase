package com.shopease.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopease.dao.PaymentDAO;
import com.shopease.model.Payment;
import com.shopease.utility.DBConnection;

public class PaymentDAOImpl implements PaymentDAO {

    // 1. Add Payment
    @Override
    public boolean addPayment(Payment payment) {

        String sql = "INSERT INTO payments "
                + "(order_id, payment_method, payment_status, transaction_reference) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setString(3, payment.getPaymentStatus());
            ps.setString(4, payment.getTransactionReference());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 2. Get Payment By ID
    @Override
    public Payment getPaymentById(int paymentId) {

        String sql = "SELECT * FROM payments WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, paymentId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return extractPayment(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 3. Get Payment By Order ID
    @Override
    public Payment getPaymentByOrderId(int orderId) {

        String sql = "SELECT * FROM payments WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return extractPayment(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 4. Get All Payments
    @Override
    public List<Payment> getAllPayments() {

        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT * FROM payments ORDER BY payment_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                payments.add(extractPayment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    // 5. Get Payments By Status
    @Override
    public List<Payment> getPaymentsByStatus(String paymentStatus) {

        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT * FROM payments WHERE payment_status = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    payments.add(extractPayment(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    // 6. Update Payment
    @Override
    public boolean updatePayment(Payment payment) {

        String sql = "UPDATE payments SET "
                + "order_id = ?, payment_method = ?, payment_status = ?, "
                + "transaction_reference = ? "
                + "WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setString(3, payment.getPaymentStatus());
            ps.setString(4, payment.getTransactionReference());
            ps.setInt(5, payment.getPaymentId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 7. Update Payment Status
    @Override
    public boolean updatePaymentStatus(int paymentId, String paymentStatus) {

        String sql = "UPDATE payments SET payment_status = ? "
                + "WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);
            ps.setInt(2, paymentId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 8. Delete Payment
    @Override
    public boolean deletePayment(int paymentId) {

        String sql = "DELETE FROM payments WHERE payment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, paymentId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 9. Check Payment Exists
    @Override
    public boolean paymentExists(int orderId) {

        String sql = "SELECT 1 FROM payments WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Extract Payment From ResultSet
    private Payment extractPayment(ResultSet rs) throws SQLException {

        Payment payment = new Payment();

        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setOrderId(rs.getInt("order_id"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setPaymentStatus(rs.getString("payment_status"));
        payment.setTransactionReference(
                rs.getString("transaction_reference"));
        payment.setPaymentDate(rs.getTimestamp("payment_date"));

        return payment;
    }
}