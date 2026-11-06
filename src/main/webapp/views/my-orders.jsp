<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.shopease.model.Order" %>

<%
    String ctx = request.getContextPath();
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>My Orders | ShopEase</title>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: Arial, sans-serif;
    background: #f4f7fc;
    color: #243247;
}

header {
    background: #ffffff;
    padding: 19px 7%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.logo {
    color: #1769d2;
    font-size: 27px;
    font-weight: bold;
    text-decoration: none;
}

.header-link {
    color: #1769d2;
    text-decoration: none;
    font-weight: bold;
}

.container {
    max-width: 1100px;
    margin: 45px auto;
    padding: 0 20px;
}

.page-heading {
    margin-bottom: 28px;
}

.page-heading h1 {
    color: #123b70;
    font-size: 30px;
    margin-bottom: 9px;
}

.page-heading p {
    color: #64748b;
    line-height: 1.5;
}

.order-card {
    background: #ffffff;
    border-radius: 13px;
    padding: 23px 25px;
    margin-bottom: 18px;
    box-shadow: 0 4px 16px rgba(0,0,0,0.045);
    border: 1px solid #edf1f7;
}

.order-top {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 15px;
    padding-bottom: 18px;
    border-bottom: 1px solid #e8edf5;
}

.order-number {
    color: #123b70;
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 7px;
}

.order-date {
    color: #64748b;
    font-size: 13px;
}

.status-badge {
    display: inline-block;
    padding: 7px 13px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: bold;
    text-transform: uppercase;
    white-space: nowrap;
}

.status-placed {
    background: #e5f0ff;
    color: #175cd3;
}

.status-processing {
    background: #fff4dc;
    color: #a16207;
}

.status-shipped {
    background: #f0eaff;
    color: #6941c6;
}

.status-delivered {
    background: #e5f8ed;
    color: #15803d;
}

.status-cancelled {
    background: #fee2e2;
    color: #b91c1c;
}

.status-default {
    background: #eef2f6;
    color: #475467;
}

.order-info {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 20px;
    padding: 20px 0;
}

.info-label {
    color: #64748b;
    font-size: 12px;
    margin-bottom: 7px;
}

.info-value {
    color: #243247;
    font-size: 15px;
    font-weight: bold;
}

.order-bottom {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 15px;
    border-top: 1px solid #e8edf5;
    padding-top: 17px;
}

.total-label {
    color: #64748b;
    font-size: 13px;
    margin-bottom: 5px;
}

.total-amount {
    color: #1769d2;
    font-size: 20px;
    font-weight: bold;
}

.details-btn {
    display: inline-block;
    background: #1769d2;
    color: #ffffff;
    text-decoration: none;
    padding: 11px 18px;
    border-radius: 7px;
    font-size: 13px;
    font-weight: bold;
    transition: background 0.2s ease;
}

.details-btn:hover {
    background: #104fa8;
}

.empty-state {
    background: #ffffff;
    padding: 55px 25px;
    border-radius: 13px;
    text-align: center;
    box-shadow: 0 4px 16px rgba(0,0,0,0.045);
}

.empty-icon {
    font-size: 43px;
    margin-bottom: 15px;
}

.empty-state h2 {
    color: #123b70;
    margin-bottom: 10px;
}

.empty-state p {
    color: #64748b;
    margin-bottom: 22px;
    line-height: 1.5;
}

.shop-btn {
    display: inline-block;
    background: #1769d2;
    color: white;
    text-decoration: none;
    padding: 12px 21px;
    border-radius: 7px;
    font-weight: bold;
}

@media (max-width: 650px) {
    header {
        padding: 16px 5%;
    }

    .container {
        margin: 30px auto;
    }

    .page-heading h1 {
        font-size: 25px;
    }

    .order-card {
        padding: 18px;
    }

    .order-top {
        flex-direction: column;
    }

    .order-info {
        grid-template-columns: 1fr;
        gap: 15px;
    }

    .order-bottom {
        align-items: flex-start;
        flex-direction: column;
    }

    .details-btn {
        width: 100%;
        text-align: center;
    }
}
</style>
</head>

<body>

<header>
    <a href="<%=ctx%>/home" class="logo">ShopEase</a>
    <a href="<%=ctx%>/home" class="header-link">Continue Shopping</a>
</header>

<main class="container">

    <div class="page-heading">
        <h1>My Orders</h1>
        <p>Track your purchases and view your order details.</p>
    </div>

    <% if (orders == null || orders.isEmpty()) { %>

        <section class="empty-state">
            <div class="empty-icon">📦</div>
            <h2>No orders yet</h2>
            <p>Your orders will appear here after you complete a purchase.</p>
            <a href="<%=ctx%>/home" class="shop-btn">Start Shopping</a>
        </section>

    <% } else { %>

        <% for (Order order : orders) {
            String status = order.getOrderStatus() == null
                    ? "UNKNOWN" : order.getOrderStatus().trim().toUpperCase();

            String statusClass = "status-default";

            if ("PLACED".equals(status)) {
                statusClass = "status-placed";
            } else if ("PROCESSING".equals(status)) {
                statusClass = "status-processing";
            } else if ("SHIPPED".equals(status)) {
                statusClass = "status-shipped";
            } else if ("DELIVERED".equals(status)) {
                statusClass = "status-delivered";
            } else if ("CANCELLED".equals(status)) {
                statusClass = "status-cancelled";
            }
        %>

            <section class="order-card">

                <div class="order-top">
                    <div>
                        <div class="order-number">
                            Order #<%=order.getOrderId()%>
                        </div>

                        <div class="order-date">
                            Placed on:
                            <%=order.getOrderDate() != null
                                ? dateFormat.format(order.getOrderDate())
                                : "Date not available"%>
                        </div>
                    </div>

                    <span class="status-badge <%=statusClass%>">
                        <%=status%>
                    </span>
                </div>

                <div class="order-info">
                    <div>
                        <div class="info-label">Order ID</div>
                        <div class="info-value">#<%=order.getOrderId()%></div>
                    </div>

                    <div>
                        <div class="info-label">Order Status</div>
                        <div class="info-value"><%=status%></div>
                    </div>
                </div>

                <div class="order-bottom">
                    <div>
                        <div class="total-label">Total Amount</div>
                        <div class="total-amount">
                            ₹<%=order.getTotalAmount()%>
                        </div>
                    </div>

                    <a class="details-btn"
                       href="<%=ctx%>/order-details?id=<%=order.getOrderId()%>">
                        View Order Details
                    </a>
                </div>

            </section>

        <% } %>

    <% } %>

</main>

</body>
</html>
