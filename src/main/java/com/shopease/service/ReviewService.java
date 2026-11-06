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

public class ReviewService {

    public boolean saveReview(int productId, int userId, int rating, String title, String comment) {
        if (productId <= 0 || userId <= 0 || rating < 1 || rating > 5
                || comment == null || comment.trim().isEmpty()
                || comment.trim().length() > 1000
                || (title != null && title.trim().length() > 120)) return false;

        String sql = "INSERT INTO product_reviews (product_id,user_id,rating,review_title,review_comment) "
                + "VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE rating=VALUES(rating), "
                + "review_title=VALUES(review_title), review_comment=VALUES(review_comment), "
                + "updated_at=CURRENT_TIMESTAMP";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, userId);
            ps.setInt(3, rating);
            ps.setString(4, title == null || title.trim().isEmpty() ? null : title.trim());
            ps.setString(5, comment.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteReview(int productId, int userId) {
        String sql = "DELETE FROM product_reviews WHERE product_id=? AND user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> getReviewByUserAndProduct(int productId, int userId) {
        String sql = "SELECT review_id,rating,review_title,review_comment "
                + "FROM product_reviews WHERE product_id=? AND user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("reviewId", rs.getInt("review_id"));
                    row.put("rating", rs.getInt("rating"));
                    row.put("title", rs.getString("review_title"));
                    row.put("comment", rs.getString("review_comment"));
                    return row;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> getReviews(int productId) {
        List<Map<String, Object>> reviews = new ArrayList<>();
        String sql = "SELECT r.review_id,r.rating,r.review_title,r.review_comment,r.created_at,u.name "
                + "FROM product_reviews r JOIN users u ON r.user_id=u.user_id "
                + "WHERE r.product_id=? ORDER BY r.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("reviewId", rs.getInt("review_id"));
                    row.put("rating", rs.getInt("rating"));
                    row.put("title", rs.getString("review_title"));
                    row.put("comment", rs.getString("review_comment"));
                    row.put("createdAt", rs.getTimestamp("created_at"));
                    row.put("customerName", rs.getString("name"));
                    reviews.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public double getAverageRating(int productId) {
        String sql = "SELECT AVG(rating) FROM product_reviews WHERE product_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getReviewCount(int productId) {
        String sql = "SELECT COUNT(*) FROM product_reviews WHERE product_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
 // Fetch all customer reviews for Admin Panel
    public List<Map<String, Object>> getAllReviews() {

        List<Map<String, Object>> reviews = new ArrayList<>();

        String sql = "SELECT r.review_id, r.product_id, r.user_id, "
                + "r.rating, r.review_title, r.review_comment, r.created_at, "
                + "p.product_name, u.name AS customer_name, u.email "
                + "FROM product_reviews r "
                + "JOIN products p ON r.product_id = p.product_id "
                + "JOIN users u ON r.user_id = u.user_id "
                + "ORDER BY r.created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Map<String, Object> row = new HashMap<>();

                row.put("reviewId", rs.getInt("review_id"));
                row.put("productId", rs.getInt("product_id"));
                row.put("userId", rs.getInt("user_id"));
                row.put("rating", rs.getInt("rating"));
                row.put("title", rs.getString("review_title"));
                row.put("comment", rs.getString("review_comment"));
                row.put("createdAt", rs.getTimestamp("created_at"));
                row.put("productName", rs.getString("product_name"));
                row.put("customerName", rs.getString("customer_name"));
                row.put("email", rs.getString("email"));

                reviews.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }


    // Delete review from Admin Panel using Review ID
    public boolean deleteReviewById(int reviewId) {

        String sql = "DELETE FROM product_reviews WHERE review_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reviewId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
