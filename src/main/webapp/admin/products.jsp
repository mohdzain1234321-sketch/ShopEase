<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<title>Manage Products | ShopEase</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body>

	<div class="admin-layout">

		<%@ include file="sidebar.jsp"%>

		<main class="admin-content">

			<header class="admin-header">

				<h1>Product Management</h1>

				<a
					href="${pageContext.request.contextPath}/admin/products?action=add"
					class="primary-btn"> + Add Product </a>

			</header>

			<c:if test="${param.success == 'added'}">
				<p class="success-message">Product added successfully!</p>
			</c:if>

			<c:if test="${param.success == 'updated'}">
				<p class="success-message">Product updated successfully!</p>
			</c:if>

			<c:if test="${param.success == 'deleted'}">
				<p class="success-message">Product deleted successfully!</p>
			</c:if>

			<c:if test="${not empty param.error}">
				<p class="error-message">Product operation failed. Please check
					the details and try again.</p>
			</c:if>

			<div class="table-container">

				<table>

					<thead>
						<tr>
							<th>Product ID</th>
							<th>Product Name</th>
							<th>Category ID</th>
							<th>Price</th>
							<th>Discount</th>
							<th>Stock</th>
							<th>Actions</th>
						</tr>
					</thead>

					<tbody>

						<c:forEach var="product" items="${products}">

							<tr>
								<td>${product.productId}</td>
								<td>${product.productName}</td>
								<td>${product.categoryId}</td>
								<td>₹${product.price}</td>
								<td>${product.discount}%</td>
								<td>${product.stock}</td>

								<td><a class="secondary-btn"
									href="${pageContext.request.contextPath}/admin/products?action=edit&id=${product.productId}">
										Edit </a>

									<form
										action="${pageContext.request.contextPath}/admin/products"
										method="post" style="display: inline;">

										<!-- CSRF Protection -->
										<input type="hidden" name="adminCsrfToken"
											value="${adminCsrfToken}"> <input type="hidden"
											name="action" value="delete"> <input type="hidden"
											name="productId" value="${product.productId}">

										<button type="submit" class="logout-btn"
											onclick="return confirm('Delete this product?')">
											Delete</button>

									</form></td>

							</tr>

						</c:forEach>

						<c:if test="${empty products}">
							<tr>
								<td colspan="7">No products found.</td>
							</tr>
						</c:if>

					</tbody>

				</table>

			</div>

		</main>

	</div>

</body>
</html>