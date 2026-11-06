<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("userId") != null) {
        response.sendRedirect(request.getContextPath() + "/home");
        return;
    }
    boolean fromCart = "cart".equals(request.getParameter("returnTo"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | ShopEase</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body class="auth-body">
<main class="auth-shell register-shell">
    <section class="auth-showcase">
        <a class="auth-logo" href="${pageContext.request.contextPath}/home">ShopEase<span>.</span></a>
        <div class="showcase-content">
            <span class="showcase-label">MAKE ROOM FOR SOMETHING NEW</span>
            <h1>Shopping that feels <em>extraordinary.</em></h1>
            <p>Create your ShopEase account and bring your shopping discoveries together in one convenient place.</p>
            <div class="showcase-points">
                <span><b>✓</b> Explore products you'll love</span>
                <span><b>✓</b> Save time during checkout</span>
                <span><b>✓</b> Keep track of your orders</span>
            </div>
        </div>
        <div class="showcase-bottom">Find something that feels like you.</div>
        <div class="showcase-orb"></div>
    </section>

    <section class="auth-form-side">
        <div class="auth-card register-card">
            <div class="mobile-logo">ShopEase<span>.</span></div>
            <div class="form-heading">
                <span class="form-kicker">JOIN SHOPEASE</span>
                <h2>Create your account</h2>
                <p>It takes just a moment to get started.</p>
            </div>

            <% String error = request.getParameter("error"); %>
            <% if (error != null) { %>
                <div class="auth-alert error">
                    <% if ("missing_fields".equals(error)) { %>Please complete all fields.
                    <% } else if ("invalid_name".equals(error)) { %>Please enter a name between 2 and 100 characters.
                    <% } else if ("invalid_email".equals(error)) { %>Please enter a valid email address.
                    <% } else if ("invalid_phone".equals(error)) { %>Enter a valid 10-digit phone number.
                    <% } else if ("weak_password".equals(error)) { %>Your password must contain at least 8 characters.
                    <% } else if ("password_mismatch".equals(error)) { %>Your passwords do not match.
                    <% } else { %>Registration could not be completed. The email may already be registered. Please check and try again.
                    <% } %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/register" method="post"
                  class="auth-form" id="registerForm">
                <% if (fromCart) { %>
                    <input type="hidden" name="returnTo" value="cart">
                <% } %>
                <div class="auth-field">
                    <label for="name">Full name</label>
                    <input id="name" name="name" type="text"
                           placeholder="Enter your full name" minlength="2" maxlength="100"
                           autocomplete="name" required>
                </div>

                <div class="auth-field">
                    <label for="email">Email address</label>
                    <input id="email" name="email" type="email"
                           placeholder="you@example.com" autocomplete="email" required>
                </div>

                <div class="auth-field">
                    <label for="phone">Phone number</label>
                    <input id="phone" name="phone" type="tel"
                           placeholder="10-digit mobile number"
                           inputmode="numeric" pattern="[0-9]{10}" maxlength="10"
                           autocomplete="tel" required>
                </div>

                <div class="auth-field">
                    <label for="password">Password</label>
                    <div class="password-wrap">
                        <input id="password" name="password" type="password"
                               placeholder="At least 8 characters" minlength="8"
                               autocomplete="new-password" required>
                        <button type="button" class="toggle-password" data-target="password">Show</button>
                    </div>
                </div>

                <div class="auth-field">
                    <label for="confirmPassword">Confirm password</label>
                    <div class="password-wrap">
                        <input id="confirmPassword" name="confirmPassword" type="password"
                               placeholder="Enter your password again"
                               minlength="8" autocomplete="new-password" required>
                        <button type="button" class="toggle-password" data-target="confirmPassword">Show</button>
                    </div>
                </div>

                <button type="submit" class="auth-submit">Create account <span>→</span></button>
            </form>

            <p class="auth-switch">Already have an account?
                <a href="${pageContext.request.contextPath}/login<%= fromCart ? "?returnTo=cart" : "" %>">Sign in</a>
            </p>
            <a class="back-home" href="${pageContext.request.contextPath}/home">← Back to shopping</a>
        </div>
        <p class="auth-legal">ShopEase · Your everyday shopping destination</p>
    </section>
</main>
<script src="${pageContext.request.contextPath}/js/auth.js"></script>
</body>
</html>
