package com.shopease.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.shopease.model.*;
import com.shopease.service.*;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {


private static final long serialVersionUID = 1L;

private CartService cartService;
private AddressService addressService;
private ProductService productService;

@Override
public void init() {
    cartService = new CartService();
    addressService = new AddressService();
    productService = new ProductService();
}

@Override
protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect(
            request.getContextPath() + "/login?returnTo=cart"
        );
        return;
    }

    int userId = (Integer) session.getAttribute("userId");

    String productParameter = request.getParameter("productId");

    List<Map<String, Object>> display = new ArrayList<>();

    BigDecimal subtotal = BigDecimal.ZERO;
    BigDecimal discountTotal = BigDecimal.ZERO;

    // =====================================
    // DIRECT BUY NOW CHECKOUT
    // =====================================

    if (productParameter != null && !productParameter.trim().isEmpty()) {

        try {

            int productId = Integer.parseInt(productParameter);

            int quantity = Integer.parseInt(
                request.getParameter("quantity")
            );

            if (productId <= 0 || quantity <= 0) {
                response.sendError(400, "Invalid product or quantity.");
                return;
            }

            Product product = productService.getProductById(productId);

            if (product == null) {
                response.sendError(404, "Product not found.");
                return;
            }

            if (product.getStock() < quantity) {
                response.sendError(400, "Requested quantity is unavailable.");
                return;
            }

            BigDecimal original = product.getPrice();
            BigDecimal percentage = product.getDiscount() == null
                ? BigDecimal.ZERO : product.getDiscount();

            BigDecimal discount = original.multiply(percentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            BigDecimal unitPrice = original.subtract(discount);

            BigDecimal lineOriginal =
                original.multiply(BigDecimal.valueOf(quantity));

            BigDecimal lineDiscount =
                discount.multiply(BigDecimal.valueOf(quantity));

            CartItem item = new CartItem();
            item.setProductId(productId);
            item.setQuantity(quantity);

            Map<String, Object> row = new HashMap<>();

            row.put("item", item);
            row.put("product", product);
            row.put("unitPrice", unitPrice);
            row.put("lineTotal",
                unitPrice.multiply(BigDecimal.valueOf(quantity)));

            display.add(row);

            subtotal = lineOriginal;
            discountTotal = lineDiscount;

            // Remember direct checkout for order placement.
            session.setAttribute("buyNowMode", true);
            session.setAttribute("buyNowProductId", productId);
            session.setAttribute("buyNowQuantity", quantity);

        } catch (NumberFormatException e) {

            response.sendError(400, "Invalid product ID or quantity.");
            return;
        }

    } else {

        // =====================================
        // NORMAL CART CHECKOUT
        // =====================================

        session.removeAttribute("buyNowMode");
        session.removeAttribute("buyNowProductId");
        session.removeAttribute("buyNowQuantity");

        Cart cart = cartService.getCartByUserId(userId);

        if (cart == null) {
            response.sendRedirect(
                request.getContextPath() + "/cart?message=empty"
            );
            return;
        }

        List<CartItem> items =
            cartService.getCartItems(cart.getCartId());

        if (items == null || items.isEmpty()) {
            response.sendRedirect(
                request.getContextPath() + "/cart?message=empty"
            );
            return;
        }

        for (CartItem item : items) {

            Product product =
                productService.getProductById(item.getProductId());

            if (product == null) {
                continue;
            }

            BigDecimal original = product.getPrice();

            BigDecimal percentage = product.getDiscount() == null
                ? BigDecimal.ZERO : product.getDiscount();

            BigDecimal discount = original.multiply(percentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            BigDecimal unitPrice = original.subtract(discount);

            BigDecimal lineOriginal =
                original.multiply(BigDecimal.valueOf(item.getQuantity()));

            BigDecimal lineDiscount =
                discount.multiply(BigDecimal.valueOf(item.getQuantity()));

            Map<String, Object> row = new HashMap<>();

            row.put("item", item);
            row.put("product", product);
            row.put("unitPrice", unitPrice);
            row.put("lineTotal",
                unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())));

            display.add(row);

            subtotal = subtotal.add(lineOriginal);
            discountTotal = discountTotal.add(lineDiscount);
        }

    }

    if (display.isEmpty()) {
        response.sendRedirect(
            request.getContextPath() + "/cart?message=empty"
        );
        return;
    }

    List<Address> addresses =
        addressService.getAddressesByUserId(userId);

    request.setAttribute("checkoutItems", display);
    request.setAttribute("addresses", addresses);

    request.setAttribute("subtotal", subtotal);
    request.setAttribute("discountTotal", discountTotal);
    request.setAttribute("grandTotal",
        subtotal.subtract(discountTotal));

    request.getRequestDispatcher("/views/checkout.jsp")
        .forward(request, response);
}

}
