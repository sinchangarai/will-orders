package com.bits.wilp.order.controller;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bits.wilp.order.model.Order;
import com.bits.wilp.order.model.Product;
import com.bits.wilp.order.service.OrderService;
import com.bits.wilp.order.util.HttpUtil;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    private HttpUtil httpUtil = new HttpUtil();


    @PostMapping("/orders/placeOrder")
    public ResponseEntity<?> placeOrder(@RequestBody Order order, HttpServletRequest request) {
        boolean isJwtExpired = httpUtil.isJwtExpired(request);
        if(isJwtExpired)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try{
            Order newOrder = orderService.placeOrder(order, request);
            return new ResponseEntity<Order>(newOrder, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/orders/{id}/deliverOrder")
    public ResponseEntity<?> deliverOrder(@PathVariable("id") String id, HttpServletRequest request) {
        boolean isJwtExpired = httpUtil.isJwtExpired(request);
        if(isJwtExpired)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try{
            Order newOrder = orderService.deliverOrder(id);
            return new ResponseEntity<Order>(newOrder, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @PutMapping("/orders/{id}/cancelOrder")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") String id, HttpServletRequest request) {
        boolean isJwtExpired = httpUtil.isJwtExpired(request);
        if(isJwtExpired)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try{
            Order newOrder = orderService.cancelOrder(id);
            return new ResponseEntity<Order>(newOrder, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getSingleProduct(@PathVariable("id") String id, HttpServletRequest request) {
        boolean isJwtExpired = httpUtil.isJwtExpired(request);
        if(isJwtExpired)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try{
            Order order = orderService.getSingleOrder(id);
            return new ResponseEntity<Order>(order, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders(HttpServletRequest request) {
        boolean isJwtExpired = httpUtil.isJwtExpired(request);
        if(isJwtExpired)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, orders.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
    
}
