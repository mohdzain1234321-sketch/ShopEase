<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="com.shopease.model.Order" %>
<%@ page import="com.shopease.model.Payment" %>

<%
    String ctx = request.getContextPath();

    Order order = (Order) request.getAttribute("order");
    Payment payment = (Payment) request.getAttribute("payment");

    String paymentMethod = "Not available";
    String paymentStatus = "Not available";
    String transactionReference = null;

    if (payment != null) {
        paymentMethod = payment.getPaymentMethod();
        paymentStatus = payment.getPaymentStatus();

        transactionReference = payment.getTransactionReference();

        if (paymentMethod != null) {
            paymentMethod = paymentMethod.trim().toUpperCase();
        }

        if (paymentStatus != null) {
            paymentStatus = paymentStatus.trim().toUpperCase();
        }
    }

    boolean paymentFailed = "FAILED".equals(paymentStatus);
    boolean paymentSuccessful = "SUCCESS".equals(paymentStatus);

    String heading = paymentFailed
            ? "Payment Failed!"
            : "Order Placed Successfully!";

    String message = paymentFailed
            ? "Your simulated payment was unsuccessful. Your order has been recorded."
            : "Thank you for shopping with ShopEase. Your order has been placed successfully.";

    String displayMethod = paymentMethod;

    if ("COD".equals(paymentMethod)) {
        displayMethod = "Cash on Delivery";
    } else if ("UPI".equals(paymentMethod)) {
        displayMethod = "UPI";
    } else if ("CARD".equals(paymentMethod)) {
        displayMethod = "Debit / Credit Card";
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Order Confirmation | ShopEase</title>

<style>

* {
    box-sizing: border-box;
}

body {
    margin: 0;
    font-family: Arial, sans-serif;
    background: #f4f7fc;
}

.success-container {

    max-width: 660px;
    margin: 70px auto;
    padding: 45px 30px;
    background: white;
    border-radius: 15px;
    text-align: center;
    box-shadow: 0 5px 25px rgba(0,0,0,0.08);

}

.success-icon {

    width: 85px;
    height: 85px;
    margin: 0 auto 20px;
    background: #e5f8ed;
    color: #16a34a;
    border-radius: 50%;
    font-size: 55px;
    line-height: 85px;

}

.failed-icon {

    background: #fff0ef;
    color: #dc2626;

}

h1 {

    color: #123b70;
    margin-bottom: 12px;

}

.message {

    color: #64748b;
    line-height: 1.6;

}

.payment-info {

    margin: 25px 0;
    padding: 18px;
    background: #f0f6ff;
    border-radius: 8px;
    color: #334155;
    line-height: 1.8;

}

.status {

    font-weight: bold;

}

.status.SUCCESS {
    color: #16a34a;
}

.status.FAILED {
    color: #dc2626;
}

.status.PENDING {
    color: #d97706;
}

.buttons {

    display: flex;
    justify-content: center;
    gap: 15px;
    flex-wrap: wrap;
    margin-top: 25px;

}

.btn {

    display: inline-block;
    padding: 13px 22px;
    border-radius: 7px;
    text-decoration: none;
    font-weight: bold;

}

.primary {

    background: #1769d2;
    color: white;

}

.secondary {

    border: 1px solid #1769d2;
    color: #1769d2;

}

.btn:hover {
    opacity: 0.85;
}

@media(max-width: 650px) {

    .success-container {
        margin: 35px 15px;
    }

}

</style>

</head>

<body>

<div class="success-container">

    <div class="success-icon <%= paymentFailed ? "failed-icon" : "" %>">

        <%= paymentFailed ? "&#10006;" : "&#10003;" %>

    </div>

    <h1><%= heading %></h1>

    <p class="message">
        <%= message %>
    </p>

    <div class="payment-info">

        <strong>Payment Method:</strong>
        <%= displayMethod %>

        <br><br>

        <strong>Payment Status:</strong>

        <span class="status <%= paymentStatus %>">
            <%= paymentStatus %>
        </span>

        <% if (order != null) { %>

            <br><br>

            <strong>Order ID:</strong>
            #<%= order.getOrderId() %>

        <% } %>

        <% if (transactionReference != null
                && !transactionReference.trim().isEmpty()) { %>

            <br><br>

            <strong>Transaction Reference:</strong>
            <%= transactionReference %>

        <% } %>

    </div>

    <p class="message">

        You can track your order and view its details
        from your My Orders section.

    </p>

    <div class="buttons">

        <a href="<%= ctx %>/orders" class="btn primary">
            View My Orders
        </a>

        <a href="<%= ctx %>/home" class="btn secondary">
            Continue Shopping
        </a>

    </div>

</div>

</body>
</html>