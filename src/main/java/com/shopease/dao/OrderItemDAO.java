package com.shopease.dao;

import java.util.List;
import java.math.BigDecimal;

import com.shopease.model.OrderItem;

public interface OrderItemDAO {

    boolean addOrderItem(OrderItem orderItem);

    OrderItem getOrderItemById(int orderItemId);

    List<OrderItem> getAllOrderItems();

    List<OrderItem> getOrderItemsByOrderId(int orderId);

    List<OrderItem> getOrderItemsByProductId(int productId);

    boolean updateOrderItem(OrderItem orderItem);

    boolean deleteOrderItem(int orderItemId);

    boolean deleteOrderItemsByOrderId(int orderId);

    BigDecimal getOrderTotal(int orderId);

    int getTotalQuantityByOrderId(int orderId);
}