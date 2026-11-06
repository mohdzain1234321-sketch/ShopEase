package com.shopease.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shopease.utility.DBConnection;

/**
 * Cancels a customer's PLACED order in one database transaction.
 * Restores product/variant stock and updates order/payment statuses together.
 */
public class OrderCancellationService {

    public boolean cancelOrder(int orderId, int userId) {
        if (orderId <= 0 || userId <= 0) return false;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                String orderStatus = null;
                String lockOrder =
                    "SELECT order_status FROM orders WHERE order_id = ? AND user_id = ? FOR UPDATE";

                try (PreparedStatement ps = con.prepareStatement(lockOrder)) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }
                        orderStatus = rs.getString("order_status");
                    }
                }

                if (orderStatus == null || !"PLACED".equalsIgnoreCase(orderStatus.trim())) {
                    con.rollback();
                    return false;
                }

                String itemsSql =
                    "SELECT product_id, variant_id, quantity FROM order_items WHERE order_id = ? FOR UPDATE";

                try (PreparedStatement ps = con.prepareStatement(itemsSql)) {
                    ps.setInt(1, orderId);
                    try (ResultSet rs = ps.executeQuery()) {
                        boolean hasItems = false;
                        while (rs.next()) {
                            hasItems = true;
                            int productId = rs.getInt("product_id");
                            int variantValue = rs.getInt("variant_id");
                            Integer variantId = rs.wasNull() ? null : variantValue;
                            int quantity = rs.getInt("quantity");

                            if (quantity <= 0) {
                                throw new SQLException("Invalid quantity in order item.");
                            }

                            String stockSql = variantId == null
                                ? "UPDATE products SET stock = stock + ? WHERE product_id = ?"
                                : "UPDATE product_variants SET stock = stock + ? WHERE variant_id = ? AND product_id = ?";

                            try (PreparedStatement stockPs = con.prepareStatement(stockSql)) {
                                stockPs.setInt(1, quantity);
                                stockPs.setInt(2, variantId == null ? productId : variantId);
                                if (variantId != null) stockPs.setInt(3, productId);
                                if (stockPs.executeUpdate() != 1) {
                                    throw new SQLException("Could not restore stock for an order item.");
                                }
                            }
                        }
                        if (!hasItems) {
                            throw new SQLException("Order has no items; cancellation stopped.");
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE orders SET order_status = 'CANCELLED', payment_status = 'CANCELLED' "
                      + "WHERE order_id = ? AND user_id = ? AND order_status = 'PLACED'")) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, userId);
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Order status changed before cancellation completed.");
                    }
                }

                // COD payment is marked cancelled; this does not issue a refund.
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE payments SET payment_status = 'CANCELLED' WHERE order_id = ?")) {
                    ps.setInt(1, orderId);
                    ps.executeUpdate();
                }

                con.commit();
                return true;

            } catch (SQLException | RuntimeException e) {
                try { con.rollback(); } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                System.err.println("Order cancellation failed; transaction rolled back.");
                e.printStackTrace();
                return false;
            } finally {
                try { con.setAutoCommit(true); } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("Could not connect to database to cancel order.");
            e.printStackTrace();
            return false;
        }
    }
}
