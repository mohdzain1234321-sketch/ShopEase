<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shop Products | ShopEase</title>


<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/products.css">

</head>

<body>

<header class="shop-header">

<a class="shop-logo"
   href="${pageContext.request.contextPath}/home">
    ShopEase<span>.</span>
</a>

<nav>
    <a href="${pageContext.request.contextPath}/home">Home</a>
    <a href="${pageContext.request.contextPath}/wishlist">Wishlist</a>
    <a href="${pageContext.request.contextPath}/cart">Cart</a>
    <a href="${pageContext.request.contextPath}/account">My Account</a>
</nav>


</header>

<main class="shop-container">


<div class="shop-heading">
    <div>
        <p class="eyebrow">DISCOVER YOUR FAVORITES</p>
        <h1>Explore Products</h1>
        <p>Find the best products at amazing prices.</p>
    </div>
</div>

<div class="shop-layout">

    <!-- FILTER SIDEBAR -->

    <aside class="filter-sidebar">

        <h2>Filters</h2>

        <form action="${pageContext.request.contextPath}/products"
              method="get">

            <label for="keyword">Search Products</label>

            <input type="search"
                   id="keyword"
                   name="keyword"
                   value="${keyword}"
                   placeholder="Search products...">

            <label for="categoryId">Category</label>

            <select name="categoryId" id="categoryId">

                <option value="">All Categories</option>

                <option value="1" ${categoryId == '1' ? 'selected' : ''}>Electronics</option>
                <option value="2" ${categoryId == '2' ? 'selected' : ''}>Mobiles</option>
                <option value="3" ${categoryId == '3' ? 'selected' : ''}>Fashion</option>
                <option value="4" ${categoryId == '4' ? 'selected' : ''}>Footwear</option>
                <option value="5" ${categoryId == '5' ? 'selected' : ''}>Home Appliances</option>
                <option value="6" ${categoryId == '6' ? 'selected' : ''}>Beauty</option>
                <option value="7" ${categoryId == '7' ? 'selected' : ''}>Books</option>
                <option value="8" ${categoryId == '8' ? 'selected' : ''}>Accessories</option>

            </select>

            <label for="minPrice">Minimum Price</label>

            <input type="number"
                   id="minPrice"
                   name="minPrice"
                   min="0"
                   value="${minPrice}"
                   placeholder="₹ Min">

            <label for="maxPrice">Maximum Price</label>

            <input type="number"
                   id="maxPrice"
                   name="maxPrice"
                   min="0"
                   value="${maxPrice}"
                   placeholder="₹ Max">

            <label for="sort">Sort By</label>

            <select name="sort" id="sort">

                <option value="newest" ${sort == 'newest' ? 'selected' : ''}>
                    Newest First
                </option>

                <option value="priceLow" ${sort == 'priceLow' ? 'selected' : ''}>
                    Price: Low to High
                </option>

                <option value="priceHigh" ${sort == 'priceHigh' ? 'selected' : ''}>
                    Price: High to Low
                </option>

            </select>

            <label class="checkbox-label">

                <input type="checkbox"
                       name="discountOnly"
                       value="true"
                       ${discountOnly ? 'checked' : ''}>

                Discounted Products Only

            </label>

            <button type="submit" class="apply-btn">
                Apply Filters
            </button>

            <a class="clear-btn"
               href="${pageContext.request.contextPath}/products">
                Clear Filters
            </a>

        </form>

    </aside>


    <!-- PRODUCT GRID -->

    <section class="products-section">

        <div class="results-heading">

            <h2>Available Products</h2>

            <span>${totalProducts} products found</span>

        </div>


        <c:choose>

            <c:when test="${not empty products}">

                <div class="products-grid">

                    <c:forEach var="product" items="${products}">

                        <article class="product-card">

                            <div class="product-image">

                                <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}">

                                    <img src="${pageContext.request.contextPath}/${product.image}"
                                         alt="${product.productName}">

                                </a>

                                <c:if test="${product.discount > 0}">

                                    <span class="discount-tag">
                                        ${product.discount}% OFF
                                    </span>

                                </c:if>

                            </div>


                            <div class="product-info">

                                <h3>${product.productName}</h3>

                                <p class="product-description">
                                    ${product.description}
                                </p>

                                <div class="price-row">

                                    <strong>

                                        ₹<fmt:formatNumber
                                            value="${product.price * (100 - product.discount) / 100}"
                                            minFractionDigits="2"
                                            maxFractionDigits="2"/>

                                    </strong>

                                    <c:if test="${product.discount > 0}">

                                        <del>₹${product.price}</del>

                                    </c:if>

                                </div>

                                <p class="stock-info">
                                    ${product.stock} items available
                                </p>


                                <div class="product-actions">

                                    <a class="view-btn"
                                       href="${pageContext.request.contextPath}/product-details?id=${product.productId}">
                                        View Product
                                    </a>

                                    <form action="${pageContext.request.contextPath}/wishlist"
                                          method="post">

                                        <input type="hidden"
                                               name="action"
                                               value="add">

                                        <input type="hidden"
                                               name="productId"
                                               value="${product.productId}">

                                        <button type="submit"
                                                class="wishlist-btn"
                                                aria-label="Add to wishlist">
                                            ♡
                                        </button>

                                    </form>

                                </div>

                            </div>

                        </article>

                    </c:forEach>

                </div>


                <!-- PAGINATION -->

                <c:if test="${totalPages > 1}">

                    <nav class="pagination" aria-label="Product pages">

                        <!-- PREVIOUS BUTTON -->

                        <c:if test="${currentPage > 1}">

                            <c:url var="previousUrl" value="/products">

                                <c:param name="page" value="${currentPage - 1}"/>
                                <c:param name="keyword" value="${keyword}"/>
                                <c:param name="categoryId" value="${categoryId}"/>
                                <c:param name="minPrice" value="${minPrice}"/>
                                <c:param name="maxPrice" value="${maxPrice}"/>
                                <c:param name="sort" value="${sort}"/>

                                <c:if test="${discountOnly}">
                                    <c:param name="discountOnly" value="true"/>
                                </c:if>

                            </c:url>

                            <a href="${previousUrl}" class="page-btn">
                                &laquo; Previous
                            </a>

                        </c:if>


                        <!-- PAGE NUMBERS -->

                        <c:forEach var="pageNumber"
                                   begin="1"
                                   end="${totalPages}">

                            <c:url var="pageUrl" value="/products">

                                <c:param name="page" value="${pageNumber}"/>
                                <c:param name="keyword" value="${keyword}"/>
                                <c:param name="categoryId" value="${categoryId}"/>
                                <c:param name="minPrice" value="${minPrice}"/>
                                <c:param name="maxPrice" value="${maxPrice}"/>
                                <c:param name="sort" value="${sort}"/>

                                <c:if test="${discountOnly}">
                                    <c:param name="discountOnly" value="true"/>
                                </c:if>

                            </c:url>

                            <c:choose>

                                <c:when test="${pageNumber == currentPage}">

                                    <span class="page-btn active">
                                        ${pageNumber}
                                    </span>

                                </c:when>

                                <c:otherwise>

                                    <a href="${pageUrl}" class="page-btn">
                                        ${pageNumber}
                                    </a>

                                </c:otherwise>

                            </c:choose>

                        </c:forEach>


                        <!-- NEXT BUTTON -->

                        <c:if test="${currentPage < totalPages}">

                            <c:url var="nextUrl" value="/products">

                                <c:param name="page" value="${currentPage + 1}"/>
                                <c:param name="keyword" value="${keyword}"/>
                                <c:param name="categoryId" value="${categoryId}"/>
                                <c:param name="minPrice" value="${minPrice}"/>
                                <c:param name="maxPrice" value="${maxPrice}"/>
                                <c:param name="sort" value="${sort}"/>

                                <c:if test="${discountOnly}">
                                    <c:param name="discountOnly" value="true"/>
                                </c:if>

                            </c:url>

                            <a href="${nextUrl}" class="page-btn">
                                Next &raquo;
                            </a>

                        </c:if>

                    </nav>

                </c:if>

            </c:when>


            <c:otherwise>

                <div class="empty-products">

                    <h2>No products found</h2>

                    <p>Try changing your search or filters.</p>

                    <a href="${pageContext.request.contextPath}/products">
                        View All Products
                    </a>

                </div>

            </c:otherwise>

        </c:choose>

    </section>

</div>

</main>

</body>
</html>
