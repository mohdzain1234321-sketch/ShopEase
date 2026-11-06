<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Details | ShopEase</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/common.css">

    <style>
        .details-container {
            max-width: 1200px;
            margin: 50px auto;
            padding: 20px;
        }

        .breadcrumb {
            color: #777;
            margin-bottom: 30px;
        }

        .breadcrumb a {
            color: #1769aa;
            text-decoration: none;
        }

        .product-details {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 60px;
            background: white;
            padding: 35px;
            border-radius: 14px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.06);
        }

        .product-image {
            background: #f7f9fc;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 420px;
            padding: 25px;
        }

        .product-image img {
            width: 100%;
            max-height: 380px;
            object-fit: contain;
        }

        .product-info h1 {
            font-size: 30px;
            margin-bottom: 15px;
            color: #172b4d;
        }

        .product-description {
            color: #666;
            line-height: 1.7;
            margin: 20px 0;
        }

        .price {
            font-size: 30px;
            font-weight: 800;
            color: #1769aa;
        }

        .original-price {
            color: #999;
            text-decoration: line-through;
            margin-left: 12px;
        }

        .discount-badge {
            display: inline-block;
            background: #e8f7ed;
            color: #168344;
            padding: 7px 12px;
            border-radius: 20px;
            margin: 15px 0;
            font-weight: 700;
        }

        .stock {
            margin: 18px 0;
            color: #168344;
            font-weight: 600;
        }

        .quantity {
            margin: 25px 0;
        }

        .quantity input {
            width: 75px;
            padding: 10px;
            margin-left: 10px;
            border: 1px solid #ddd;
            border-radius: 6px;
        }

        .action-buttons {
            display: flex;
            gap: 15px;
            margin-top: 25px;
        }

        .action-buttons button {
            padding: 14px 25px;
            border: none;
            border-radius: 7px;
            font-weight: 700;
            cursor: pointer;
        }

        .add-cart {
            background: #1769aa;
            color: white;
        }

        .buy-now {
            background: #ff9f1c;
            color: white;
        }

        .action-buttons button:hover {
            opacity: 0.88;
        }

        .reviews-shortcut a {
            color: #1769aa;
            text-decoration: none;
            font-weight: 600;
        }

        @media(max-width: 768px) {
            .product-details {
                grid-template-columns: 1fr;
                gap: 30px;
                padding: 20px;
            }

            .product-info h1 {
                font-size: 24px;
            }

            .action-buttons {
                flex-direction: column;
            }
        }
    </style>
</head>

<body>

<div class="details-container">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/home">Home</a>
        / Product Details
    </div>

    <c:choose>

        <c:when test="${not empty product}">

            <div class="product-details">

                <div class="product-image">
                    <img
                        src="${pageContext.request.contextPath}/${product.image}"
                        alt="${product.productName}">
                </div>

                <div class="product-info">

                    <h1>${product.productName}</h1>

                    <p class="product-description">
                        ${product.description}
                    </p>

                    <div class="price">

                        ₹<fmt:formatNumber
                            value="${product.price * (100 - product.discount) / 100}"
                            minFractionDigits="2"
                            maxFractionDigits="2"/>

                        <c:if test="${product.discount > 0}">
                            <span class="original-price">
                                ₹${product.price}
                            </span>
                        </c:if>

                    </div>

                    <c:if test="${product.discount > 0}">
                        <div class="discount-badge">
                            ${product.discount}% OFF
                        </div>
                    </c:if>

                    <p class="stock">
                        ${product.stock} items available
                    </p>

                    <div class="reviews-shortcut">
                        <a href="${pageContext.request.contextPath}/reviews?productId=${product.productId}">
                            ★ Read Reviews &amp; Ratings
                        </a>
                    </div>

                    <div class="quantity">

                        <label for="quantity">Quantity:</label>

                        <input
                            type="number"
                            id="quantity"
                            value="1"
                            min="1"
                            max="${product.stock}"
                            ${product.stock <= 0 ? 'disabled' : ''}>

                    </div>

                    <div class="action-buttons">

                        <!-- ADD TO CART -->

                        <form action="${pageContext.request.contextPath}/cart"
                              method="post"
                              id="addCartForm">

                            <input type="hidden"
                                   name="action"
                                   value="add">

                            <input type="hidden"
                                   name="productId"
                                   value="${product.productId}">

                            <input type="hidden"
                                   name="variantId"
                                   value="">

                            <input type="hidden"
                                   name="quantity"
                                   id="cartQuantity"
                                   value="1">

                            <button type="submit"
                                    class="add-cart"
                                    ${product.stock <= 0 ? 'disabled' : ''}>
                                Add to Cart
                            </button>

                        </form>

                        <!-- BUY NOW -->

                        <form action="${pageContext.request.contextPath}/checkout"
                              method="get"
                              id="buyNowForm">

                            <input type="hidden"
                                   name="productId"
                                   value="${product.productId}">

                            <input type="hidden"
                                   name="quantity"
                                   id="buyNowQuantity"
                                   value="1">

                            <button type="submit"
                                    class="buy-now"
                                    ${product.stock <= 0 ? 'disabled' : ''}>
                                Buy Now
                            </button>

                        </form>

                    </div>

                </div>

            </div>

            <!-- RELATED PRODUCTS -->

            <c:import url="/related-products">
                <c:param name="productId"
                         value="${product.productId}" />
            </c:import>

        </c:when>

        <c:otherwise>

            <h2>Product not found!</h2>

            <a href="${pageContext.request.contextPath}/products">
                Continue Shopping
            </a>

        </c:otherwise>

    </c:choose>

</div>

<script src="${pageContext.request.contextPath}/js/product-details.js"></script>

</body>
</html>