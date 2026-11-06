package com.shopease.dao;

import java.util.List;
import com.shopease.model.CartItem;

public interface CartItemDAO {

    boolean addCartItem(CartItem cartItem);

    CartItem getCartItemById(int cartItemId);

    List<CartItem> getAllCartItems();

    List<CartItem> getCartItemsByCartId(int cartId);

    CartItem getCartItemByProduct(int cartId, int productId, Integer variantId);

    boolean updateCartItem(CartItem cartItem);

    boolean updateQuantity(int cartItemId, int quantity);

    boolean deleteCartItem(int cartItemId);

    boolean clearCart(int cartId);

    int getCartItemCount(int cartId);
}