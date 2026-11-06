package com.shopease.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopease.model.Product;
import com.shopease.utility.DBConnection;

public class WishlistService {

    public boolean add(int userId, int productId) {
        if (userId <= 0 || productId <= 0) return false;

        String sql = "INSERT IGNORE INTO wishlist_items (user_id, product_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Wishlist add failed: " + e.getMessage());
            return false;
        }
    }

    public boolean remove(int userId, int productId) {
        if (userId <= 0 || productId <= 0) return false;

        String sql = "DELETE FROM wishlist_items WHERE user_id = ? AND product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Wishlist remove failed: " + e.getMessage());
            return false;
        }
    }

    public List<Product> getProducts(int userId) {
        List<Product> products = new ArrayList<>();
        if (userId <= 0) return products;

        String sql = "SELECT p.product_id, p.category_id, p.product_name, p.description, "
                   + "p.price, p.discount, p.image, p.stock, p.created_at "
                   + "FROM wishlist_items w JOIN products p ON p.product_id = w.product_id "
                   + "WHERE w.user_id = ? ORDER BY w.created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setProductName(rs.getString("product_name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setDiscount(rs.getBigDecimal("discount"));
                    p.setImage(rs.getString("image"));
                    p.setStock(rs.getInt("stock"));
                    p.setCreatedAt(rs.getTimestamp("created_at"));
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Wishlist fetch failed: " + e.getMessage());
        }
        return products;
    }

    public boolean contains(int userId, int productId) {
        String sql = "SELECT 1 FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Wishlist check failed: " + e.getMessage());
            return false;
        }
    }
}
