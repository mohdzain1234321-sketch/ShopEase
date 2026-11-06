package com.shopease.model;

public class CartItem {

    private int cartItemId;
    private int cartId;
    private int productId;
    private Integer variantId;
    private int quantity;

    // Default Constructor
    public CartItem() {
    }

    // Parameterized Constructor
    public CartItem(int cartItemId, int cartId, int productId,
                    Integer variantId, int quantity) {

        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.productId = productId;
        this.variantId = variantId;
        this.quantity = quantity;
    }

    // Getters and Setters

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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
}