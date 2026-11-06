<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*,com.shopease.model.Address"%>
<%
String ctx = request.getContextPath();
List<Address> addresses = (List<Address>) request.getAttribute("addresses");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>My Addresses | ShopEase</title>
<link rel="stylesheet" href="<%=ctx%>/css/checkout.css">
</head>
<body>
	<header class="top">
		<a class="brand" href="<%=ctx%>/home">ShopEase</a>
		<nav>
			<a href="<%=ctx%>/home">Home</a><a href="<%=ctx%>/cart">Cart</a>
		</nav>
	</header>
	<main class="wrap">
		<div class="heading">
			<div>
				<span class="eyebrow">YOUR ACCOUNT</span>
				<h1>My delivery addresses</h1>
				<p>Save and manage the places where you receive orders.</p>
			</div>
			<a class="btn secondary" href="<%=ctx%>/checkout">Back to
				checkout</a>
		</div>
		<%
		if ("success".equals(request.getParameter("message"))) {
		%><div
			class="notice">Address updated successfully.</div>
		<%
		} else if ("error".equals(request.getParameter("message"))) {
		%><div
			class="notice error">We couldn't save that address. Check the
			details and try again.</div>
		<%
		}
		%>
		<div class="grid">
			<section class="panel">
				<h2>Add a new address</h2>
				<form method="post" action="<%=ctx%>/addresses" class="form-grid">
					<input type="hidden" name="action" value="add"><label>Address
						type<select name="addressType"><option>Home</option>
							<option>Work</option>
							<option>Other</option></select>
					</label><label>Full name<input name="fullName" required
						maxlength="100"></label><label>Phone<input name="phone"
						required pattern="[0-9]{10}" maxlength="10"></label><label
						class="wide">Address<input name="addressLine" required
						maxlength="255"></label><label>City<input name="city"
						required></label><label>State<input name="state" required></label><label>PIN
						code<input name="postalCode" required pattern="[0-9]{6}"
						maxlength="6">
					</label><label>Country<input name="country" value="India" required></label>
					<button class="btn primary wide" type="submit">Save
						address</button>
				</form>
			</section>
			<section class="panel">
				<h2>Saved addresses</h2>
				<%
				if (addresses == null || addresses.isEmpty()) {
				%><div class="empty">No
					saved addresses yet. Add one to continue checkout.</div>
				<%
				} else {
				for (Address a : addresses) {
				%><article class="address-card">
					<div class="address-head">
						<strong><%=a.getFullName()%></strong><span class="pill"><%=a.getAddressType()%></span>
						<%
						if (a.isDefault()) {
						%><span class="default">Default</span>
						<%
						}
						%>
					</div>
					<p><%=a.getAddressLine()%>,
						<%=a.getCity()%>,
						<%=a.getState()%>
						-
						<%=a.getPostalCode()%>,
						<%=a.getCountry()%></p>
					<p>
						Phone:
						<%=a.getPhone()%></p>
					<div class="actions">
						<%
						if (!a.isDefault()) {
						%><form method="post"
							action="<%=ctx%>/addresses">
							<input type="hidden" name="action" value="default"><input
								type="hidden" name="addressId" value="<%=a.getAddressId()%>">
							<button class="text-btn" type="submit">Set as default</button>
						</form>
						<% } %><form method="post" action="<%=ctx%>/addresses"
							onsubmit="return confirm('Delete this address?')">
							<input type="hidden" name="action" value="delete"><input
								type="hidden" name="addressId" value="<%=a.getAddressId()%>">
							<button class="text-btn danger" type="submit">Delete</button>
						</form>
					</div>
				</article>
				<% }} %>
			</section>
		</div>
	</main>
</body>
</html>
