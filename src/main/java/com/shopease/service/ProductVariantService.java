package com.shopease.service;

import java.math.BigDecimal;
import java.util.List;

import com.shopease.dao.ProductVariantDAO;
import com.shopease.daoimpl.ProductVariantDAOImpl;
import com.shopease.model.ProductVariant;

public class ProductVariantService {

private ProductVariantDAO variantDAO = new ProductVariantDAOImpl();

// 1. Add Product Variant
public boolean addProductVariant(ProductVariant variant) {

    if (variant == null
            || variant.getProductId() <= 0
            || variant.getStock() < 0) {
        return false;
    }

    if (variant.getAdditionalPrice() != null
            && variant.getAdditionalPrice().compareTo(BigDecimal.ZERO) < 0) {
        return false;
    }

    return variantDAO.addProductVariant(variant);
}

// 2. Get Variant By ID
public ProductVariant getVariantById(int variantId) {

    if (variantId <= 0) {
        return null;
    }

    return variantDAO.getVariantById(variantId);
}

// 3. Get All Variants
public List<ProductVariant> getAllVariants() {

    return variantDAO.getAllVariants();
}

// 4. Get Variants By Product ID
public List<ProductVariant> getVariantsByProductId(int productId) {

    if (productId <= 0) {
        return null;
    }

    return variantDAO.getVariantsByProductId(productId);
}

// 5. Update Product Variant
public boolean updateProductVariant(ProductVariant variant) {

    if (variant == null
            || variant.getVariantId() <= 0
            || variant.getProductId() <= 0
            || variant.getStock() < 0) {
        return false;
    }

    if (variant.getAdditionalPrice() != null
            && variant.getAdditionalPrice().compareTo(BigDecimal.ZERO) < 0) {
        return false;
    }

    return variantDAO.updateProductVariant(variant);
}

// 6. Delete Product Variant
public boolean deleteProductVariant(int variantId) {

    if (variantId <= 0) {
        return false;
    }

    return variantDAO.deleteProductVariant(variantId);
}


}
