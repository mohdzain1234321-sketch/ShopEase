package com.shopease.service;

import java.math.BigDecimal;
import java.util.List;

import com.shopease.dao.ProductDAO;
import com.shopease.daoimpl.ProductDAOImpl;
import com.shopease.model.Product;

public class ProductService {

private ProductDAO productDAO = new ProductDAOImpl();

// 1. Add Product
public boolean addProduct(Product product) {

    if (product == null) {
        return false;
    }

    if (product.getProductName() == null
            || product.getProductName().trim().isEmpty()
            || product.getPrice() == null
            || product.getPrice().compareTo(BigDecimal.ZERO) < 0
            || product.getStock() < 0) {
        return false;
    }

    if (product.getDiscount() != null
            && (product.getDiscount().compareTo(BigDecimal.ZERO) < 0
            || product.getDiscount().compareTo(new BigDecimal("100")) > 0)) {
        return false;
    }

    return productDAO.addProduct(product);
}

// 2. Get Product By ID
public Product getProductById(int productId) {

    if (productId <= 0) {
        return null;
    }

    return productDAO.getProductById(productId);
}

// 3. Get All Products
public List<Product> getAllProducts() {

    return productDAO.getAllProducts();
}

// 4. Get Products By Category
public List<Product> getProductsByCategory(int categoryId) {

    if (categoryId <= 0) {
        return null;
    }

    return productDAO.getProductsByCategory(categoryId);
}

// 5. Search Products
public List<Product> searchProducts(String keyword) {

    if (keyword == null || keyword.trim().isEmpty()) {
        return productDAO.getAllProducts();
    }

    return productDAO.searchProducts(keyword.trim());
}

public List<Product> getProductSuggestions(String keyword) {
    return productDAO.getProductSuggestions(keyword);
}

// 6. Get Available Products
public List<Product> getAvailableProducts() {

    return productDAO.getAvailableProducts();
}

// 7. Get Filtered and Sorted Products
public List<Product> getFilteredProducts(
        String keyword,
        Integer categoryId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        boolean discountOnly,
        String sort) {

    // Validate category
    if (categoryId != null && categoryId <= 0) {
        return null;
    }

    // Validate minimum price
    if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) < 0) {
        return null;
    }

    // Validate maximum price
    if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
        return null;
    }

    // Minimum price cannot be greater than maximum price
    if (minPrice != null && maxPrice != null
            && minPrice.compareTo(maxPrice) > 0) {
        return null;
    }

    // Allow only supported sorting options
    if (!"priceLow".equals(sort)
            && !"priceHigh".equals(sort)
            && !"newest".equals(sort)) {
        sort = "newest";
    }

    // Remove unnecessary spaces from search keyword
    if (keyword != null) {
        keyword = keyword.trim();
    }

    return productDAO.getFilteredProducts(
            keyword,
            categoryId,
            minPrice,
            maxPrice,
            discountOnly,
            sort
    );
}

//8. Get Paginated Products
public List<Product> getPaginatedProducts(
     String keyword,
     Integer categoryId,
     BigDecimal minPrice,
     BigDecimal maxPrice,
     boolean discountOnly,
     String sort,
     int offset,
     int pageSize) {

 if (offset < 0 || pageSize <= 0) {
     return java.util.Collections.emptyList();
 }

 if (categoryId != null && categoryId <= 0) {
     return java.util.Collections.emptyList();
 }

 if (minPrice != null
         && minPrice.compareTo(BigDecimal.ZERO) < 0) {
     return java.util.Collections.emptyList();
 }

 if (maxPrice != null
         && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
     return java.util.Collections.emptyList();
 }

 if (minPrice != null && maxPrice != null
         && minPrice.compareTo(maxPrice) > 0) {
     return java.util.Collections.emptyList();
 }

 if (!"priceLow".equals(sort)
         && !"priceHigh".equals(sort)
         && !"newest".equals(sort)) {
     sort = "newest";
 }

 return productDAO.getPaginatedProducts(
         keyword,
         categoryId,
         minPrice,
         maxPrice,
         discountOnly,
         sort,
         offset,
         pageSize
 );
}


//9. Count Filtered Products
public int countFilteredProducts(
     String keyword,
     Integer categoryId,
     BigDecimal minPrice,
     BigDecimal maxPrice,
     boolean discountOnly) {

 if (categoryId != null && categoryId <= 0) {
     return 0;
 }

 if (minPrice != null
         && minPrice.compareTo(BigDecimal.ZERO) < 0) {
     return 0;
 }

 if (maxPrice != null
         && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
     return 0;
 }

 if (minPrice != null && maxPrice != null
         && minPrice.compareTo(maxPrice) > 0) {
     return 0;
 }

 return productDAO.countFilteredProducts(
         keyword,
         categoryId,
         minPrice,
         maxPrice,
         discountOnly
 );
}

// 8. Update Product
public boolean updateProduct(Product product) {

    if (product == null || product.getProductId() <= 0) {
        return false;
    }

    if (product.getProductName() == null
            || product.getProductName().trim().isEmpty()
            || product.getPrice() == null
            || product.getPrice().compareTo(BigDecimal.ZERO) < 0
            || product.getStock() < 0) {
        return false;
    }

    if (product.getDiscount() != null
            && (product.getDiscount().compareTo(BigDecimal.ZERO) < 0
            || product.getDiscount().compareTo(new BigDecimal("100")) > 0)) {
        return false;
    }

    return productDAO.updateProduct(product);
}

// 9. Delete Product
public boolean deleteProduct(int productId) {

    if (productId <= 0) {
        return false;
    }

    return productDAO.deleteProduct(productId);
}

}
