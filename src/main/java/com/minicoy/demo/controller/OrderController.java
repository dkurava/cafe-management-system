package com.minicoy.demo.controller;

import com.minicoy.demo.model.Order;
import com.minicoy.demo.model.OrderItem;
import com.minicoy.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*") // allows UI to call this API!
public class OrderController {

    @Autowired
    private OrderService orderService;

    // GET ALL orders
    // http://localhost:8081/api/orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // GET ONE order by id
    // http://localhost:8081/api/orders/1
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // PLACE new order
    // POST http://localhost:8081/api/orders/place?customerName=Ram
    // Body: [{"product":{"id":1},"quantity":2,"price":5.50}]
    @PostMapping("/place")
    public Order placeOrder(
            @RequestParam String customerName,
            @RequestBody List<OrderItem> items) {
        return orderService.placeOrder(customerName, items);
    }

    // UPDATE order customer name
    // PUT http://localhost:8081/api/orders/1?customerName=Raj
    @PutMapping("/{id}")
    public Order updateOrder(
            @PathVariable Long id,
            @RequestParam String customerName) {
        return orderService.updateOrder(id, customerName);
    }

    // DELETE order
    // DELETE http://localhost:8081/api/orders/1
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "Order deleted successfully!";
    }
}
