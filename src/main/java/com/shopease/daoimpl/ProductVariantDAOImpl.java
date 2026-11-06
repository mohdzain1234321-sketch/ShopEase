package com.shopease.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopease.dao.ProductVariantDAO;
import com.shopease.model.ProductVariant;
import com.shopease.utility.DBConnection;

public class ProductVariantDAOImpl implements ProductVariantDAO {

    // Add Product Variant
    @Override
    public boolean addProductVariant(ProductVariant variant) {

        String sql = "INSERT INTO product_variants "
                + "(product_id, size, color, additional_price, stock) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, variant.getProductId());
            ps.setString(2, variant.getSize());
            ps.setString(3, variant.getColor());
            ps.setBigDecimal(4, variant.getAdditionalPrice());
            ps.setInt(5, variant.getStock());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Get Variant By ID
    @Override
    public ProductVariant getVariantById(int variantId) {

        String sql = "SELECT * FROM product_variants WHERE variant_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, variantId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractVariant(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Get All Variants
    @Override
    public List<ProductVariant> getAllVariants() {

        List<ProductVariant> variants = new ArrayList<>();

        String sql = "SELECT * FROM product_variants";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                variants.add(extractVariant(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return variants;
    }

    // Get Variants By Product ID
    @Override
    public List<ProductVariant> getVariantsByProductId(int productId) {

        List<ProductVariant> variants = new ArrayList<>();

        String sql = "SELECT * FROM product_variants WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    variants.add(extractVariant(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return variants;
    }

    // Update Product Variant
    @Override
    public boolean updateProductVariant(ProductVariant variant) {

        String sql = "UPDATE product_variants SET product_id = ?, "
                + "size = ?, color = ?, additional_price = ?, stock = ? "
                + "WHERE variant_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, variant.getProductId());
            ps.setString(2, variant.getSize());
            ps.setString(3, variant.getColor());
            ps.setBigDecimal(4, variant.getAdditionalPrice());
            ps.setInt(5, variant.getStock());
            ps.setInt(6, variant.getVariantId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete Product Variant
    @Override
    public boolean deleteProductVariant(int variantId) {

        String sql = "DELETE FROM product_variants WHERE variant_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, variantId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Extract Variant From ResultSet
    private ProductVariant extractVariant(ResultSet rs) throws SQLException {

        ProductVariant variant = new ProductVariant();

        variant.setVariantId(rs.getInt("variant_id"));
        variant.setProductId(rs.getInt("product_id"));
        variant.setSize(rs.getString("size"));
        variant.setColor(rs.getString("color"));
        variant.setAdditionalPrice(rs.getBigDecimal("additional_price"));
        variant.setStock(rs.getInt("stock"));

        return variant;
    }
}