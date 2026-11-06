package com.shopease.controller.admin;

import java.io.IOException;

import com.shopease.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/customers")
public class AdminCustomerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("loggedInAdmin") == null) {

            response.sendRedirect(
                request.getContextPath() + "/admin/login"
            );
            return;
        }

        request.setAttribute(
            "customers",
            userService.getAllUsers()
        );

        request.getRequestDispatcher(
            "/admin/customers.jsp"
        ).forward(request, response);
    }
}