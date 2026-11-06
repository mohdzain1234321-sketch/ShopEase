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

import com.shopease.model.User;
import com.shopease.model.Order;
import com.shopease.model.Product;
import com.shopease.service.UserService;
import com.shopease.service.OrderService;
import com.shopease.service.WishlistService;
import com.shopease.service.AccountDashboardService;

@WebServlet("/account")
public class AccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    private OrderService orderService;
    private WishlistService wishlistService;
    private AccountDashboardService dashboardService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        orderService = new OrderService();
        wishlistService = new WishlistService();
        dashboardService = new AccountDashboardService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?returnTo=account");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        User user = userService.getUserById(userId);
        if (user == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        user.setPassword(null);
        List<Order> orders = orderService.getOrdersByUserId(userId);
        List<Product> wishlist = wishlistService.getProducts(userId);
        List<Map<String, Object>> reviews = dashboardService.getRecentReviewsByUser(userId);

        request.setAttribute("profileUser", user);
        request.setAttribute("recentOrders", orders);
        request.setAttribute("wishlistProducts", wishlist);
        request.setAttribute("recentReviews", reviews);
        request.setAttribute("reviewCount", dashboardService.getReviewCountByUser(userId));
        request.setAttribute("addressCount", dashboardService.getAddressCount(userId));
        request.getRequestDispatcher("/views/account.jsp").forward(request, response);
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
        String name = trim(request.getParameter("name"));
        String email = trim(request.getParameter("email"));
        String phone = trim(request.getParameter("phone"));

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            response.sendRedirect(request.getContextPath() + "/account?message=invalid");
            return;
        }

        User updated = new User();
        updated.setUserId(userId);
        updated.setName(name);
        updated.setEmail(email);
        updated.setPhone(phone);

        boolean success = userService.updateUser(updated);
        if (success) {
            User refreshed = userService.getUserById(userId);
            if (refreshed != null) {
                refreshed.setPassword(null);
                session.setAttribute("loggedInUser", refreshed);
            }
        }
        response.sendRedirect(request.getContextPath()
                + "/account?message=" + (success ? "success" : "error"));
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
