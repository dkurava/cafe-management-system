package com.minicoy.demo.service;

import com.minicoy.demo.model.Order;
import com.minicoy.demo.model.OrderItem;
import com.minicoy.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order placeOrder(String customerName, List<OrderItem> items) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setOrderDate(new Date());

        double total = 0.0;

        // Classic loop - works perfectly in JDK 17
        for (OrderItem item : items) {
            // Formula: Price * Quantity
            total = total + (item.getPrice() * item.getQuantity());
        }

        order.setTotalAmount(total);

        // This returns the Order object after MySQL saves it
        return orderRepository.save(order);
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}