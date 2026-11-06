<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Categories - ShopEase</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body>

<div class="admin-layout">

    <%@ include file="sidebar.jsp" %>

    <main class="admin-content">

        <h1>Manage Categories</h1>

        <c:if test="${param.success == 'completed'}">
            <div class="success-message">
                Category saved successfully!
            </div>
        </c:if>

        <c:if test="${param.error == 'operationfailed'}">
            <div class="error-message">
                Category operation failed. Check for duplicate names or database errors.
            </div>
        </c:if>

        <c:if test="${param.error == 'server'}">
            <div class="error-message">
                Something went wrong. Please check the Eclipse Console.
            </div>
        </c:if>

        <div class="admin-form-container">

            <h2>
                <c:choose>
                    <c:when test="${not empty editCategory}">
                        Edit Category
                    </c:when>
                    <c:otherwise>
                        Add New Category
                    </c:otherwise>
                </c:choose>
            </h2>

            <form action="${pageContext.request.contextPath}/admin/categories"
                  method="post">

                <!-- CSRF Protection -->
                <input type="hidden"
                       name="adminCsrfToken"
                       value="${adminCsrfToken}">

                <c:choose>
                    <c:when test="${not empty editCategory}">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="categoryId"
                               value="${editCategory.categoryId}">
                    </c:when>

                    <c:otherwise>
                        <input type="hidden" name="action" value="add">
                    </c:otherwise>
                </c:choose>

                <label>Category Name</label>

                <input type="text" name="categoryName"
                       value="${editCategory.categoryName}"
                       required>

                <label>Description</label>

                <textarea name="description"
                          rows="3">${editCategory.description}</textarea>

                <button type="submit" class="primary-btn">
                    Save Category
                </button>

                <c:if test="${not empty editCategory}">
                    <a href="${pageContext.request.contextPath}/admin/categories">
                        Cancel
                    </a>
                </c:if>

            </form>

        </div>

        <br>

        <h2>All Categories</h2>

        <div class="table-container">

            <table>

                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Category Name</th>
                        <th>Description</th>
                        <th>Actions</th>
                    </tr>
                </thead>

                <tbody>

                    <c:forEach var="category" items="${categories}">

                        <tr>
                            <td>${category.categoryId}</td>
                            <td>${category.categoryName}</td>
                            <td>${category.description}</td>

                            <td>

                                <a class="secondary-btn"
                                   href="${pageContext.request.contextPath}/admin/categories?action=edit&id=${category.categoryId}">
                                    Edit
                                </a>

                                <form action="${pageContext.request.contextPath}/admin/categories"
                                      method="post"
                                      style="display:inline;">

                                    <!-- CSRF Protection -->
                                    <input type="hidden"
                                           name="adminCsrfToken"
                                           value="${adminCsrfToken}">

                                    <input type="hidden" name="action" value="delete">

                                    <input type="hidden"
                                           name="categoryId"
                                           value="${category.categoryId}">

                                    <button type="submit"
                                            class="logout-btn"
                                            onclick="return confirm('Delete this category?')">
                                        Delete
                                    </button>

                                </form>

                            </td>
                        </tr>

                    </c:forEach>

                    <c:if test="${empty categories}">
                        <tr>
                            <td colspan="4">No categories available.</td>
                        </tr>
                    </c:if>

                </tbody>

            </table>

        </div>

    </main>

</div>

</body>
</html>