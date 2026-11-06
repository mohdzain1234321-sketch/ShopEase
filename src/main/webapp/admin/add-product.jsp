<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Add Product - ShopEase</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>

<div class="admin-content">

    <h1>Add New Product</h1>

    <div class="admin-form-container">

        <form action="${pageContext.request.contextPath}/admin/products"
              method="post">

            <!-- CSRF Protection -->
            <input type="hidden"
                   name="adminCsrfToken"
                   value="${adminCsrfToken}">

            <input type="hidden" name="action" value="add">

            <label>Product Name</label>
            <input type="text" name="productName" required>

            <label>Category</label>
            <select name="categoryId" required>
                <option value="">Select Category</option>

                <c:forEach var="category" items="${categories}">
                    <option value="${category.categoryId}">
                        ${category.categoryName}
                    </option>
                </c:forEach>
            </select>

            <label>Description</label>
            <textarea name="description" rows="4"></textarea>

            <label>Price</label>
            <input type="number" name="price"
                   min="0" step="0.01" required>

            <label>Discount (%)</label>
            <input type="number" name="discount"
                   min="0" max="100" step="0.01" value="0">

            <label>Stock</label>
            <input type="number" name="stock"
                   min="0" required>

            <label>Image Path</label>
            <input type="text" name="image"
                   placeholder="images/product.png">

            <button type="submit" class="primary-btn">
                Add Product
            </button>

        </form>

        <br>

        <a href="${pageContext.request.contextPath}/admin/products">
            Back to Products
        </a>

    </div>
</div>

</body>
</html>