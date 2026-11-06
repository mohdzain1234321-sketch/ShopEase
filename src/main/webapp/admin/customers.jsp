<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Customer Management | ShopEase</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body>

<div class="admin-layout">

    <%@ include file="sidebar.jsp" %>

    <main class="admin-content">

        <h1>Customer Management</h1>

        <div class="table-container">

            <table>

                <thead>
                    <tr>
                        <th>Customer ID</th>
                        <th>Customer Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Registered At</th>
                    </tr>
                </thead>

                <tbody>

                    <c:forEach var="customer" items="${customers}">

                        <tr>
                            <td>${customer.userId}</td>
                            <td>${customer.name}</td>
                            <td>${customer.email}</td>
                            <td>${customer.phone}</td>
                            <td>${customer.createdAt}</td>
                        </tr>

                    </c:forEach>

                    <c:if test="${empty customers}">
                        <tr>
                            <td colspan="5">No customers found.</td>
                        </tr>
                    </c:if>

                </tbody>

            </table>

        </div>

    </main>

</div>

</body>
</html>