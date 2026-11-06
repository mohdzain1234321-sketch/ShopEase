<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>

    <title>Inventory Management | ShopEase</title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin.css">

    <style>
        .stock-low {
            color: #e67e22;
            font-weight: bold;
        }

        .stock-out {
            color: #e74c3c;
            font-weight: bold;
        }

        .stock-available {
            color: #27ae60;
            font-weight: bold;
        }

        .stock-input {
            width: 75px;
            padding: 8px;
        }
    </style>

</head>

<body>

<div class="admin-layout">

    <%@ include file="sidebar.jsp" %>

    <main class="admin-content">

        <h1>Inventory Management</h1>

        <c:if test="${param.success == 'updated'}">
            <div class="success-message">
                Stock updated successfully!
            </div>
        </c:if>

        <c:if test="${not empty param.error}">
            <div class="error-message">
                Unable to update stock. Please check the product and try again.
            </div>
        </c:if>

        <div class="table-container">

            <table>

                <thead>
                    <tr>
                        <th>Product ID</th>
                        <th>Product Name</th>
                        <th>Price</th>
                        <th>Available Stock</th>
                        <th>Stock Status</th>
                        <th>Update Stock</th>
                    </tr>
                </thead>

                <tbody>

                    <c:forEach var="product" items="${products}">

                        <tr>

                            <td>${product.productId}</td>

                            <td>${product.productName}</td>

                            <td>₹${product.price}</td>

                            <td>${product.stock}</td>

                            <td>

                                <c:choose>

                                    <c:when test="${product.stock == 0}">
                                        <span class="stock-out">
                                            Out of Stock
                                        </span>
                                    </c:when>

                                    <c:when test="${product.stock <= 5}">
                                        <span class="stock-low">
                                            Low Stock
                                        </span>
                                    </c:when>

                                    <c:otherwise>
                                        <span class="stock-available">
                                            Available
                                        </span>
                                    </c:otherwise>

                                </c:choose>

                            </td>

                            <td>

                                <form action="${pageContext.request.contextPath}/admin/inventory"
                                      method="post">

                                    <!-- CSRF Protection -->
                                    <input type="hidden"
                                           name="adminCsrfToken"
                                           value="${adminCsrfToken}">

                                    <input type="hidden"
                                           name="productId"
                                           value="${product.productId}">

                                    <input type="number"
                                           name="stock"
                                           class="stock-input"
                                           min="0"
                                           value="${product.stock}"
                                           required>

                                    <button type="submit"
                                            class="primary-btn">
                                        Update
                                    </button>

                                </form>

                            </td>

                        </tr>

                    </c:forEach>

                    <c:if test="${empty products}">
                        <tr>
                            <td colspan="6">
                                No products available.
                            </td>
                        </tr>
                    </c:if>

                </tbody>

            </table>

        </div>

    </main>

</div>

</body>
</html>