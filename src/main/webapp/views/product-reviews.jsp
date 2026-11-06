<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*,java.text.SimpleDateFormat,com.shopease.model.Product" %>
<%
    String ctx = request.getContextPath();
    Product product = (Product) request.getAttribute("reviewProduct");
    List<Map<String,Object>> reviews = (List<Map<String,Object>>) request.getAttribute("reviews");
    Double average = (Double) request.getAttribute("averageRating");
    Integer count = (Integer) request.getAttribute("reviewCount");
    Map<String,Object> myReview = (Map<String,Object>) request.getAttribute("myReview");
    boolean loggedIn = session.getAttribute("userId") != null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    int selectedRating = myReview == null ? 0 : (Integer) myReview.get("rating");
    String reviewTitle = myReview == null || myReview.get("title") == null ? "" : (String) myReview.get("title");
    String reviewComment = myReview == null || myReview.get("comment") == null ? "" : (String) myReview.get("comment");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Reviews | ShopEase</title><link rel="stylesheet" href="<%=ctx%>/css/product-reviews.css">
</head>
<body>
<header class="review-header">
    <a class="brand" href="<%=ctx%>/home">ShopEase<span>.</span></a>
    <nav><a href="<%=ctx%>/products">Shop</a><a href="<%=ctx%>/wishlist">Wishlist</a><a href="<%=ctx%>/orders">My Orders</a><a href="<%=ctx%>/account">My Account</a></nav>
</header>
<main class="review-wrap">
    <a class="back-link" href="<%=ctx%>/product-details?id=<%=product.getProductId()%>">← Back to product</a>
    <section class="product-summary">
        <img src="<%=ctx%>/<%=product.getImage()%>" alt="<%=product.getProductName()%>">
        <div><span class="eyebrow">CUSTOMER FEEDBACK</span><h1><%=product.getProductName()%></h1>
            <div class="rating-summary"><strong>★ <%=String.format("%.1f", average == null ? 0.0 : average)%> / 5</strong><span><%=count == null ? 0 : count%> review(s)</span></div>
        </div>
    </section>
    <% if ("saved".equals(request.getParameter("message"))) { %>
        <div class="notice success">Your review has been saved successfully.</div>
    <% } else if ("deleted".equals(request.getParameter("message"))) { %>
        <div class="notice success">Your review has been deleted.</div>
    <% } else if ("error".equals(request.getParameter("message")) || "delete_error".equals(request.getParameter("message"))) { %>
        <div class="notice error">We couldn't complete that action. Please try again.</div>
    <% } %>
    <div class="review-layout">
        <section class="panel">
            <h2><%=myReview == null ? "Write a review" : "Edit your review"%></h2>
            <% if (loggedIn) { %>
            <form method="post" action="<%=ctx%>/reviews" class="review-form">
                <input type="hidden" name="productId" value="<%=product.getProductId()%>">
                <label>Rating</label>
                <select name="rating" required>
                    <option value="">Choose a rating</option>
                    <% for(int i=5;i>=1;i--){ %>
                    <option value="<%=i%>" <%=selectedRating==i?"selected":""%>><%= "★".repeat(i) %> - <%=i==5?"Excellent":i==4?"Very good":i==3?"Good":i==2?"Fair":"Poor"%></option>
                    <% } %>
                </select>
                <label>Review title (optional)</label>
                <input type="text" name="reviewTitle" maxlength="120" value="<%=reviewTitle.replace("&","&amp;").replace("\"","&quot;").replace("<","&lt;").replace(">","&gt;")%>" placeholder="Summarise your experience">
                <label>Your review</label>
                <textarea name="reviewComment" rows="5" maxlength="1000" required placeholder="What did you like or dislike?"><%=reviewComment.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")%></textarea>
                <button type="submit" class="primary-btn"><%=myReview == null ? "Submit Review" : "Update Review"%></button>
            </form>
            <% if (myReview != null) { %>
            <form method="post" action="<%=ctx%>/reviews" class="delete-review-form" onsubmit="return confirm('Are you sure you want to delete your review?');">
                <input type="hidden" name="productId" value="<%=product.getProductId()%>">
                <input type="hidden" name="action" value="delete">
                <button type="submit" class="delete-btn">Delete My Review</button>
            </form>
            <% } %>
            <% } else { %>
                <p class="login-prompt">Please <a href="<%=ctx%>/login">log in</a> to write a review.</p>
            <% } %>
        </section>
        <section class="panel review-list">
            <h2>Customer reviews</h2>
            <% if (reviews == null || reviews.isEmpty()) { %>
                <div class="empty"><span>☆</span><h3>No reviews yet</h3><p>Be the first to share your experience.</p></div>
            <% } else { for (Map<String,Object> review : reviews) {
                int stars = (Integer)review.get("rating");
                java.sql.Timestamp created = (java.sql.Timestamp)review.get("createdAt");
            %>
                <article class="review-card">
                    <div class="review-top"><strong><%=review.get("customerName")%></strong><time><%=created == null ? "" : dateFormat.format(created)%></time></div>
                    <div class="stars"><% for(int i=1;i<=5;i++){ %><%=i<=stars?"★":"☆"%><% } %></div>
                    <% if (review.get("title") != null) { %><h3><%=review.get("title")%></h3><% } %>
                    <p><%=review.get("comment")%></p>
                </article>
            <% } } %>
        </section>
    </div>
</main>
</body></html>
