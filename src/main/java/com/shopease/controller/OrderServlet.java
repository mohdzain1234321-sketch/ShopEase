package com.shopease.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.shopease.model.Order;
import com.shopease.model.OrderItem;
import com.shopease.model.Payment;
import com.shopease.service.OrderService;

@WebServlet(urlPatterns = {"/orders", "/order-details"})
public class OrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
    }

    // Display order history and individual order details.
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
        String servletPath = request.getServletPath();

        if ("/order-details".equals(servletPath)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("id"));
                Order order = orderService.getOrderById(orderId);

                if (order == null || order.getUserId() != userId) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found.");
                    return;
                }

                List<OrderItem> orderItems = orderService.getOrderItems(orderId);
                Payment payment = orderService.getPaymentByOrderId(orderId);

                request.setAttribute("order", order);
                request.setAttribute("orderItems", orderItems);
                request.setAttribute("payment", payment);
                request.getRequestDispatcher("/views/order-details.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID.");
            }
            return;
        }

        List<Order> orders = orderService.getOrdersByUserId(userId);
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/views/my-orders.jsp").forward(request, response);
    }

    // Handle normal checkout and Buy Now.
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

        if (!"placeOrder".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order action.");
            return;
        }

        try {
            String addressValue = request.getParameter("addressId");
            String paymentMethod = request.getParameter("paymentMethod");

            if (addressValue == null || paymentMethod == null) {
                response.sendRedirect(request.getContextPath() + "/checkout?error=missing_information");
                return;
            }

            int addressId = Integer.parseInt(addressValue);
            paymentMethod = paymentMethod.trim().toUpperCase();

            if (!"COD".equals(paymentMethod)
                    && !"UPI".equals(paymentMethod)
                    && !"CARD".equals(paymentMethod)) {
                response.sendRedirect(request.getContextPath() + "/checkout?error=invalid_payment_method");
                return;
            }

            boolean buyNowMode = Boolean.TRUE.equals(session.getAttribute("buyNowMode"));
            int orderId;

            if (buyNowMode) {
                Integer productId = (Integer) session.getAttribute("buyNowProductId");
                Integer quantity = (Integer) session.getAttribute("buyNowQuantity");

                if (productId == null || quantity == null || productId <= 0 || quantity <= 0) {
                    response.sendRedirect(request.getContextPath() + "/checkout?error=invalid_product");
                    return;
                }

                orderId = orderService.placeSingleProductOrderAndGetId(
                    userId, addressId, paymentMethod, productId, quantity
                );

                if (orderId > 0) {
                    session.removeAttribute("buyNowMode");
                    session.removeAttribute("buyNowProductId");
                    session.removeAttribute("buyNowQuantity");
                }
            } else {
                orderId = orderService.placeOrderAndGetId(userId, addressId, paymentMethod);
            }

            if (orderId > 0) {
                // PaymentServlet will verify that this order belongs to the logged-in user.
                response.sendRedirect(request.getContextPath() + "/payment?orderId=" + orderId);
            } else {
                response.sendRedirect(request.getContextPath() + "/checkout?error=order_failed");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid checkout information.");
        }
    }
}
