package com.minicoy.demo.service;

import com.minicoy.demo.model.Order;
import com.minicoy.demo.model.OrderItem;
import com.minicoy.demo.repository.OrderItemRepository;
import com.minicoy.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Place new order
    public Order placeOrder(String customerName,
                            List<OrderItem> items) {
        // create order first
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setOrderDate(new Date());

        // calculate total
        double total = 0.0;
        for (OrderItem item : items) {
            total += (item.getPrice() * item.getQuantity());
            item.setOrder(order); // link item to order!
        }

        order.setTotalAmount(total);

        // save order + items
        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(items);
        return savedOrder;
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get by id — returns Optional!
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Get order by id
    //public Order getOrderById(Long id) {
    //    return orderRepository.findById(id)
    //            .orElse(null);
    //}

    // Update order customer name
    //public Order updateOrder(Long id, String customerName) {
    //    Order order = orderRepository.findById(id)
    //            .orElse(null);
    //    if (order != null) {
    //        order.setCustomerName(customerName);
    //        return orderRepository.save(order);
    //    }
    //    return null;
    //}

    // Update — returns Optional!
    public Optional<Order> updateOrder(Long id,
                                       String customerName) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setCustomerName(customerName);
                    return orderRepository.save(order);
                });
    }

    // Delete order
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
