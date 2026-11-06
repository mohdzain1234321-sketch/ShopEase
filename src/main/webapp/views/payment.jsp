<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.shopease.model.Order,com.shopease.model.Payment" %>
<%
    String ctx = request.getContextPath();
    Order order = (Order) request.getAttribute("order");
    Payment payment = (Payment) request.getAttribute("payment");

    if (order == null || payment == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Payment details are unavailable.");
        return;
    }

    String method = payment.getPaymentMethod() == null ? "" : payment.getPaymentMethod().trim().toUpperCase();
    String status = payment.getPaymentStatus() == null ? "PENDING" : payment.getPaymentStatus().trim().toUpperCase();
    boolean isUpi = "UPI".equals(method);
    boolean isCard = "CARD".equals(method);
    boolean isPending = "PENDING".equals(status);
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Payment | ShopEase</title>
<style>
*{box-sizing:border-box}body{margin:0;background:#f4f7fc;color:#17243a;font-family:Arial,Helvetica,sans-serif}.topbar{height:70px;background:#fff;border-bottom:1px solid #e7edf5;display:flex;align-items:center;justify-content:space-between;padding:0 7%;}.brand{font-size:25px;font-weight:800;color:#1769d2;text-decoration:none;letter-spacing:-.7px}.topbar a:last-child{color:#53647b;text-decoration:none;font-size:14px}.wrap{max-width:900px;margin:48px auto;padding:0 20px}.heading{text-align:center;margin-bottom:28px}.eyebrow{font-size:11px;font-weight:700;letter-spacing:2px;color:#2878df}.heading h1{margin:9px 0;font-size:32px}.heading p{margin:0;color:#718096;font-size:14px;line-height:1.6}.grid{display:grid;grid-template-columns:1.2fr .8fr;gap:22px}.panel{background:#fff;border:1px solid #e5ebf3;border-radius:15px;padding:26px;box-shadow:0 8px 25px rgba(30,60,100,.05)}.panel h2{font-size:18px;margin:0 0 20px}.method{display:flex;align-items:center;gap:13px;padding:15px;border:1px solid #dce6f2;border-radius:11px;background:#f8fbff}.method-icon{width:42px;height:42px;border-radius:11px;background:#e8f1ff;color:#1769d2;display:grid;place-items:center;font-weight:800}.method strong{display:block;font-size:15px}.method small{display:block;color:#7a8798;margin-top:5px;font-size:12px}.notice{margin-top:18px;padding:13px 14px;border-radius:9px;background:#fff8e8;color:#805b13;font-size:13px;line-height:1.55}.actions{display:grid;gap:11px;margin-top:22px}.btn{display:block;width:100%;padding:13px 16px;border:0;border-radius:9px;text-align:center;text-decoration:none;font-size:14px;font-weight:700;cursor:pointer}.success{background:#1769d2;color:#fff}.failure{background:#fff;color:#c0392b;border:1px solid #f0c7c3}.btn:hover{filter:brightness(.97)}.summary-row{display:flex;justify-content:space-between;gap:15px;padding:12px 0;border-bottom:1px solid #edf1f6;font-size:13px;color:#66758a}.summary-row strong{color:#24344c}.summary-row:last-child{border:0}.status{display:inline-block;padding:6px 10px;border-radius:20px;background:#fff2d8;color:#946200;font-size:11px;font-weight:800}.status.SUCCESS{background:#e7f8ee;color:#16834a}.status.FAILED{background:#fff0ef;color:#c0392b}.foot{text-align:center;color:#98a3b2;font-size:11px;margin-top:22px;line-height:1.6}@media(max-width:680px){.grid{grid-template-columns:1fr}.wrap{margin:28px auto}.topbar{padding:0 20px}.heading h1{font-size:27px}.panel{padding:21px}}
</style>
</head>
<body>
<header class="topbar"><a class="brand" href="<%=ctx%>/home">ShopEase</a><a href="<%=ctx%>/orders">My orders</a></header>
<main class="wrap">
    <div class="heading">
        <span class="eyebrow">SECURE CHECKOUT DEMO</span>
        <h1>Complete your payment</h1>
        <p>Your order has been created. Choose a simulated result below to complete this interview-project payment flow.</p>
    </div>
    <div class="grid">
        <section class="panel">
            <h2>Payment method</h2>
            <div class="method">
                <div class="method-icon"><%=isUpi ? "UPI" : "CARD"%></div>
                <div>
                    <strong><%=isUpi ? "UPI Payment" : "Debit / Credit Card"%></strong>
                    <small>Order #<%=order.getOrderId()%></small>
                </div>
            </div>
            <% if (isPending) { %>
                <div class="notice"><strong>Demo payment:</strong> No real money will be charged. Do not enter actual UPI PIN, card number, CVV or banking credentials. Select a result to simulate the transaction.</div>
                <form class="actions" method="post" action="<%=ctx%>/payment">
                    <input type="hidden" name="action" value="simulatePayment">
                    <input type="hidden" name="orderId" value="<%=order.getOrderId()%>">
                    <button class="btn success" type="submit" name="paymentResult" value="SUCCESS">Simulate successful payment</button>
                    <button class="btn failure" type="submit" name="paymentResult" value="FAILED">Simulate failed payment</button>
                </form>
            <% } else { %>
                <div class="notice">This payment has already been processed. You cannot submit another simulated result for this order.</div>
                <a class="btn success" style="margin-top:20px" href="<%=ctx%>/payment?orderId=<%=order.getOrderId()%>">View payment result</a>
            <% } %>
        </section>
        <aside class="panel">
            <h2>Payment summary</h2>
            <div class="summary-row"><span>Order ID</span><strong>#<%=order.getOrderId()%></strong></div>
            <div class="summary-row"><span>Payment method</span><strong><%=method%></strong></div>
            <div class="summary-row"><span>Payment status</span><span class="status <%=status%>"><%=status%></span></div>
            <% if (payment.getTransactionReference() != null && !payment.getTransactionReference().trim().isEmpty()) { %>
                <div class="summary-row"><span>Transaction reference</span><strong><%=payment.getTransactionReference()%></strong></div>
            <% } %>
        </aside>
    </div>
    <p class="foot">ShopEase interview project · Simulated payment only<br>Payments are not processed by a real bank or payment gateway.</p>
</main>
</body>
</html>
