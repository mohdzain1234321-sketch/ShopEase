package com.shopease.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.shopease.model.Product;
import com.shopease.service.WishlistService;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private WishlistService wishlistService;

    @Override
    public void init() {
        wishlistService = new WishlistService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        List<Product> products = wishlistService.getProducts(userId);
        request.setAttribute("wishlistProducts", products);

        request.getRequestDispatcher("/views/wishlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            boolean success;

            if ("add".equals(action)) {
                success = wishlistService.add(userId, productId);
            } else if ("remove".equals(action)) {
                success = wishlistService.remove(userId, productId);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid wishlist action.");
                return;
            }

            String message = success ? "updated" : "error";
            String referer = request.getHeader("Referer");

            if ("remove".equals(action) || referer == null
                    || !referer.startsWith(request.getContextPath())) {
                response.sendRedirect(request.getContextPath() + "/wishlist?message=" + message);
            } else {
                response.sendRedirect(referer);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID.");
        }
    }
}
