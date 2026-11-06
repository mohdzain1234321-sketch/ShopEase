<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Order Details | ShopEase</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body>

<div class="admin-layout">

    <%@ include file="sidebar.jsp" %>

    <main class="admin-content">

        <h1>Order Details</h1>

        <c:if test="${param.success == 'updated'}">
            <p class="success-message">
                Order information updated successfully!
            </p>
        </c:if>

        <c:if test="${not empty param.error}">
            <p class="error-message">
                Unable to update order information.
            </p>
        </c:if>

        <div class="table-container">

            <h2>Order Information</h2>

            <p><strong>Order ID:</strong> ${order.orderId}</p>
            <p><strong>Customer ID:</strong> ${order.userId}</p>
            <p><strong>Address ID:</strong> ${order.addressId}</p>
            <p><strong>Order Date:</strong> ${order.orderDate}</p>
            <p><strong>Total Amount:</strong> ₹${order.totalAmount}</p>
            <p><strong>Order Status:</strong> ${order.orderStatus}</p>
            <p><strong>Payment Status:</strong> ${order.paymentStatus}</p>

        </div>

        <div class="table-container">

            <h2>Update Order Status</h2>

            <form action="${pageContext.request.contextPath}/admin/orders"
                  method="post">

                <!-- CSRF Protection -->
                <input type="hidden"
                       name="adminCsrfToken"
                       value="${adminCsrfToken}">

                <input type="hidden" name="action" value="updateOrderStatus">

                <input type="hidden"
                       name="orderId"
                       value="${order.orderId}">

                <label>Order Status</label>

                <select name="orderStatus" required>

                    <c:forEach var="status" items="${orderStatuses}">
                        <option value="${status}"
                            ${order.orderStatus == status ? 'selected' : ''}>
                            ${status}
                        </option>
                    </c:forEach>

                </select>

                <button type="submit" class="primary-btn">
                    Update Order
                </button>

            </form>

        </div>

        <div class="table-container">

            <h2>Payment Information</h2>

            <c:choose>

                <c:when test="${not empty payment}">

                    <p><strong>Payment Method:</strong>
                        ${payment.paymentMethod}</p>

                    <p><strong>Payment Status:</strong>
                        ${payment.paymentStatus}</p>

                    <p><strong>Transaction Reference:</strong>
                        ${payment.transactionReference}</p>

                </c:when>

                <c:otherwise>
                    <p>No payment record found.</p>
                </c:otherwise>

            </c:choose>

            <h3>Update Payment Status</h3>

            <form action="${pageContext.request.contextPath}/admin/orders"
                  method="post">

                <!-- CSRF Protection -->
                <input type="hidden"
                       name="adminCsrfToken"
                       value="${adminCsrfToken}">

                <input type="hidden"
                       name="action"
                       value="updatePaymentStatus">

                <input type="hidden"
                       name="orderId"
                       value="${order.orderId}">

                <select name="paymentStatus" required>

                    <c:forEach var="status" items="${paymentStatuses}">
                        <option value="${status}"
                            ${order.paymentStatus == status ? 'selected' : ''}>
                            ${status}
                        </option>
                    </c:forEach>

                </select>

                <button type="submit" class="primary-btn">
                    Update Payment
                </button>

            </form>

        </div>

        <br>

        <a href="${pageContext.request.contextPath}/admin/orders"
           class="secondary-btn">
            Back to Orders
        </a>

    </main>

</div>

</body>
</html>