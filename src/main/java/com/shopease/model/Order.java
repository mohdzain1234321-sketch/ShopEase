package com.shopease.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {

    private int orderId;
    private int userId;
    private int addressId;
    private BigDecimal totalAmount;
    private String orderStatus;
    private String paymentStatus;
    private Timestamp orderDate;

    // Default Constructor
    public Order() {
    }

    // Parameterized Constructor
    public Order(int orderId, int userId, int addressId,
                 BigDecimal totalAmount, String orderStatus,
                 String paymentStatus, Timestamp orderDate) {

        this.orderId = orderId;
        this.userId = userId;
        this.addressId = addressId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.orderDate = orderDate;
    }

    // Getters and Setters

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
}