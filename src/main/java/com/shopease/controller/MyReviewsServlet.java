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

import com.shopease.service.AccountDashboardService;

@WebServlet("/my-reviews")
public class MyReviewsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AccountDashboardService dashboardService;

    @Override
    public void init() {
        dashboardService = new AccountDashboardService();
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
        List<Map<String, Object>> reviews = dashboardService.getAllReviewsByUser(userId);
        request.setAttribute("myReviews", reviews);
        request.getRequestDispatcher("/views/my-reviews.jsp").forward(request, response);
    }
}
