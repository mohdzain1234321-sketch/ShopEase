<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("userId") != null) {
        response.sendRedirect(request.getContextPath() + "/home");
        return;
    }
    String returnTo = request.getParameter("returnTo");
    boolean fromCart = "cart".equals(returnTo);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | ShopEase</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body class="auth-body">
<main class="auth-shell">
    <section class="auth-showcase">
        <a class="auth-logo" href="${pageContext.request.contextPath}/home">ShopEase<span>.</span></a>
        <div class="showcase-content">
            <span class="showcase-label">WELCOME BACK</span>
            <h1>Your next great find is just a <em>click away.</em></h1>
            <p>Sign in to explore your favourites, manage your cart and enjoy a smoother shopping experience.</p>
            <div class="showcase-points">
                <span><b>✓</b> Discover everyday essentials</span>
                <span><b>✓</b> Keep your shopping in one place</span>
                <span><b>✓</b> A simple, secure sign-in experience</span>
            </div>
        </div>
        <div class="showcase-bottom">A better way to shop, every day.</div>
        <div class="showcase-orb"></div>
    </section>

    <section class="auth-form-side">
        <div class="auth-card">
            <div class="mobile-logo">ShopEase<span>.</span></div>
            <div class="form-heading">
                <span class="form-kicker">YOUR SHOPPING JOURNEY</span>
                <h2>Welcome back</h2>
                <p>Enter your details to sign in to your account.</p>
            </div>

            <% if ("registered".equals(request.getParameter("success"))) { %>
                <div class="auth-alert success">Your account has been created. You can now log in.</div>
            <% } %>
            <% if ("invalid_credentials".equals(request.getParameter("error"))) { %>
                <div class="auth-alert error">The email or password you entered is incorrect.</div>
            <% } else if ("missing_fields".equals(request.getParameter("error"))) { %>
                <div class="auth-alert error">Please enter both your email and password.</div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login" method="post" class="auth-form">
                <% if (fromCart) { %>
                    <input type="hidden" name="returnTo" value="cart">
                <% } %>

                <div class="auth-field">
                    <label for="email">Email address</label>
                    <input id="email" name="email" type="email"
                           placeholder="you@example.com" autocomplete="email" required>
                </div>

                <div class="auth-field">
                    <div class="label-row">
                        <label for="password">Password</label>
                    </div>
                    <div class="password-wrap">
                        <input id="password" name="password" type="password"
                               placeholder="Enter your password"
                               autocomplete="current-password" required>
                        <button type="button" class="toggle-password" data-target="password">Show</button>
                    </div>
                </div>

                <button type="submit" class="auth-submit">Sign in <span>→</span></button>
            </form>

            <p class="auth-switch">New to ShopEase?
                <a href="${pageContext.request.contextPath}/register<%= fromCart ? "?returnTo=cart" : "" %>">Create an account</a>
            </p>
            <a class="back-home" href="${pageContext.request.contextPath}/home">← Back to shopping</a>
        </div>
        <p class="auth-legal">ShopEase · Your everyday shopping destination</p>
    </section>
</main>
<script src="${pageContext.request.contextPath}/js/auth.js"></script>
</body>
</html>
