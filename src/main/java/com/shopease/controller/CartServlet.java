package com.shopease.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shopease.model.Cart;
import com.shopease.model.CartItem;
import com.shopease.model.Product;
import com.shopease.service.CartService;
import com.shopease.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CartService cartService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        cartService = new CartService();
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?returnTo=cart");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        Cart cart = cartService.getCartByUserId(userId);
        List<Map<String, Object>> displayItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal savings = BigDecimal.ZERO;
        int itemCount = 0;

        if (cart != null) {
            List<CartItem> items = cartService.getCartItems(cart.getCartId());
            if (items != null) {
                for (CartItem item : items) {
                    Product product = productService.getProductById(item.getProductId());
                    if (product == null) continue;

                    BigDecimal original = product.getPrice() == null
                            ? BigDecimal.ZERO : product.getPrice();
                    BigDecimal discountPercent = product.getDiscount() == null
                            ? BigDecimal.ZERO : product.getDiscount();
                    BigDecimal discountAmount = original.multiply(discountPercent)
                            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                    BigDecimal unitPrice = original.subtract(discountAmount);
                    BigDecimal lineSubtotal = original.multiply(BigDecimal.valueOf(item.getQuantity()));
                    BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                    BigDecimal lineSavings = lineSubtotal.subtract(lineTotal);

                    Map<String, Object> row = new HashMap<>();
                    row.put("cartItem", item);
                    row.put("product", product);
                    row.put("originalPrice", original);
                    row.put("unitPrice", unitPrice);
                    row.put("lineSubtotal", lineSubtotal);
                    row.put("lineTotal", lineTotal);
                    row.put("lineSavings", lineSavings);
                    displayItems.add(row);

                    subtotal = subtotal.add(lineSubtotal);
                    savings = savings.add(lineSavings);
                    itemCount += item.getQuantity();
                }
            }
        }

        BigDecimal total = subtotal.subtract(savings);
        session.setAttribute("cartItemCount", itemCount);
        request.setAttribute("cartDisplayItems", displayItems);
        request.setAttribute("cartSubtotal", subtotal);
        request.setAttribute("cartSavings", savings);
        request.setAttribute("cartTotal", total);
        request.setAttribute("cartItemCount", itemCount);
        request.getRequestDispatcher("/views/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?returnTo=cart");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");
        boolean success = false;

        try {
            if ("add".equals(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                String variantValue = request.getParameter("variantId");
                Integer variantId = (variantValue == null || variantValue.trim().isEmpty())
                        ? null : Integer.valueOf(variantValue);
                success = cartService.addToCart(userId, productId, variantId, quantity);
            } else if ("update".equals(action)) {
                int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                if (ownsCartItem(userId, cartItemId)) {
                    success = cartService.updateCartItemQuantity(cartItemId, quantity);
                }
            } else if ("remove".equals(action)) {
                int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
                if (ownsCartItem(userId, cartItemId)) {
                    success = cartService.removeCartItem(cartItemId);
                }
            } else if ("clear".equals(action)) {
                Cart cart = cartService.getCartByUserId(userId);
                success = cart == null || cartService.clearCart(cart.getCartId());
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown cart action.");
                return;
            }
        } catch (NumberFormatException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid cart information.");
            return;
        }

        Cart cart = cartService.getCartByUserId(userId);
        int count = cart == null ? 0 : cartService.getCartItemCount(cart.getCartId());
        session.setAttribute("cartItemCount", count);
        response.sendRedirect(request.getContextPath() + "/cart?message=" + (success ? "success" : "error"));
    }

    private boolean ownsCartItem(int userId, int cartItemId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) return false;
        List<CartItem> items = cartService.getCartItems(cart.getCartId());
        if (items == null) return false;
        for (CartItem item : items) {
            if (item.getCartItemId() == cartItemId) return true;
        }
        return false;
    }
}
