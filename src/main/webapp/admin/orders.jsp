<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Orders | ShopEase</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body>

<div class="admin-layout">

    <%@ include file="sidebar.jsp" %>

    <main class="admin-content">

        <h1>Order Management</h1>

        <c:if test="${param.error == 'server'}">
            <p class="error-message">
                Unable to load orders. Check the server console.
            </p>
        </c:if>

        <div class="table-container">

            <table>

                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Customer ID</th>
                        <th>Order Date</th>
                        <th>Total Amount</th>
                        <th>Order Status</th>
                        <th>Payment Status</th>
                        <th>Action</th>
                    </tr>
                </thead>

                <tbody>

                    <c:forEach var="order" items="${orders}">

                        <tr>
                            <td>${order.orderId}</td>
                            <td>${order.userId}</td>
                            <td>${order.orderDate}</td>
                            <td>₹${order.totalAmount}</td>
                            <td>${order.orderStatus}</td>
                            <td>${order.paymentStatus}</td>

                            <td>
                                <a class="secondary-btn"
                                   href="${pageContext.request.contextPath}/admin/orders?action=details&id=${order.orderId}">
                                    View Details
                                </a>
                            </td>

                        </tr>

                    </c:forEach>

                    <c:if test="${empty orders}">
                        <tr>
                            <td colspan="7">No orders found.</td>
                        </tr>
                    </c:if>

                </tbody>

            </table>

        </div>

    </main>

</div>

</body>
</html>