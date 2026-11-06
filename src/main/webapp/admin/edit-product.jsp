<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Product - ShopEase</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>

<div class="admin-content">

    <h1>Edit Product</h1>

    <div class="admin-form-container">

        <form action="${pageContext.request.contextPath}/admin/products"
              method="post">

            <!-- CSRF Protection -->
            <input type="hidden"
                   name="adminCsrfToken"
                   value="${adminCsrfToken}">

            <input type="hidden" name="action" value="update">

            <input type="hidden" name="productId"
                   value="${product.productId}">

            <label>Product Name</label>
            <input type="text" name="productName"
                   value="${product.productName}" required>

            <label>Category</label>
            <select name="categoryId" required>

                <c:forEach var="category" items="${categories}">
                    <option value="${category.categoryId}"
                        ${category.categoryId == product.categoryId
                          ? 'selected' : ''}>
                        ${category.categoryName}
                    </option>
                </c:forEach>

            </select>

            <label>Description</label>
            <textarea name="description"
                      rows="4">${product.description}</textarea>

            <label>Price</label>
            <input type="number" name="price"
                   value="${product.price}"
                   min="0" step="0.01" required>

            <label>Discount (%)</label>
            <input type="number" name="discount"
                   value="${product.discount}"
                   min="0" max="100" step="0.01">

            <label>Stock</label>
            <input type="number" name="stock"
                   value="${product.stock}"
                   min="0" required>

            <label>Image Path</label>
            <input type="text" name="image"
                   value="${product.image}">

            <button type="submit" class="primary-btn">
                Update Product
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