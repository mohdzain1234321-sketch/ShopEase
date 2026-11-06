package com.shopease.controller;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.shopease.model.Order;
import com.shopease.model.Payment;
import com.shopease.service.OrderService;
import com.shopease.service.PaymentService;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private PaymentService paymentService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        paymentService = new PaymentService();
        orderService = new OrderService();
    }

    // Display the payment page or the final order confirmation.
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/views/login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            Order order = orderService.getOrderById(orderId);
            if (order == null || order.getUserId() != userId) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found.");
                return;
            }

            Payment payment = paymentService.getPaymentByOrderId(orderId);
            if (payment == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Payment record not found.");
                return;
            }

            request.setAttribute("order", order);
            request.setAttribute("payment", payment);

            String method = payment.getPaymentMethod() == null
                    ? "" : payment.getPaymentMethod().trim().toUpperCase();
            String status = payment.getPaymentStatus() == null
                    ? "" : payment.getPaymentStatus().trim().toUpperCase();

            // COD does not require an online payment screen.
            if ("COD".equals(method) || "SUCCESS".equals(status)
                    || "FAILED".equals(status)) {
                request.getRequestDispatcher("/views/success.jsp").forward(request, response);
            } else if ("UPI".equals(method) || "CARD".equals(method)) {
                request.getRequestDispatcher("/views/payment.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported payment method.");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID.");
        }
    }

    // Complete a simulated UPI/Card payment for this interview project.
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/views/login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");

        if (!"simulatePayment".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid payment action.");
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            Order order = orderService.getOrderById(orderId);
            if (order == null || order.getUserId() != userId) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found.");
                return;
            }

            Payment payment = paymentService.getPaymentByOrderId(orderId);
            if (payment == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Payment record not found.");
                return;
            }

            String method = payment.getPaymentMethod() == null
                    ? "" : payment.getPaymentMethod().trim().toUpperCase();
            String currentStatus = payment.getPaymentStatus() == null
                    ? "" : payment.getPaymentStatus().trim().toUpperCase();

            if (!"UPI".equals(method) && !"CARD".equals(method)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Only UPI and Card payments can use this simulation.");
                return;
            }

            if (!"PENDING".equals(currentStatus)) {
                response.sendRedirect(request.getContextPath() + "/payment?orderId=" + orderId);
                return;
            }

            String result = request.getParameter("paymentResult");
            String newStatus;
            String transactionReference = null;

            if ("SUCCESS".equals(result)) {
                newStatus = "SUCCESS";
                transactionReference = "SIM-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
            } else if ("FAILED".equals(result)) {
                newStatus = "FAILED";
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid simulated payment result.");
                return;
            }

            String oldStatus = payment.getPaymentStatus();
            String oldReference = payment.getTransactionReference();

            payment.setPaymentStatus(newStatus);
            payment.setTransactionReference(transactionReference);

            // Update both payment and order records. Roll back the payment record
            // to PENDING if the order status update cannot be completed.
            boolean paymentUpdated = paymentService.updatePayment(payment);
            if (!paymentUpdated) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not update payment record.");
                return;
            }

            boolean orderUpdated = orderService.updatePaymentStatus(orderId, newStatus);
            if (!orderUpdated) {
                payment.setPaymentStatus(oldStatus);
                payment.setTransactionReference(oldReference);
                paymentService.updatePayment(payment);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not update order payment status. Please try again.");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/payment?orderId=" + orderId);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID.");
        }
    }
}
