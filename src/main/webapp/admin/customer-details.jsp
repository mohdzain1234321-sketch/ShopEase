<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Customer Details - ShopEase</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body>

<div class="admin-layout">

    <%@ include file="sidebar.jsp" %>

    <main class="admin-content">

        <h1>Customer Details</h1>

        <div class="admin-form-container">

            <p><strong>Customer ID:</strong> ${customer.userId}</p>

            <p><strong>Name:</strong> ${customer.name}</p>

            <p><strong>Email:</strong> ${customer.email}</p>

            <p><strong>Phone:</strong> ${customer.phone}</p>

            <br>

            <a href="${pageContext.request.contextPath}/admin/customers"
               class="secondary-btn">
                Back to Customers
            </a>

        </div>

    </main>

</div>

</body>
</html>