package com.shopease.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopease.dao.CategoryDAO;
import com.shopease.model.Category;
import com.shopease.utility.DBConnection;

public class CategoryDAOImpl implements CategoryDAO {

    // CREATE: Add category
    @Override
    public boolean addCategory(Category category) {

        String sql = "INSERT INTO categories (category_name, description) "
                   + "VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ: Get category by ID
    @Override
    public Category getCategoryById(int categoryId) {

        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return extractCategory(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // READ: Get all categories
    @Override
    public List<Category> getAllCategories() {

        List<Category> categories = new ArrayList<>();

        String sql = "SELECT * FROM categories";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(extractCategory(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // UPDATE: Update category
    @Override
    public boolean updateCategory(Category category) {

        String sql = "UPDATE categories SET category_name = ?, "
                   + "description = ? WHERE category_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getCategoryId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: Delete category
    @Override
    public boolean deleteCategory(int categoryId) {

        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // CHECK: Check category name
    @Override
    public boolean categoryExists(String categoryName) {

        String sql = "SELECT 1 FROM categories WHERE category_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoryName);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Convert ResultSet into Category object
    private Category extractCategory(ResultSet rs) throws SQLException {

        Category category = new Category();

        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));

        return category;
    }
}