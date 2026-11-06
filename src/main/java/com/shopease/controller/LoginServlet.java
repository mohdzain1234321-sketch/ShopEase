package com.shopease.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.shopease.model.User;
import com.shopease.service.UserService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null
                || email.isBlank() || password.isBlank()) {
            response.sendRedirect(request.getContextPath()
                    + "/login?error=missing_fields");
            return;
        }

        email = email.trim().toLowerCase();

        User user = userService.loginUser(email, password);

        if (user == null) {
            response.sendRedirect(request.getContextPath()
                    + "/login?error=invalid_credentials");
            return;
        }

        // Invalidate any previous session to prevent session fixation.
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getUserId());

        // Do not keep the stored password hash in the session objaect.
        user.setPassword(null);
        session.setAttribute("loggedInUser", user);
        session.setMaxInactiveInterval(30 * 60);

        // Return the customer to the cart after login when requested.
        String destination = request.getParameter("returnTo");
        if ("cart".equals(destination)) {
            response.sendRedirect(request.getContextPath() + "/cart");
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}
