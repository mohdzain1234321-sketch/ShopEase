<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Shopping Cart | ShopEase</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">
</head>
<body>
<header class="cart-header">
  <a class="brand" href="${pageContext.request.contextPath}/home">Shop<span>Ease</span></a>
  <nav>
    <a href="${pageContext.request.contextPath}/home">Home</a>
    <a href="${pageContext.request.contextPath}/products">Shop</a>
    <a class="active" href="${pageContext.request.contextPath}/cart">Cart <span class="count"><c:out value="${cartItemCount}"/></span></a>
  </nav>
</header>

<main class="cart-container">
  <div class="page-heading">
    <div><p class="eyebrow">YOUR SHOPPING BAG</p><h1>Shopping Cart</h1>
      <p class="muted">Review your items before proceeding to checkout.</p></div>
    <a class="continue-link" href="${pageContext.request.contextPath}/products">← Continue shopping</a>
  </div>

  <c:if test="${param.message == 'success'}">
    <div class="notice success">Your cart has been updated.</div>
  </c:if>
  <c:if test="${param.message == 'error'}">
    <div class="notice error">We couldn't complete that cart action. Please check the available stock and try again.</div>
  </c:if>

  <c:choose>
    <c:when test="${empty cartDisplayItems}">
      <section class="empty-cart">
        <div class="empty-icon">🛍</div>
        <h2>Your cart is waiting</h2>
        <p class="muted">You haven't added any products yet. Explore our collection and find something you love.</p>
        <a class="primary-btn" href="${pageContext.request.contextPath}/products">Explore products</a>
      </section>
    </c:when>
    <c:otherwise>
      <div class="cart-layout">
        <section class="items-panel">
          <div class="items-heading"><h2>Your items <span>(${cartItemCount})</span></h2>
            <form action="${pageContext.request.contextPath}/cart" method="post" onsubmit="return confirm('Remove all items from your cart?');">
              <input type="hidden" name="action" value="clear">
              <button class="text-button danger-text" type="submit">Clear cart</button>
            </form>
          </div>

          <c:forEach var="row" items="${cartDisplayItems}">
            <c:set var="item" value="${row.cartItem}"/>
            <c:set var="product" value="${row.product}"/>
            <article class="cart-item">
              <a class="product-thumb" href="${pageContext.request.contextPath}/product-details?id=${product.productId}">
                <img src="${pageContext.request.contextPath}/${product.image}" alt="<c:out value='${product.productName}'/>"
                     onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/images/product-placeholder.png';">
              </a>
              <div class="item-info">
                <a class="product-name" href="${pageContext.request.contextPath}/product-details?id=${product.productId}"><c:out value="${product.productName}"/></a>
                <p class="stock-label"><c:choose><c:when test="${product.stock > 0}">In stock · ${product.stock} available</c:when><c:otherwise>Currently unavailable</c:otherwise></c:choose></p>
                <div class="mobile-price">₹<fmt:formatNumber value="${row.unitPrice}" minFractionDigits="2" maxFractionDigits="2"/></div>
                <div class="item-actions">
                  <form class="quantity-form" action="${pageContext.request.contextPath}/cart" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                    <button type="button" class="qty-btn" data-step="-1" aria-label="Decrease quantity">−</button>
                    <input class="qty-input" type="number" name="quantity" value="${item.quantity}" min="1" max="${product.stock}" aria-label="Quantity">
                    <button type="button" class="qty-btn" data-step="1" aria-label="Increase quantity">+</button>
                    <button class="update-link" type="submit">Update</button>
                  </form>
                  <form action="${pageContext.request.contextPath}/cart" method="post" onsubmit="return confirm('Remove this item?');">
                    <input type="hidden" name="action" value="remove">
                    <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                    <button class="text-button remove-button" type="submit">Remove</button>
                  </form>
                </div>
              </div>
              <div class="item-pricing">
                <strong>₹<fmt:formatNumber value="${row.lineTotal}" minFractionDigits="2" maxFractionDigits="2"/></strong>
                <del>₹<fmt:formatNumber value="${row.lineSubtotal}" minFractionDigits="2" maxFractionDigits="2"/></del>
                <small>You save ₹<fmt:formatNumber value="${row.lineSavings}" minFractionDigits="2" maxFractionDigits="2"/></small>
              </div>
            </article>
          </c:forEach>
        </section>

        <aside class="summary-card">
          <h2>Order summary</h2>
          <div class="summary-row"><span>Subtotal</span><span>₹<fmt:formatNumber value="${cartSubtotal}" minFractionDigits="2" maxFractionDigits="2"/></span></div>
          <div class="summary-row savings"><span>Product discounts</span><span>− ₹<fmt:formatNumber value="${cartSavings}" minFractionDigits="2" maxFractionDigits="2"/></span></div>
          <div class="summary-row"><span>Delivery</span><span class="free-label">Calculated at checkout</span></div>
          <div class="summary-divider"></div>
          <div class="summary-total"><span>Estimated total</span><strong>₹<fmt:formatNumber value="${cartTotal}" minFractionDigits="2" maxFractionDigits="2"/></strong></div>
          <p class="summary-note">Delivery charges and payment details will be confirmed during checkout.</p>
          <a class="primary-btn checkout-btn" href="${pageContext.request.contextPath}/checkout">Proceed to checkout</a>
          <div class="secure-note">🔒 Secure checkout</div>
        </aside>
      </div>
    </c:otherwise>
  </c:choose>
</main>
<script src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>
