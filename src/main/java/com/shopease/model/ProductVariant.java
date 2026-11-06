package com.shopease.model;

import java.math.BigDecimal;

public class ProductVariant {

    private int variantId;
    private int productId;
    private String size;
    private String color;
    private BigDecimal additionalPrice;
    private int stock;

    // Default Constructor
    public ProductVariant() {
    }

    // Parameterized Constructor
    public ProductVariant(int variantId, int productId, String size,
                          String color, BigDecimal additionalPrice, int stock) {

        this.variantId = variantId;
        this.productId = productId;
        this.size = size;
        this.color = color;
        this.additionalPrice = additionalPrice;
        this.stock = stock;
    }

    // Getters and Setters

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(BigDecimal additionalPrice) {
        this.additionalPrice = additionalPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}