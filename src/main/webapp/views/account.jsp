<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*,java.text.SimpleDateFormat,com.shopease.model.User,com.shopease.model.Order,com.shopease.model.Product" %>
<%
    String ctx = request.getContextPath();
    User profileUser = (User) request.getAttribute("profileUser");
    List<Order> orders = (List<Order>) request.getAttribute("recentOrders");
    List<Product> wishlist = (List<Product>) request.getAttribute("wishlistProducts");
    List<Map<String,Object>> reviews = (List<Map<String,Object>>) request.getAttribute("recentReviews");
    Integer reviewCount = (Integer) request.getAttribute("reviewCount");
    Integer addressCount = (Integer) request.getAttribute("addressCount");
    String message = request.getParameter("message");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    int orderCount = orders == null ? 0 : orders.size();
    int wishCount = wishlist == null ? 0 : wishlist.size();
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>My Account | ShopEase</title>
<link rel="stylesheet" href="<%=ctx%>/css/account.css">
</head>
<body>
<header class="account-header">
    <a class="brand" href="<%=ctx%>/home">ShopEase<span>.</span></a>
    <nav><a href="<%=ctx%>/home">Home</a><a href="<%=ctx%>/products">Shop</a><a href="<%=ctx%>/cart">Cart</a><a href="<%=ctx%>/logout">Logout</a></nav>
</header>

<main class="dashboard">
    <section class="welcome-row">
        <div>
            <span class="eyebrow">YOUR SHOPEASE ACCOUNT</span>
            <h1>Hello, <%=profileUser == null ? "Shopper" : profileUser.getName()%> 👋</h1>
            <p>Everything you need for your shopping, all in one place.</p>
        </div>
        <div class="avatar"><%=profileUser == null || profileUser.getName() == null || profileUser.getName().isEmpty() ? "U" : profileUser.getName().substring(0,1).toUpperCase()%></div>
    </section>

    <% if ("success".equals(message)) { %><div class="notice success">Your profile has been updated successfully.</div>
    <% } else if ("invalid".equals(message)) { %><div class="notice error">Please enter valid profile details.</div>
    <% } else if ("error".equals(message)) { %><div class="notice error">Profile update failed. The email may already be in use.</div><% } %>

    <section class="stats-grid">
        <a class="stat-card" href="<%=ctx%>/orders"><span class="stat-icon">▣</span><span><strong><%=orderCount%></strong><small>Orders</small></span></a>
        <a class="stat-card" href="<%=ctx%>/wishlist"><span class="stat-icon pink">♡</span><span><strong><%=wishCount%></strong><small>Wishlist items</small></span></a>
        <a class="stat-card" href="<%=ctx%>/my-reviews"><span class="stat-icon gold">★</span><span><strong><%=reviewCount == null ? 0 : reviewCount%></strong><small>Reviews written</small></span></a>
        <a class="stat-card" href="<%=ctx%>/addresses"><span class="stat-icon green">⌖</span><span><strong><%=addressCount == null ? 0 : addressCount%></strong><small>Saved addresses</small></span></a>
    </section>

    <section class="dashboard-section">
        <div class="section-heading"><div><span class="eyebrow">SHOPPING ACTIVITY</span><h2>My Orders</h2></div><a class="view-all" href="<%=ctx%>/orders">View All →</a></div>
        <div class="preview-grid">
        <% if (orders == null || orders.isEmpty()) { %>
            <div class="empty-card"><span>📦</span><strong>No orders yet</strong><p>Your purchases will appear here.</p><a href="<%=ctx%>/products">Start shopping</a></div>
        <% } else { int limit = Math.min(orders.size(), 2); for (int i=0; i<limit; i++) { Order o=orders.get(i); %>
            <a class="order-preview" href="<%=ctx%>/order-details?id=<%=o.getOrderId()%>">
                <div><small>ORDER #<%=o.getOrderId()%></small><strong>₹<%=o.getTotalAmount()%></strong></div>
                <div><span class="status-pill"><%=o.getOrderStatus()%></span><small><%=o.getOrderDate()==null?"":dateFormat.format(o.getOrderDate())%></small></div>
                <b>View details →</b>
            </a>
        <% }} %>
        </div>
    </section>

    <section class="dashboard-section">
        <div class="section-heading"><div><span class="eyebrow">SAVED FOR LATER</span><h2>My Wishlist</h2></div><a class="view-all" href="<%=ctx%>/wishlist">View All →</a></div>
        <div class="product-preview-grid">
        <% if (wishlist == null || wishlist.isEmpty()) { %>
            <div class="empty-card"><span>♡</span><strong>Your wishlist is empty</strong><p>Save products you love to find them here.</p><a href="<%=ctx%>/products">Explore products</a></div>
        <% } else { int limit = Math.min(wishlist.size(), 4); for(int i=0;i<limit;i++){ Product p=wishlist.get(i); %>
            <a class="product-mini" href="<%=ctx%>/product-details?id=<%=p.getProductId()%>">
                <img src="<%=ctx%>/<%=p.getImage()%>" alt="<%=p.getProductName()%>">
                <strong><%=p.getProductName()%></strong><span>₹<%=p.getPrice()%></span>
            </a>
        <% }} %>
        </div>
    </section>

    <section class="dashboard-section">
        <div class="section-heading"><div><span class="eyebrow">YOUR FEEDBACK</span><h2>My Reviews &amp; Ratings</h2></div><a class="view-all" href="<%=ctx%>/my-reviews">View All →</a></div>
        <div class="review-preview-list">
        <% if (reviews == null || reviews.isEmpty()) { %>
            <div class="empty-card"><span>★</span><strong>You haven't written a review yet</strong><p>Share your experience after shopping.</p></div>
        <% } else { for(Map<String,Object> r : reviews){ %>
            <a class="review-preview" href="<%=ctx%>/reviews?productId=<%=r.get("productId")%>">
                <div><strong><%=r.get("productName")%></strong><span class="stars"><%= "★".repeat((Integer)r.get("rating")) %></span></div>
                <p><%=r.get("title") == null ? r.get("comment") : r.get("title")%></p>
                <small><%=r.get("createdAt") == null ? "" : dateFormat.format(r.get("createdAt"))%></small>
            </a>
        <% }} %>
        </div>
    </section>

    <section class="dashboard-section">
        <div class="section-heading"><div><span class="eyebrow">ACCOUNT SETTINGS</span><h2>My Account</h2></div></div>
        <div class="account-links">
            <details class="profile-edit">
                <summary>👤 Personal information <span>Edit profile +</span></summary>
                <form action="<%=ctx%>/account" method="post" class="profile-form">
                    <label>Full name<input type="text" name="name" value="<%=profileUser.getName()%>" required maxlength="100"></label>
                    <label>Email address<input type="email" name="email" value="<%=profileUser.getEmail()%>" required maxlength="150"></label>
                    <label>Phone number<input type="tel" name="phone" value="<%=profileUser.getPhone()%>" required pattern="[0-9+ -]{10,15}" maxlength="15"></label>
                    <button class="primary-btn" type="submit">Save Changes</button>
                </form>
            </details>
            <a href="<%=ctx%>/addresses">📍 Manage addresses <span>→</span></a>
            <a href="<%=ctx%>/wishlist">♡ Saved products <span>→</span></a>
            <a href="<%=ctx%>/logout" class="logout-link">↪ Logout <span>→</span></a>
        </div>
    </section>
</main>
<footer>© 2026 ShopEase. All rights reserved.</footer>
</body>
</html>
