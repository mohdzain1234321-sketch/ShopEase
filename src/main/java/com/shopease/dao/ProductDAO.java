package com.shopease.dao;

import java.math.BigDecimal;
import java.util.List;

import com.shopease.model.Product;

public interface ProductDAO {

    boolean addProduct(Product product);

    Product getProductById(int productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(int categoryId);

    List<Product> searchProducts(String keyword);
    
    List<Product> getProductSuggestions(String keyword);

    List<Product> getAvailableProducts();

    // Filter and sort products
    List<Product> getFilteredProducts(
            String keyword,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            boolean discountOnly,
            String sort
    );

    List<Product> getPaginatedProducts(
            String keyword,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            boolean discountOnly,
            String sort,
            int offset,
            int pageSize
    );

    // Count products matching the selected filters
    int countFilteredProducts(
            String keyword,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            boolean discountOnly
    );
    boolean updateProduct(Product product);
    // Get filtered products for a specific page

    boolean deleteProduct(int productId);
}