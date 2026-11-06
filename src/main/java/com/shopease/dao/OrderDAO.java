package com.shopease.dao;

import java.util.List;
import com.shopease.model.Order;

public interface OrderDAO {

    boolean addOrder(Order order);

    Order getOrderById(int orderId);

    List<Order> getAllOrders();

    List<Order> getOrdersByUserId(int userId);

    List<Order> getOrdersByStatus(String orderStatus);

    boolean updateOrder(Order order);

    boolean updateOrderStatus(int orderId, String orderStatus);

    boolean updatePaymentStatus(int orderId, String paymentStatus);

    boolean deleteOrder(int orderId);

    int getOrderCountByUserId(int userId);
    
    int getOrderCount();
}