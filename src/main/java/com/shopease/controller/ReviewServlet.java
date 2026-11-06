package com.shopease.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.shopease.model.Product;
import com.shopease.service.ProductService;
import com.shopease.service.ReviewService;

@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReviewService reviewService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        reviewService = new ReviewService();
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId;
        try {
            productId = Integer.parseInt(request.getParameter("productId"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID.");
            return;
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found.");
            return;
        }

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            int userId = (Integer) session.getAttribute("userId");
            request.setAttribute("myReview",
                    reviewService.getReviewByUserAndProduct(productId, userId));
        }

        List<Map<String, Object>> reviews = reviewService.getReviews(productId);
        request.setAttribute("reviewProduct", product);
        request.setAttribute("reviews", reviews);
        request.setAttribute("averageRating", reviewService.getAverageRating(productId));
        request.setAttribute("reviewCount", reviewService.getReviewCount(productId));
        request.getRequestDispatcher("/views/product-reviews.jsp").forward(request, response);
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

        int productId;
        try {
            productId = Integer.parseInt(request.getParameter("productId"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID.");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        if ("delete".equals(request.getParameter("action"))) {
            boolean deleted = reviewService.deleteReview(productId, userId);
            response.sendRedirect(request.getContextPath() + "/reviews?productId="
                    + productId + "&message=" + (deleted ? "deleted" : "delete_error"));
            return;
        }

        int rating;
        try {
            rating = Integer.parseInt(request.getParameter("rating"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath()
                    + "/reviews?productId=" + productId + "&message=error");
            return;
        }

        boolean saved = reviewService.saveReview(productId, userId, rating,
                request.getParameter("reviewTitle"), request.getParameter("reviewComment"));
        response.sendRedirect(request.getContextPath() + "/reviews?productId="
                + productId + "&message=" + (saved ? "saved" : "error"));
    }
}
