package com.shopease.dao;

import java.util.List;
import com.shopease.model.ProductVariant;

public interface ProductVariantDAO {

    boolean addProductVariant(ProductVariant variant);

    ProductVariant getVariantById(int variantId);

    List<ProductVariant> getAllVariants();

    List<ProductVariant> getVariantsByProductId(int productId);

    boolean updateProductVariant(ProductVariant variant);

    boolean deleteProductVariant(int variantId);
}