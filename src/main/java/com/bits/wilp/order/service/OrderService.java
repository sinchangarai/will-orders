package com.bits.wilp.order.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bits.wilp.order.model.Order;

public interface OrderService {
    public Order placeOrder(Order order, HttpServletRequest request);
    public Order deliverOrder(String orderId);
    public Order cancelOrder(String orderId);

    public List<Order> getAllOrdersByUser(String userId);
    public Order getSingleOrder(String id);
    public List<Order> getAllOrders();

}
