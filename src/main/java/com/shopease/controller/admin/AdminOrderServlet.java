package com.shopease.controller.admin;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.shopease.model.Order;
import com.shopease.model.Payment;
import com.shopease.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderService orderService;

    private static final List<String> ORDER_STATUSES =
            Arrays.asList(
                "PLACED",
                "PROCESSING",
                "SHIPPED",
                "DELIVERED",
                "CANCELLED"
            );

    private static final List<String> PAYMENT_STATUSES =
            Arrays.asList(
                "PENDING",
                "SUCCESS",
                "FAILED"
            );

    @Override
    public void init() {
        orderService = new OrderService();
    }

    private boolean isAdminLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null
                && session.getAttribute("loggedInAdmin") != null;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(
                request.getContextPath() + "/admin/login"
            );
            return;
        }

        String action = request.getParameter("action");

        try {

            if ("details".equals(action)) {

                int orderId = Integer.parseInt(
                    request.getParameter("id")
                );

                Order order = orderService.getOrderById(orderId);

                if (order == null) {
                    response.sendRedirect(
                        request.getContextPath()
                        + "/admin/orders?error=notfound"
                    );
                    return;
                }

                Payment payment =
                    orderService.getPaymentByOrderId(orderId);

                request.setAttribute("order", order);
                request.setAttribute("payment", payment);
                request.setAttribute(
                    "orderStatuses", ORDER_STATUSES
                );
                request.setAttribute(
                    "paymentStatuses", PAYMENT_STATUSES
                );

                request.getRequestDispatcher(
                    "/admin/order-details.jsp"
                ).forward(request, response);

                return;
            }

            List<Order> orders = orderService.getAllOrders();

            request.setAttribute("orders", orders);

            request.getRequestDispatcher(
                "/admin/orders.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                request.getContextPath()
                + "/admin/orders?error=invalidid"
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(
                request.getContextPath() + "/admin/login"
            );
            return;
        }

        String action = request.getParameter("action");

        try {

            int orderId = Integer.parseInt(
                request.getParameter("orderId")
            );

            boolean success = false;

            if ("updateOrderStatus".equals(action)) {

                String status =
                    request.getParameter("orderStatus");

                if (!ORDER_STATUSES.contains(status)) {
                    response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST
                    );
                    return;
                }

                success = orderService.updateOrderStatus(
                    orderId, status
                );

            } else if ("updatePaymentStatus".equals(action)) {

                String status =
                    request.getParameter("paymentStatus");

                if (!PAYMENT_STATUSES.contains(status)) {
                    response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST
                    );
                    return;
                }

                success = orderService.updatePaymentStatus(
                    orderId, status
                );

            }

            response.sendRedirect(
                request.getContextPath()
                + "/admin/orders?action=details&id="
                + orderId
                + (success ? "&success=updated" : "&error=updatefailed")
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                request.getContextPath()
                + "/admin/orders?error=server"
            );
        }
    }
}