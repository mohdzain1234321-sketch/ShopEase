package com.shopease.model;

import java.math.BigDecimal;

public class OrderItem {

    private int orderItemId;
    private int orderId;
    private int productId;
    private Integer variantId;
    private int quantity;
    private BigDecimal price;

    // Default Constructor
    public OrderItem() {
    }

    // Parameterized Constructor
    public OrderItem(int orderItemId, int orderId, int productId,
                     Integer variantId, int quantity, BigDecimal price) {

        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.variantId = variantId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}