package com.shopease.daoimpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopease.dao.ProductDAO;
import com.shopease.model.Product;
import com.shopease.utility.DBConnection;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public boolean addProduct(Product product) {

        String sql = "INSERT INTO products "
                + "(category_id, product_name, description, price, discount, image, stock) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setBigDecimal(5, product.getDiscount());
            ps.setString(6, product.getImage());
            ps.setInt(7, product.getStock());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Product getProductById(int productId) {

        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractProduct(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(extractProduct(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public List<Product> getProductsByCategory(int categoryId) {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products WHERE category_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProduct(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public List<Product> searchProducts(String keyword) {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products WHERE product_name LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProduct(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
    
    @Override
    public List<Product> getProductSuggestions(String keyword) {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products "
                   + "WHERE product_name LIKE ? "
                   + "ORDER BY product_name ASC "
                   + "LIMIT 5";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProduct(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public List<Product> getAvailableProducts() {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products WHERE stock > 0";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(extractProduct(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // Filter and sort products
    @Override
    public List<Product> getFilteredProducts(
            String keyword,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            boolean discountOnly,
            String sort) {

        List<Product> products = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM products WHERE 1=1"
        );

        List<Object> parameters = new ArrayList<>();

        // Search by product name or description
        if (keyword != null && !keyword.trim().isEmpty()) {

            sql.append(" AND (product_name LIKE ? OR description LIKE ?)");

            parameters.add("%" + keyword.trim() + "%");
            parameters.add("%" + keyword.trim() + "%");
        }

        // Filter by category
        if (categoryId != null) {

            sql.append(" AND category_id = ?");
            parameters.add(categoryId);
        }

        // Minimum price
        if (minPrice != null) {

            sql.append(" AND price >= ?");
            parameters.add(minPrice);
        }

        // Maximum price
        if (maxPrice != null) {

            sql.append(" AND price <= ?");
            parameters.add(maxPrice);
        }

        // Discounted products only
        if (discountOnly) {

            sql.append(" AND discount > 0");
        }

        // Sorting
        if ("priceLow".equals(sort)) {

            sql.append(" ORDER BY price ASC");

        } else if ("priceHigh".equals(sort)) {

            sql.append(" ORDER BY price DESC");

        } else {

            sql.append(" ORDER BY created_at DESC");
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            // Set dynamic query parameters
            for (int i = 0; i < parameters.size(); i++) {

                Object parameter = parameters.get(i);

                if (parameter instanceof String) {
                    ps.setString(i + 1, (String) parameter);

                } else if (parameter instanceof Integer) {
                    ps.setInt(i + 1, (Integer) parameter);

                } else if (parameter instanceof BigDecimal) {
                    ps.setBigDecimal(i + 1, (BigDecimal) parameter);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    products.add(extractProduct(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

 // Get filtered products for a specific page
    @Override
    public List<Product> getPaginatedProducts(
            String keyword,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            boolean discountOnly,
            String sort,
            int offset,
            int pageSize) {

        List<Product> products = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM products WHERE 1=1"
        );

        List<Object> parameters = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (product_name LIKE ? OR description LIKE ?)");
            parameters.add("%" + keyword.trim() + "%");
            parameters.add("%" + keyword.trim() + "%");
        }

        if (categoryId != null) {
            sql.append(" AND category_id = ?");
            parameters.add(categoryId);
        }

        if (minPrice != null) {
            sql.append(" AND price >= ?");
            parameters.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            parameters.add(maxPrice);
        }

        if (discountOnly) {
            sql.append(" AND discount > 0");
        }

        if ("deals".equals(sort)) {

            sql.append(" ORDER BY discount DESC, created_at DESC");

        } else if ("priceLow".equals(sort)) {

            sql.append(" ORDER BY price ASC");

        } else if ("priceHigh".equals(sort)) {

            sql.append(" ORDER BY price DESC");

        } else {

            sql.append(" ORDER BY created_at DESC");

        }
        sql.append(" LIMIT ? OFFSET ?");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;

            for (Object parameter : parameters) {

                if (parameter instanceof String) {
                    ps.setString(index++, (String) parameter);
                } else if (parameter instanceof Integer) {
                    ps.setInt(index++, (Integer) parameter);
                } else if (parameter instanceof BigDecimal) {
                    ps.setBigDecimal(index++, (BigDecimal) parameter);
                }
            }

            ps.setInt(index++, pageSize);
            ps.setInt(index, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProduct(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }


    // Count products matching filters
    @Override
    public int countFilteredProducts(
            String keyword,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            boolean discountOnly) {

        int total = 0;

        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM products WHERE 1=1"
        );

        List<Object> parameters = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (product_name LIKE ? OR description LIKE ?)");
            parameters.add("%" + keyword.trim() + "%");
            parameters.add("%" + keyword.trim() + "%");
        }

        if (categoryId != null) {
            sql.append(" AND category_id = ?");
            parameters.add(categoryId);
        }

        if (minPrice != null) {
            sql.append(" AND price >= ?");
            parameters.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            parameters.add(maxPrice);
        }

        if (discountOnly) {
            sql.append(" AND discount > 0");
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;

            for (Object parameter : parameters) {

                if (parameter instanceof String) {
                    ps.setString(index++, (String) parameter);
                } else if (parameter instanceof Integer) {
                    ps.setInt(index++, (Integer) parameter);
                } else if (parameter instanceof BigDecimal) {
                    ps.setBigDecimal(index++, (BigDecimal) parameter);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
    
    
    
    
    @Override
    public boolean updateProduct(Product product) {

        String sql = "UPDATE products SET category_id = ?, product_name = ?, "
                + "description = ?, price = ?, discount = ?, image = ?, stock = ? "
                + "WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setBigDecimal(5, product.getDiscount());
            ps.setString(6, product.getImage());
            ps.setInt(7, product.getStock());
            ps.setInt(8, product.getProductId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteProduct(int productId) {

        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Product extractProduct(ResultSet rs) throws SQLException {

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