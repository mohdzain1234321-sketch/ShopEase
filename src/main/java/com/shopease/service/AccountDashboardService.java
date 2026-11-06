package com.shopease.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shopease.utility.DBConnection;

public class AccountDashboardService {

    public List<Map<String, Object>> getRecentReviewsByUser(int userId) {
        List<Map<String, Object>> reviews = new ArrayList<>();
        String sql = "SELECT r.review_id, r.product_id, p.product_name, "
                + "r.rating, r.review_title, r.review_comment, r.created_at "
                + "FROM product_reviews r JOIN products p ON p.product_id=r.product_id "
                + "WHERE r.user_id=? ORDER BY r.updated_at DESC LIMIT 3";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("reviewId", rs.getInt("review_id"));
                    row.put("productId", rs.getInt("product_id"));
                    row.put("productName", rs.getString("product_name"));
                    row.put("rating", rs.getInt("rating"));
                    row.put("title", rs.getString("review_title"));
                    row.put("comment", rs.getString("review_comment"));
                    row.put("createdAt", rs.getTimestamp("created_at"));
                    reviews.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Dashboard review preview failed: " + e.getMessage());
        }
        return reviews;
    }

    public List<Map<String, Object>> getAllReviewsByUser(int userId) {
        List<Map<String, Object>> reviews = new ArrayList<>();
        String sql = "SELECT r.review_id, r.product_id, p.product_name, "
                + "r.rating, r.review_title, r.review_comment, r.created_at "
                + "FROM product_reviews r JOIN products p ON p.product_id=r.product_id "
                + "WHERE r.user_id=? ORDER BY r.updated_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("reviewId", rs.getInt("review_id"));
                    row.put("productId", rs.getInt("product_id"));
                    row.put("productName", rs.getString("product_name"));
                    row.put("rating", rs.getInt("rating"));
                    row.put("title", rs.getString("review_title"));
                    row.put("comment", rs.getString("review_comment"));
                    row.put("createdAt", rs.getTimestamp("created_at"));
                    reviews.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Dashboard all reviews failed: " + e.getMessage());
        }
        return reviews;
    }

    public int getReviewCountByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM product_reviews WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Dashboard review count failed: " + e.getMessage());
        }
        return 0;
    }

    public int getAddressCount(int userId) {
        String sql = "SELECT COUNT(*) FROM addresses WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Dashboard address count failed: " + e.getMessage());
        }
        return 0;
    }
}
