package com.shopease.dao;

import java.util.List;
import com.shopease.model.Cart;

public interface CartDAO {

    boolean addCart(Cart cart);

    Cart getCartById(int cartId);

    Cart getCartByUserId(int userId);

    List<Cart> getAllCarts();

    boolean updateCart(Cart cart);

    boolean deleteCart(int cartId);

    boolean deleteCartByUserId(int userId);

    boolean cartExists(int userId);
}