package com.bits.wilp.order.service;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bits.wilp.order.model.Order;
import com.bits.wilp.order.model.OrderConstants;
import com.bits.wilp.order.model.Product;
import com.bits.wilp.order.model.ProductQuantity;
import com.bits.wilp.order.repository.OrderRepository;
import com.bits.wilp.order.util.HttpUtil;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private HttpUtil httpUtil;

    @Override
    public Order placeOrder(Order order, HttpServletRequest request) {
        if(order.getUserId() == null || order.getProductQuantityList() == null || order.getProductQuantityList().isEmpty()
            || order.getDeliveryAddress() == null) {
            throw new IllegalArgumentException("Cannot place order with empty userid, product quanitity, payment or deliver address");
        }

        Double totalAmount = 0.0;
        for(ProductQuantity pq : order.getProductQuantityList())  {
            Product currentProduct = httpUtil.getProductDetails(pq.getProductId());
            if(currentProduct == null || currentProduct.getAvailableQuantity() < pq.getQuantity()) {
                throw new IllegalArgumentException("Unknown product or insufficient stock available for productid: " + pq.getProductId());
            }
            totalAmount += currentProduct.getPrice() * pq.getQuantity();
            // update the product catalog
            httpUtil.updateProductQuantity(pq.getProductId(), currentProduct.getAvailableQuantity() - pq.getQuantity(), request);
        }
        order.setAmount(totalAmount);
        order.setPaymentType(OrderConstants.PAYMENT_PREPAID);
        order.setOrderStatus(OrderConstants.ORDER_PLACED);

        Date currentTime = new Date(System.currentTimeMillis());
        order.setPlacedAt(currentTime);
        order.setUpdatedAt(currentTime);

        orderRepository.save(order);
        return order;
    }

   
    @Override
    public Order deliverOrder(String orderId) {
        Order order = getSingleOrder(orderId);
        if(OrderConstants.ORDER_CANCELLED.equals(order.getOrderStatus()))
            throw new IllegalArgumentException("Already cancelled Order cannot be delivered with orderid: " + orderId);

        order.setOrderStatus(OrderConstants.ORDER_DELIVERED);
        order.setUpdatedAt(new Date(System.currentTimeMillis()));
        orderRepository.save(order);

        return order;
    }


    @Override
    public Order cancelOrder(String orderId) {
        Order order = getSingleOrder(orderId);
        if(OrderConstants.ORDER_DELIVERED.equals(order.getOrderStatus()))
            throw new IllegalArgumentException("Already delivered Order cannot be cancelled with orderid: " + orderId);

        order.setOrderStatus(OrderConstants.ORDER_CANCELLED);
        order.setUpdatedAt(new Date(System.currentTimeMillis()));
        orderRepository.save(order);

        return order;
    }

    @Override
    public List<Order> getAllOrdersByUser(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllOrdersByUser'");
    }


    @Override
    public Order getSingleOrder(String id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isPresent()) {
            return orderOptional.get();
        } else {
            throw new IllegalArgumentException("Order not found with id: " + id);
        }
    }


    @Override
    public List<Order> getAllOrders() {
        List<Order> allOrders = orderRepository.findAll();
        if(allOrders.size() > 0)
            return allOrders;
        else
            return new ArrayList<Order>();
    }
    
}
