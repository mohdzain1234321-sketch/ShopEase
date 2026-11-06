package com.shopease.service;

import java.util.List;

import com.shopease.dao.PaymentDAO;
import com.shopease.daoimpl.PaymentDAOImpl;
import com.shopease.model.Payment;

public class PaymentService {

private PaymentDAO paymentDAO = new PaymentDAOImpl();

// 1. Add Payment
public boolean addPayment(Payment payment) {

    if (payment == null || payment.getOrderId() <= 0) {
        return false;
    }

    if (payment.getPaymentMethod() == null
            || payment.getPaymentMethod().trim().isEmpty()) {
        return false;
    }

    // Check whether a payment record already exists for this order
    if (paymentDAO.paymentExists(payment.getOrderId())) {
        return false;
    }

    return paymentDAO.addPayment(payment);
}

// 2. Get Payment By ID
public Payment getPaymentById(int paymentId) {

    if (paymentId <= 0) {
        return null;
    }

    return paymentDAO.getPaymentById(paymentId);
}

// 3. Get Payment By Order ID
public Payment getPaymentByOrderId(int orderId) {

    if (orderId <= 0) {
        return null;
    }

    return paymentDAO.getPaymentByOrderId(orderId);
}

// 4. Get All Payments
public List<Payment> getAllPayments() {

    return paymentDAO.getAllPayments();
}

// 5. Get Payments By Status
public List<Payment> getPaymentsByStatus(String paymentStatus) {

    if (paymentStatus == null || paymentStatus.trim().isEmpty()) {
        return null;
    }

    return paymentDAO.getPaymentsByStatus(paymentStatus.trim());
}

// 6. Update Payment
public boolean updatePayment(Payment payment) {

    if (payment == null
            || payment.getPaymentId() <= 0
            || payment.getOrderId() <= 0) {
        return false;
    }

    if (payment.getPaymentMethod() == null
            || payment.getPaymentMethod().trim().isEmpty()) {
        return false;
    }

    Payment existingPayment =
            paymentDAO.getPaymentById(payment.getPaymentId());

    if (existingPayment == null
            || existingPayment.getOrderId() != payment.getOrderId()) {
        return false;
    }

    return paymentDAO.updatePayment(payment);
}

// 7. Update Payment Status
public boolean updatePaymentStatus(int paymentId, String status) {

    if (paymentId <= 0 || status == null
            || status.trim().isEmpty()) {
        return false;
    }

    Payment payment = paymentDAO.getPaymentById(paymentId);

    if (payment == null) {
        return false;
    }

    payment.setPaymentStatus(status.trim());

    return paymentDAO.updatePayment(payment);
}

// 8. Delete Payment
public boolean deletePayment(int paymentId) {

    if (paymentId <= 0) {
        return false;
    }

    return paymentDAO.deletePayment(paymentId);
}

// 9. Check Payment Exists
public boolean paymentExists(int orderId) {

    if (orderId <= 0) {
        return false;
    }

    return paymentDAO.paymentExists(orderId);
}

}
