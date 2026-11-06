<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List,java.math.BigDecimal,com.shopease.model.Product" %>
<%
    List<Product> products = (List<Product>) request.getAttribute("wishlistProducts");
    String message = request.getParameter("message");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Wishlist | ShopEase</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/wishlist.css">
</head>
<body>
<header class="wish-header">
    <a class="brand" href="<%= request.getContextPath() %>/home">ShopEase<span>.</span></a>
    <nav>
        <a href="<%= request.getContextPath() %>/home">Home</a>
        <a href="<%= request.getContextPath() %>/products">Shop</a>
        <a href="<%= request.getContextPath() %>/cart">Cart</a>
        <a href="<%= request.getContextPath() %>/orders">My Orders</a>
    </nav>
</header>

<main class="wish-container">
    <div class="wish-heading">
        <div>
            <p class="eyebrow">YOUR SAVED PICKS</p>
            <h1>My Wishlist <span>♡</span></h1>
            <p class="subtitle">Keep the products you love in one place.</p>
        </div>
        <span class="wish-count"><%= products == null ? 0 : products.size() %> items</span>
    </div>

    <% if ("updated".equals(message)) { %>
        <div class="notice">Your wishlist has been updated.</div>
    <% } else if ("error".equals(message)) { %>
        <div class="notice error">We couldn't update your wishlist. Please try again.</div>
    <% } %>

    <% if (products == null || products.isEmpty()) { %>
        <section class="empty-wishlist">
            <div class="empty-heart">♡</div>
            <h2>Your wishlist is waiting</h2>
            <p>Save products you like and find them here whenever you return.</p>
            <a class="primary-btn" href="<%= request.getContextPath() %>/products">Explore Products</a>
        </section>
    <% } else { %>
        <section class="wish-grid">
        <% for (Product p : products) {
            BigDecimal price = p.getPrice() == null ? BigDecimal.ZERO : p.getPrice();
            BigDecimal discount = p.getDiscount() == null ? BigDecimal.ZERO : p.getDiscount();
            BigDecimal finalPrice = price.subtract(price.multiply(discount).divide(new BigDecimal("100")));
        %>
            <article class="wish-card">
                <a class="wish-image" href="<%= request.getContextPath() %>/product-details?id=<%= p.getProductId() %>">
                    <img src="<%= request.getContextPath() %>/<%= p.getImage() == null ? "images/product-placeholder.png" : p.getImage() %>"
                         alt="<%= p.getProductName() %>">
                </a>
                <div class="wish-info">
                    <h3><%= p.getProductName() %></h3>
                    <p class="wish-price">₹<%= finalPrice.setScale(2, BigDecimal.ROUND_HALF_UP) %>
                        <% if (discount.compareTo(BigDecimal.ZERO) > 0) { %>
                            <del>₹<%= price.setScale(2, BigDecimal.ROUND_HALF_UP) %></del>
                            <span><%= discount.stripTrailingZeros().toPlainString() %>% OFF</span>
                        <% } %>
                    </p>
                    <p class="stock <%= p.getStock() > 0 ? "available" : "unavailable" %>">
                        <%= p.getStock() > 0 ? "In stock" : "Out of stock" %>
                    </p>
                    <div class="wish-actions">
                        <a class="view-btn" href="<%= request.getContextPath() %>/product-details?id=<%= p.getProductId() %>">View Product</a>
                        <form method="post" action="<%= request.getContextPath() %>/wishlist">
                            <input type="hidden" name="action" value="remove">
                            <input type="hidden" name="productId" value="<%= p.getProductId() %>">
                            <button class="remove-btn" type="submit" aria-label="Remove from wishlist">×</button>
                        </form>
                    </div>
                </div>
            </article>
        <% } %>
        </section>
    <% } %>
</main>
</body>
</html>
