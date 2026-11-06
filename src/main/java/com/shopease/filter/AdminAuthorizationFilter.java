package com.shopease.filter;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/admin/*"})
public class AdminAuthorizationFilter implements Filter {

    private static final String CSRF_TOKEN = "adminCsrfToken";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI()
                .substring(req.getContextPath().length());

        // Allow admin login without an existing admin session.
        if ("/admin/login".equals(path)
                || "/admin/login.jsp".equals(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        // Verify admin authentication.
        if (session == null
                || session.getAttribute("loggedInAdmin") == null) {

            res.sendRedirect(req.getContextPath() + "/admin/login");
            return;
        }

        // Prevent caching of protected pages.
        res.setHeader("Cache-Control",
                "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        // Generate CSRF token if it does not exist.
        synchronized (session) {
            if (session.getAttribute(CSRF_TOKEN) == null) {
                byte[] bytes = new byte[32];
                RANDOM.nextBytes(bytes);

                String token = Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(bytes);

                session.setAttribute(CSRF_TOKEN, token);
            }
        }

        // Validate every authenticated admin POST request.
        if ("POST".equalsIgnoreCase(req.getMethod())) {

            String expected = (String) session.getAttribute(CSRF_TOKEN);
            String actual = req.getParameter(CSRF_TOKEN);

            if (expected == null || actual == null
                    || !MessageDigest.isEqual(
                            expected.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                            actual.getBytes(java.nio.charset.StandardCharsets.UTF_8))) {

                res.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Invalid CSRF token. Please refresh and try again.");
                return;
            }
        }

        req.setAttribute(CSRF_TOKEN,
                session.getAttribute(CSRF_TOKEN));

        chain.doFilter(request, response);
    }
}