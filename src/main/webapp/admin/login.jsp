<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Admin Login | ShopEase</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body class="admin-login-body">

    <div class="login-container">

        <div class="login-header">

            <h1>ShopEase</h1>

            <p>Admin Control Panel</p>

        </div>

        <div class="login-card">

            <h2>Welcome Back!</h2>

            <p class="login-subtitle">
                Login to access your admin dashboard
            </p>

            <% if ("invalid_credentials".equals(request.getParameter("error"))) { %>

                <div class="error-message">
                    Invalid username or password.
                </div>

            <% } %>

            <% if ("logged_out".equals(request.getParameter("message"))) { %>

                <div class="success-message">
                    You have successfully logged out.
                </div>

            <% } %>

            <form action="${pageContext.request.contextPath}/admin/login"
                  method="post">

                <div class="form-group">

                    <label for="username">Username</label>

                    <input
                        type="text"
                        id="username"
                        name="username"
                        placeholder="Enter your admin username"
                        required
                        autocomplete="username">

                </div>

                <div class="form-group">

                    <label for="password">Password</label>

                    <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Enter your password"
                        required
                        autocomplete="current-password">

                </div>

                <button type="submit" class="login-btn">
                    Login to Dashboard
                </button>

            </form>

            <div class="login-footer">
                <p>ShopEase Admin Portal</p>
                <span>Authorized administrators only</span>
            </div>

        </div>

    </div>

</body>
</html>