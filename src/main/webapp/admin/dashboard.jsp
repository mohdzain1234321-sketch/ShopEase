<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.shopease.model.Admin" %>

<%
    Admin admin = (Admin) session.getAttribute("loggedInAdmin");

    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/admin/login");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard | ShopEase</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body>

<div class="admin-layout">

    <aside class="sidebar">
        <h2>ShopEase</h2>
        <p>Admin Panel</p>

        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/products">Products</a>
        <a href="${pageContext.request.contextPath}/admin/categories">Categories</a>
        <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
        <a href="${pageContext.request.contextPath}/admin/customers">Customers</a>
        <a href="${pageContext.request.contextPath}/admin/inventory">Inventory</a>

        <form action="${pageContext.request.contextPath}/admin/logout"
              method="post">
            <button type="submit" class="logout-btn">Logout</button>
        </form>
    </aside>

    <main class="admin-content">

        <header class="admin-header">
            <h1>Dashboard</h1>
            <p>Welcome, <%= admin.getUsername() %>!</p>
        </header>

        <section class="dashboard-cards">

            <div class="dashboard-card">
                <h3>Total Products</h3>
                <h2>${totalProducts}</h2>
            </div>

            <div class="dashboard-card">
                <h3>Total Customers</h3>
                <h2>${totalCustomers}</h2>
            </div>

            <div class="dashboard-card">
                <h3>Total Orders</h3>
                <h2>${totalOrders}</h2>
            </div>

            <div class="dashboard-card">
                <h3>Total Revenue</h3>
                <h2>₹ ${totalRevenue}</h2>
            </div>

        </section>

        <div class="dashboard-welcome">
            <h2>Welcome to ShopEase Administration</h2>
            <p>
                Manage your products, categories, customers,
                orders and inventory from one place.
            </p>
        </div>

    </main>

</div>

</body>
</html>