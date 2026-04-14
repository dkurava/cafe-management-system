package com.minicoy.demo.controller;

import com.minicoy.demo.model.Order;
import com.minicoy.demo.model.OrderItem;
import com.minicoy.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // CREATE: Place a new order
    // We send a List of OrderItems in the request body
    @PostMapping("/place")
    public Order createOrder(@RequestParam String customerName, @RequestBody List<OrderItem> items) {
        return orderService.placeOrder(customerName, items);
    }

    // READ: Get all orders for the owner to see
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
        // Note: You'll need to add a simple getAllOrders() in your Service/Repository
    }
}
