package com.shopease.controller;

import java.io.IOException;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.shopease.model.User;
import com.shopease.service.UserService;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = trim(request.getParameter("name"));
        String email = trim(request.getParameter("email")).toLowerCase();
        String phone = trim(request.getParameter("phone"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                || password == null || confirmPassword == null
                || password.isBlank() || confirmPassword.isBlank()) {
            redirectError(request, response, "missing_fields");
            return;
        }

        if (name.length() < 2 || name.length() > 100) {
            redirectError(request, response, "invalid_name");
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            redirectError(request, response, "invalid_email");
            return;
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            redirectError(request, response, "invalid_phone");
            return;
        }

        if (password.length() < 8) {
            redirectError(request, response, "weak_password");
            return;
        }

        if (!password.equals(confirmPassword)) {
            redirectError(request, response, "password_mismatch");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);

        boolean registered = userService.registerUser(user);

        if (registered) {
            String returnTo = request.getParameter("returnTo");
            String suffix = "cart".equals(returnTo) ? "&returnTo=cart" : "";
            response.sendRedirect(request.getContextPath()
                    + "/login?success=registered" + suffix);
        } else {
            // UserService returns false for failed inserts, including duplicate email.
            redirectError(request, response, "registration_failed");
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private void redirectError(HttpServletRequest request,
                               HttpServletResponse response,
                               String code) throws IOException {
        response.sendRedirect(request.getContextPath()
                + "/register?error=" + code);
    }
}
