package com.shopease.model;

import java.sql.Timestamp;

public class Payment {

    private int paymentId;
    private int orderId;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionReference;
    private Timestamp paymentDate;

    // Default Constructor
    public Payment() {
    }

    // Parameterized Constructor
    public Payment(int paymentId, int orderId, String paymentMethod,
                   String paymentStatus, String transactionReference,
                   Timestamp paymentDate) {

        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionReference = transactionReference;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }
}