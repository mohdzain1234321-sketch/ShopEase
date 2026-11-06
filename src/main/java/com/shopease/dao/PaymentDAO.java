package com.shopease.dao;

import java.util.List;
import com.shopease.model.Payment;

public interface PaymentDAO {

    boolean addPayment(Payment payment);

    Payment getPaymentById(int paymentId);

    Payment getPaymentByOrderId(int orderId);

    List<Payment> getAllPayments();

    List<Payment> getPaymentsByStatus(String paymentStatus);

    boolean updatePayment(Payment payment);

    boolean updatePaymentStatus(int paymentId, String paymentStatus);

    boolean deletePayment(int paymentId);

    boolean paymentExists(int orderId);
}