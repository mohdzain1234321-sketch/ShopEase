package com.shopease.controller.admin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.shopease.service.ReviewService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/reviews")
public class AdminReviewServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, Object>> reviews =
                reviewService.getAllReviews();

        request.setAttribute("reviews", reviews);

        request.getRequestDispatcher("/admin/reviews.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int reviewId = Integer.parseInt(
                    request.getParameter("reviewId"));

            boolean deleted =
                    reviewService.deleteReviewById(reviewId);

            if (deleted) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/reviews?success=deleted");
            } else {
                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/reviews?error=delete");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/reviews?error=invalid");
        }
    }
}