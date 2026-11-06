package com.shopease.service;

import java.math.BigDecimal;
import java.util.List;

import com.shopease.dao.CartDAO;
import com.shopease.dao.CartItemDAO;
import com.shopease.dao.ProductDAO;
import com.shopease.daoimpl.CartDAOImpl;
import com.shopease.daoimpl.CartItemDAOImpl;
import com.shopease.daoimpl.ProductDAOImpl;
import com.shopease.model.Cart;
import com.shopease.model.CartItem;
import com.shopease.model.Product;

public class CartService {

private CartDAO cartDAO = new CartDAOImpl();
private CartItemDAO cartItemDAO = new CartItemDAOImpl();
private ProductDAO productDAO = new ProductDAOImpl();

// 1. Get Customer Cart
public Cart getCartByUserId(int userId) {

    if (userId <= 0) {
        return null;
    }

    return cartDAO.getCartByUserId(userId);
}

// 2. Create Cart
public Cart createCart(int userId) {

    if (userId <= 0) {
        return null;
    }

    Cart existingCart = cartDAO.getCartByUserId(userId);

    if (existingCart != null) {
        return existingCart;
    }

    Cart cart = new Cart();
    cart.setUserId(userId);

    boolean created = cartDAO.addCart(cart);

    if (created) {
        return cartDAO.getCartByUserId(userId);
    }

    return null;
}

// 3. Add Product To Cart
public boolean addToCart(int userId, int productId,
                         Integer variantId, int quantity) {

    if (userId <= 0 || productId <= 0 || quantity <= 0) {
        return false;
    }

    Product product = productDAO.getProductById(productId);

    if (product == null || product.getStock() < quantity) {
        return false;
    }

    Cart cart = getCartByUserId(userId);

    if (cart == null) {
        cart = createCart(userId);
    }

    if (cart == null) {
        return false;
    }

    List<CartItem> items = cartItemDAO.getCartItemsByCartId(
            cart.getCartId()
    );

    // Check whether the same product and variant already exist
    for (CartItem item : items) {

        boolean sameVariant =
                (item.getVariantId() == null && variantId == null)
                || (item.getVariantId() != null
                && item.getVariantId().equals(variantId));

        if (item.getProductId() == productId && sameVariant) {

            int newQuantity = item.getQuantity() + quantity;

            if (newQuantity > product.getStock()) {
                return false;
            }

            return cartItemDAO.updateQuantity(
                    item.getCartItemId(), newQuantity
            );
        }
    }

    CartItem cartItem = new CartItem();

    cartItem.setCartId(cart.getCartId());
    cartItem.setProductId(productId);
    cartItem.setVariantId(variantId);
    cartItem.setQuantity(quantity);

    return cartItemDAO.addCartItem(cartItem);
}

// 4. Get Cart Items
public List<CartItem> getCartItems(int cartId) {

    if (cartId <= 0) {
        return null;
    }

    return cartItemDAO.getCartItemsByCartId(cartId);
}

// 5. Update Cart Item Quantity
public boolean updateCartItemQuantity(int cartItemId, int quantity) {

    if (cartItemId <= 0 || quantity <= 0) {
        return false;
    }

    CartItem cartItem = cartItemDAO.getCartItemById(cartItemId);

    if (cartItem == null) {
        return false;
    }

    Product product = productDAO.getProductById(
            cartItem.getProductId()
    );

    if (product == null || quantity > product.getStock()) {
        return false;
    }

    return cartItemDAO.updateQuantity(cartItemId, quantity);
}

// 6. Remove Cart Item
public boolean removeCartItem(int cartItemId) {

    if (cartItemId <= 0) {
        return false;
    }

    return cartItemDAO.deleteCartItem(cartItemId);
}

// 7. Clear Cart
public boolean clearCart(int cartId) {

    if (cartId <= 0) {
        return false;
    }

    return cartItemDAO.clearCart(cartId);
}

// 8. Get Cart Item Count
public int getCartItemCount(int cartId) {

    if (cartId <= 0) {
        return 0;
    }

    return cartItemDAO.getCartItemCount(cartId);
}

// 9. Calculate Cart Total
public BigDecimal calculateCartTotal(int cartId) {

    if (cartId <= 0) {
        return BigDecimal.ZERO;
    }

    List<CartItem> items = cartItemDAO.getCartItemsByCartId(cartId);

    if (items == null || items.isEmpty()) {
        return BigDecimal.ZERO;
    }

    BigDecimal total = BigDecimal.ZERO;

    for (CartItem item : items) {

        Product product = productDAO.getProductById(
                item.getProductId()
        );

        if (product != null) {

            BigDecimal price = product.getPrice();

            if (product.getDiscount() != null) {

                BigDecimal discount = product.getDiscount();

                BigDecimal discountAmount = price
                        .multiply(discount)
                        .divide(new BigDecimal("100"));

                price = price.subtract(discountAmount);
            }

            BigDecimal itemTotal = price.multiply(
                    BigDecimal.valueOf(item.getQuantity())
            );

            total = total.add(itemTotal);
        }
    }

    return total;
}


}
