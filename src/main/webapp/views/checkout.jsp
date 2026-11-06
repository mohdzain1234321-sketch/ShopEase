<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*,java.math.BigDecimal,com.shopease.model.*"%>
<%
String ctx = request.getContextPath();
List<Map<String, Object>> rows = (List<Map<String, Object>>) request.getAttribute("checkoutItems");
List<Address> addresses = (List<Address>) request.getAttribute("addresses");
BigDecimal subtotal = (BigDecimal) request.getAttribute("subtotal"),
		discount = (BigDecimal) request.getAttribute("discountTotal"),
		total = (BigDecimal) request.getAttribute("grandTotal");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>Checkout | ShopEase</title>
<link rel="stylesheet" href="<%=ctx%>/css/checkout.css">
</head>
<body>
	<header class="top">
		<a class="brand" href="<%=ctx%>/home">ShopEase</a>
		<nav>
			<a href="<%=ctx%>/home">Continue shopping</a><a href="<%=ctx%>/cart">Cart</a>
		</nav>
	</header>
	<main class="wrap">
		<div class="heading">
			<div>
				<span class="eyebrow">ALMOST YOURS</span>
				<h1>Checkout</h1>
				<p>Confirm your delivery details and review your order.</p>
			</div>
			<div class="step">
				1. Address <b>→</b> 2. Review <b>→</b> 3. Place order
			</div>
		</div>
		<%
		if ("order_failed".equals(request.getParameter("error"))) {
		%><div
			class="notice error">We couldn't place your order. Please
			verify your address, stock and cart, then try again.</div>
		<%
		}
		%>
		<div class="checkout-grid">
			<section class="panel">
				<div class="section-title">
					<h2>Delivery address</h2>
					<a href="<%=ctx%>/addresses">Manage addresses</a>
				</div>
				<%
				if (addresses == null || addresses.isEmpty()) {
				%><div class="empty">
					<p>You need to add a delivery address before placing an order.</p>
					<a class="btn primary" href="<%=ctx%>/addresses">Add delivery
						address</a>
				</div>
				<%
				} else {
				%><form id="placeOrderForm" method="post"
					action="<%=ctx%>/orders">
					<input type="hidden" name="action" value="placeOrder">
					<div class="address-options">
						<%
						for (Address a : addresses) {
						%><label class="address-option"><input
							type="radio" name="addressId" value="<%=a.getAddressId()%>"
							<%=a.isDefault() ? "checked" : ""%> required><span><strong><%=a.getFullName()%></strong>
								<em><%=a.getAddressType()%></em>
								<%
								if (a.isDefault()) {
								%><small class="default">Default
									address</small>
								<%
								}
								%><small><%=a.getAddressLine()%>, <%=a.getCity()%>, <%=a.getState()%>
									- <%=a.getPostalCode()%>, <%=a.getCountry()%></small><small>Phone:
									<%=a.getPhone()%></small></span></label>
						<%
						}
						%>
					</div>
					<div class="section-title payment-title">
    <h2>Payment method</h2>
</div>

<label class="payment-option">
    <input type="radio" name="paymentMethod"
           value="COD" checked required>
    <span>
        <strong>Cash on Delivery</strong>
        <small>Pay when your order arrives.</small>
    </span>
    <b>COD</b>
</label>

<label class="payment-option">
    <input type="radio" name="paymentMethod"
           value="UPI">
    <span>
        <strong>UPI Payment</strong>
        <small>Pay using a simulated UPI transaction.</small>
    </span>
    <b>UPI</b>
</label>

<label class="payment-option">
    <input type="radio" name="paymentMethod"
           value="CARD">
    <span>
        <strong>Debit / Credit Card</strong>
        <small>Simulated card payment.</small>
    </span>
    <b>Card</b>
</label>

<p class="hint">
    Online payments are simulated for this demonstration project.
</p>

<button class="btn primary place" type="submit">
    Continue · ₹<%=total%>
</button>
				</form>
				<%
				}
				%>
			</section>
			<aside class="panel summary">
				<h2>Order summary</h2>
				<%
				if (rows != null) {
					for (Map<String, Object> row : rows) {
						Product p = (Product) row.get("product");
						CartItem item = (CartItem) row.get("item");
				%><div
					class="summary-product">
					<img src="<%=ctx%>/<%=p.getImage()%>" alt="">
					<div>
						<strong><%=p.getProductName()%></strong><small>Qty: <%=item.getQuantity()%></small>
					</div>
					<b>₹<%=row.get("lineTotal")%></b>
				</div>
				<% }} %><div class="total-row">
					<span>Subtotal</span><b>₹<%=subtotal%></b>
				</div>
				<div class="total-row savings">
					<span>Discount</span><b>− ₹<%=discount%></b>
				</div>
				<div class="total-row">
					<span>Delivery</span><b>FREE</b>
				</div>
				<div class="grand">
					<span>Total payable</span><strong>₹<%=total%></strong>
				</div>
				<p class="secure">🔒 Your order details are processed securely.</p>
			</aside>
		</div>
	</main>
</body>
</html>
