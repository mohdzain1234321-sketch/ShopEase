<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.shopease.model.Order" %>
<%@ page import="com.shopease.model.OrderItem" %>
<%@ page import="com.shopease.model.Payment" %>
<%
    String ctx = request.getContextPath();
    Order order = (Order) request.getAttribute("order");
    List<OrderItem> orderItems = (List<OrderItem>) request.getAttribute("orderItems");
    Payment payment = (Payment) request.getAttribute("payment");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
    String orderStatus = order == null || order.getOrderStatus() == null ? "" : order.getOrderStatus().trim();
    boolean canCancel = "PLACED".equalsIgnoreCase(orderStatus);
    String cancelResult = request.getParameter("cancel");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Order Details | ShopEase</title>
<style>
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:Arial,sans-serif;background:#f4f7fc;color:#243247}
header{background:#fff;padding:20px 7%;display:flex;justify-content:space-between;align-items:center;box-shadow:0 2px 10px rgba(0,0,0,.05)}
.logo{color:#1769d2;font-size:27px;font-weight:bold;text-decoration:none}
header a{color:#1769d2;text-decoration:none;font-weight:bold}
.container{max-width:1050px;margin:45px auto;padding:0 20px}
h1{color:#123b70;margin-bottom:10px}
.subtitle{color:#64748b;margin-bottom:30px}
.panel{background:#fff;padding:25px;border-radius:12px;margin-bottom:22px;box-shadow:0 4px 15px rgba(0,0,0,.04)}
.panel h2{color:#123b70;font-size:20px;margin-bottom:22px}
.details-grid{display:grid;grid-template-columns:repeat(3,1fr);gap:25px}
.label{color:#64748b;font-size:13px;margin-bottom:8px}
.value{font-weight:bold;color:#243247}
.status{display:inline-block;padding:7px 13px;border-radius:20px;background:#e5f8ed;color:#15803d;font-size:13px;font-weight:bold}
.pending{background:#fff4dc;color:#a16207}
.cancelled{background:#fee2e2;color:#b91c1c}
.product-row{display:flex;justify-content:space-between;gap:20px;padding:17px 0;border-bottom:1px solid #e8edf5}
.product-row:last-child{border-bottom:none}
.product-info{line-height:1.8}
.product-name{font-weight:bold;color:#123b70}
.product-price{font-weight:bold;color:#1769d2}
.total-row{display:flex;justify-content:space-between;padding-top:20px;margin-top:10px;border-top:1px solid #e5eaf2;font-size:19px;font-weight:bold}
.back-btn,.cancel-btn{display:inline-block;padding:13px 23px;text-decoration:none;border-radius:7px;font-weight:bold;margin-top:10px;border:0;cursor:pointer;font-size:14px}
.back-btn{background:#1769d2;color:#fff}
.cancel-btn{background:#dc2626;color:#fff;margin-left:10px}
.cancel-btn:hover{background:#b91c1c}
.empty{padding:20px;color:#64748b;background:#f8fafc;border-radius:8px}
.notice{padding:14px 17px;border-radius:8px;margin:0 0 20px;font-size:14px}
.notice.success{background:#e8f8ee;color:#166534}
.notice.error{background:#fee2e2;color:#991b1b}
.action-row{display:flex;flex-wrap:wrap;align-items:center;gap:8px}
@media(max-width:650px){.details-grid{grid-template-columns:1fr}.product-row{flex-direction:column}.cancel-btn{margin-left:0}}
</style>
</head>
<body>
<header><a href="<%=ctx%>/home" class="logo">ShopEase</a><a href="<%=ctx%>/orders">My Orders</a></header>
<div class="container">
<% if (order == null) { %>
    <div class="panel"><h2>Order Not Found</h2><p>The requested order details could not be loaded.</p>
    <a href="<%=ctx%>/orders" class="back-btn">Back to My Orders</a></div>
<% } else { %>
    <h1>Order Details</h1>
    <p class="subtitle">Here is the information about your order.</p>
    <% if ("success".equals(cancelResult)) { %>
        <div class="notice success">Your order has been cancelled successfully. Stock has been restored.</div>
    <% } else if ("failed".equals(cancelResult)) { %>
        <div class="notice error">This order could not be cancelled. It may already be processing, shipped, delivered, or cancelled.</div>
    <% } %>

    <div class="panel"><h2>Order Information</h2><div class="details-grid">
        <div><div class="label">Order ID</div><div class="value">#<%=order.getOrderId()%></div></div>
        <div><div class="label">Order Date</div><div class="value"><%=order.getOrderDate()!=null?dateFormat.format(order.getOrderDate()):"Not available"%></div></div>
        <div><div class="label">Order Status</div><span class="status <%= "CANCELLED".equalsIgnoreCase(orderStatus) ? "cancelled" : "" %>"><%=orderStatus%></span></div>
    </div></div>

    <div class="panel"><h2>Ordered Products</h2>
        <% if(orderItems==null || orderItems.isEmpty()){ %><div class="empty">No products were found for this order.</div>
        <% } else { for(OrderItem item:orderItems){ %>
            <div class="product-row"><div class="product-info">
                <div class="product-name">Product ID: <%=item.getProductId()%></div>
                <div>Variant ID: <%=item.getVariantId()!=null?item.getVariantId():"Standard"%></div>
                <div>Quantity: <%=item.getQuantity()%></div>
            </div><div class="product-price">₹<%=item.getPrice()%></div></div>
        <% }} %>
        <div class="total-row"><span>Order Total</span><span>₹<%=order.getTotalAmount()%></span></div>
    </div>

    <div class="panel"><h2>Payment Information</h2>
        <% if(payment!=null){ %><div class="details-grid">
            <div><div class="label">Payment Method</div><div class="value"><%=payment.getPaymentMethod()%></div></div>
            <div><div class="label">Payment Status</div><span class="status pending"><%=payment.getPaymentStatus()%></span></div>
            <div><div class="label">Transaction Reference</div><div class="value"><%=payment.getTransactionReference()!=null?payment.getTransactionReference():"Not available"%></div></div>
        </div><% } else { %><p class="empty">Payment information is currently unavailable.</p><% } %>
    </div>

    <div class="action-row">
        <a href="<%=ctx%>/orders" class="back-btn">← Back to My Orders</a>
        <% if(canCancel){ %>
            <form method="post" action="<%=ctx%>/orders" onsubmit="return confirm('Are you sure you want to cancel order #<%=order.getOrderId()%>?');">
                <input type="hidden" name="action" value="cancelOrder">
                <input type="hidden" name="orderId" value="<%=order.getOrderId()%>">
                <button type="submit" class="cancel-btn">Cancel Order</button>
            </form>
        <% } %>
    </div>
<% } %>
</div>
</body>
</html>
