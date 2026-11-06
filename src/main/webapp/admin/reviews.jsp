<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ShopEase | Reviews</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/admin.css">

<style>
.review-comment {
    max-width: 250px;
    white-space: normal;
    line-height: 1.5;
}

.rating {
    color: #f59e0b;
    font-weight: 700;
}

.delete-btn {
    background: #fee2e2;
    color: #dc2626;
    border: none;
    padding: 8px 13px;
    border-radius: 6px;
    cursor: pointer;
    font-weight: 600;
}

.delete-btn:hover {
    background: #fecaca;
}

.message {
    padding: 12px;
    margin-bottom: 18px;
    border-radius: 6px;
}

.success {
    background: #dcfce7;
    color: #166534;
}

.error {
    background: #fee2e2;
    color: #991b1b;
}
</style>
</head>

<body>

<div class="admin-layout">

    <%@ include file="sidebar.jsp" %>

    <main class="main-content">

        <div class="page-header">
            <div>
                <h1>Customer Reviews</h1>
                <p>View and manage customer product reviews.</p>
            </div>
        </div>

        <c:if test="${param.success == 'deleted'}">
            <div class="message success">
                Review deleted successfully.
            </div>
        </c:if>

        <c:if test="${not empty param.error}">
            <div class="message error">
                Unable to delete review. Please try again.
            </div>
        </c:if>

        <div class="table-container">

            <table class="data-table">

                <thead>
                    <tr>
                        <th>Review ID</th>
                        <th>Customer</th>
                        <th>Email</th>
                        <th>Product</th>
                        <th>Rating</th>
                        <th>Title</th>
                        <th>Comment</th>
                        <th>Date</th>
                        <th>Action</th>
                    </tr>
                </thead>

                <tbody>

                    <c:choose>

                        <c:when test="${not empty reviews}">

                            <c:forEach var="review" items="${reviews}">

                                <tr>
                                    <td>#${review.reviewId}</td>

                                    <td>
                                        <c:out value="${review.customerName}"/>
                                    </td>

                                    <td>
                                        <c:out value="${review.email}"/>
                                    </td>

                                    <td>
                                        <c:out value="${review.productName}"/>
                                    </td>

                                    <td class="rating">
                                        ${review.rating}/5 ★
                                    </td>

                                    <td>
                                        <c:out value="${review.title}"/>
                                    </td>

                                    <td class="review-comment">
                                        <c:out value="${review.comment}"/>
                                    </td>

                                    <td>${review.createdAt}</td>

                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/reviews"
                                              method="post"
                                              onsubmit="return confirm('Are you sure you want to delete this review?');">

                                            <!-- CSRF Protection -->
                                            <input type="hidden"
                                                   name="adminCsrfToken"
                                                   value="${adminCsrfToken}">

                                            <input type="hidden"
                                                   name="reviewId"
                                                   value="${review.reviewId}">

                                            <button type="submit"
                                                    class="delete-btn">
                                                Delete
                                            </button>

                                        </form>
                                    </td>
                                </tr>

                            </c:forEach>

                        </c:when>

                        <c:otherwise>

                            <tr>
                                <td colspan="9" style="text-align:center;">
                                    No customer reviews found.
                                </td>
                            </tr>

                        </c:otherwise>

                    </c:choose>

                </tbody>

            </table>

        </div>

    </main>

</div>

</body>
</html>