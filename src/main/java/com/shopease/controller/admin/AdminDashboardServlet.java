package com.shopease.controller.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shopease.utility.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection con = DBConnection.getConnection()) {
            request.setAttribute("totalProducts", count(con, "SELECT COUNT(*) FROM products"));
            request.setAttribute("totalCustomers", count(con, "SELECT COUNT(*) FROM users"));
            request.setAttribute("totalOrders", count(con, "SELECT COUNT(*) FROM orders"));

            // Revenue is the sum of order totals; refine to SUCCESS payments
            // once the exact payment/order business rule is confirmed.
         // Count revenue only from successful payments
            request.setAttribute("totalRevenue",
                    decimal(con,
                        "SELECT COALESCE(SUM(o.total_amount), 0) "
                      + "FROM orders o "
                      + "INNER JOIN payments p ON o.order_id = p.order_id "
                      + "WHERE UPPER(p.payment_status) = 'SUCCESS'"));

        } catch (SQLException e) {
            throw new ServletException("Unable to load admin dashboard statistics.", e);
        }

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    private int count(Connection con, String sql) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private BigDecimal decimal(Connection con, String sql) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
    }
}
