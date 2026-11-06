<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*,java.text.SimpleDateFormat" %>
<%
    String ctx = request.getContextPath();
    List<Map<String,Object>> reviews = (List<Map<String,Object>>) request.getAttribute("myReviews");
    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>My Reviews | ShopEase</title><link rel="stylesheet" href="<%=ctx%>/css/account.css">
</head>
<body>
<header class="account-header"><a class="brand" href="<%=ctx%>/home">ShopEase<span>.</span></a>
<nav><a href="<%=ctx%>/account">My Account</a><a href="<%=ctx%>/products">Shop</a><a href="<%=ctx%>/orders">My Orders</a></nav></header>
<main class="dashboard">
<div class="welcome-row"><div><span class="eyebrow">YOUR FEEDBACK</span><h1>My Reviews &amp; Ratings</h1><p>All the product reviews you have shared.</p></div></div>
<section class="dashboard-section">
<% if(reviews==null || reviews.isEmpty()){ %>
<div class="empty-card"><span>★</span><strong>No reviews yet</strong><p>Your submitted product reviews will appear here.</p><a href="<%=ctx%>/products">Explore products</a></div>
<% } else { for(Map<String,Object> r:reviews){ %>
<a class="review-preview" style="display:block;margin-bottom:12px" href="<%=ctx%>/reviews?productId=<%=r.get("productId")%>">
<div><strong><%=r.get("productName")%></strong><span class="stars"><%= "★".repeat((Integer)r.get("rating")) %></span></div>
<p><%=r.get("title")==null?r.get("comment"):r.get("title")%></p>
<small><%=r.get("createdAt")==null?"":df.format(r.get("createdAt"))%></small>
</a>
<% }} %>
</section>
</main>
</body></html>
