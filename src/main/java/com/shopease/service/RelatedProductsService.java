package com.shopease.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopease.model.Product;
import com.shopease.utility.DBConnection;

public class RelatedProductsService {

    private static final int LIMIT = 4;

    public List<Product> getRelatedProducts(int productId, int categoryId) {
        List<Product> products = new ArrayList<>();

        String categorySql = "SELECT product_id, category_id, product_name, description, "
                + "price, discount, image, stock, created_at "
                + "FROM products WHERE category_id = ? AND product_id <> ? "
                + "AND stock > 0 ORDER BY created_at DESC LIMIT ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(categorySql)) {

            ps.setInt(1, categoryId);
            ps.setInt(2, productId);
            ps.setInt(3, LIMIT);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Related products fetch failed: " + e.getMessage());
            return products;
        }

        if (products.isEmpty()) {
            String fallbackSql = "SELECT product_id, category_id, product_name, description, "
                    + "price, discount, image, stock, created_at "
                    + "FROM products WHERE product_id <> ? AND stock > 0 "
                    + "ORDER BY created_at DESC LIMIT ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(fallbackSql)) {

                ps.setInt(1, productId);
                ps.setInt(2, LIMIT);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        products.add(mapProduct(rs));
                    }
                }
            } catch (SQLException e) {
                System.err.println("Fallback related products fetch failed: " + e.getMessage());
            }
        }

        return products;
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setProductName(rs.getString("product_name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setDiscount(rs.getBigDecimal("discount"));
        product.setImage(rs.getString("image"));
        product.setStock(rs.getInt("stock"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        return product;
    }
}
