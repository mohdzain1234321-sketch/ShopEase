package com.shopease.controller.admin;

import java.io.IOException;

import com.shopease.model.Admin;
import com.shopease.service.AdminService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        adminService = new AdminService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Admin admin;
        try {
            admin = adminService.authenticate(username, password);
        } catch (RuntimeException ex) {
            throw new ServletException("Admin authentication could not be completed.", ex);
        }

        if (admin == null) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/login?error=invalid_credentials");
            return;
        }

        HttpSession session = request.getSession(true);
        request.changeSessionId(); // Prevent session fixation without invalidating customer session.
        session.setAttribute("loggedInAdmin", admin);
        session.setMaxInactiveInterval(30 * 60);

        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
    }
}
