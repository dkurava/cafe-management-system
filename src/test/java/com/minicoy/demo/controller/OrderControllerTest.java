package com.minicoy.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minicoy.demo.model.Order;
import com.minicoy.demo.model.OrderItem;
import com.minicoy.demo.model.Product;
import com.minicoy.demo.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest           // loads full Spring context!
@AutoConfigureMockMvc     // sets up MockMvc automatically!
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    private Order order1;
    private Order order2;
    private List<OrderItem> orderItems;

    @BeforeEach
    void setUp() {
        // create test orders
        order1 = new Order();
        order1.setId(1L);
        order1.setCustomerName("Ram");
        order1.setTotalAmount(14.50);
        order1.setOrderDate(new Date());

        order2 = new Order();
        order2.setId(2L);
        order2.setCustomerName("Divya");
        order2.setTotalAmount(11.00);
        order2.setOrderDate(new Date());

        // create test order items
        Product coffee = new Product();
        coffee.setId(1L);

        OrderItem item1 = new OrderItem();
        item1.setProduct(coffee);
        item1.setQuantity(2);
        item1.setPrice(5.50);

        orderItems = Arrays.asList(item1);
    }

    // ════════════════════════════════
    // GET ALL ORDERS TESTS
    // ════════════════════════════════

    // TEST 1
    @Test
    void getAllOrders_shouldReturn200() throws Exception {

        // ARRANGE
        when(orderService.getAllOrders())
                .thenReturn(Arrays.asList(order1, order2));

        // ACT + ASSERT
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()").value(2))
                .andExpect(
                        jsonPath("$[0].customerName")
                                .value("Ram"))
                .andExpect(
                        jsonPath("$[1].customerName")
                                .value("Divya"));
    }

    // TEST 2
    @Test
    void getAllOrders_shouldReturnEmptyList()
            throws Exception {

        // ARRANGE
        when(orderService.getAllOrders())
                .thenReturn(Arrays.asList());

        // ACT + ASSERT
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()").value(0));
    }

    // ════════════════════════════════
    // GET ORDER BY ID TESTS
    // ════════════════════════════════

    // TEST 3
    @Test
    void getOrderById_shouldReturn200_whenExists()
            throws Exception {

        // ARRANGE
        when(orderService.getOrderById(1L))
                .thenReturn(Optional.of(order1));

        // ACT + ASSERT
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.customerName").value("Ram"))
                .andExpect(
                        jsonPath("$.totalAmount").value(14.50));
    }

    // TEST 4 — EXCEPTION TEST!
    @Test
    void getOrderById_shouldReturn404_whenNotExists()
            throws Exception {

        // ARRANGE
        when(orderService.getOrderById(999L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound()) // 404!
                .andExpect(
                        jsonPath("$.status").value(404))
                .andExpect(
                        jsonPath("$.message").value(
                                "Order not found with id: 999"));
    }

    // ════════════════════════════════
    // POST PLACE ORDER TESTS
    // ════════════════════════════════

    // TEST 5
    @Test
    void placeOrder_shouldReturn200_whenValid()
            throws Exception {

        // ARRANGE
        when(orderService.placeOrder(
                eq("Ram"), anyList()))
                .thenReturn(order1);

        String itemsJson =
                objectMapper.writeValueAsString(orderItems);

        // ACT + ASSERT
        mockMvc.perform(
                        post("/api/orders/place")
                                .param("customerName", "Ram")
                                // ↑ RequestParam!
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(itemsJson)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.customerName").value("Ram"))
                .andExpect(
                        jsonPath("$.totalAmount").value(14.50));
    }

    // TEST 6 — EXCEPTION TEST!
    // empty customer name → 400!
    @Test
    void placeOrder_shouldReturn400_whenNameEmpty()
            throws Exception {

        // ARRANGE
        String itemsJson =
                objectMapper.writeValueAsString(orderItems);

        // ACT + ASSERT
        mockMvc.perform(
                        post("/api/orders/place")
                                .param("customerName", "") // empty!
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(itemsJson)
                )
                .andExpect(status().isBadRequest()) // 400!
                .andExpect(
                        jsonPath("$.message").value(
                                "Customer name cannot be empty!"));
    }

    // TEST 7 — EXCEPTION TEST!
    // empty items list → 400!
    @Test
    void placeOrder_shouldReturn400_whenItemsEmpty()
            throws Exception {

        // ARRANGE
        String emptyItemsJson =
                objectMapper.writeValueAsString(
                        Arrays.asList()); // empty list!

        // ACT + ASSERT
        mockMvc.perform(
                        post("/api/orders/place")
                                .param("customerName", "Ram")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(emptyItemsJson)
                )
                .andExpect(status().isBadRequest()) // 400!
                .andExpect(
                        jsonPath("$.message").value(
                                "Order must have at least one item!"));
    }

    // ════════════════════════════════
    // PUT UPDATE ORDER TESTS
    // ════════════════════════════════

    // TEST 8
    @Test
    void updateOrder_shouldReturn200_whenExists()
            throws Exception {

        // ARRANGE
        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setCustomerName("Raj"); // updated!
        updatedOrder.setTotalAmount(14.50);

        when(orderService.updateOrder(1L, "Raj"))
                .thenReturn(Optional.of(updatedOrder));

        // ACT + ASSERT
        mockMvc.perform(
                        put("/api/orders/1")
                                .param("customerName", "Raj")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.customerName").value("Raj"));
    }

    // TEST 9 — EXCEPTION TEST!
    @Test
    void updateOrder_shouldReturn404_whenNotExists()
            throws Exception {

        // ARRANGE
        when(orderService.updateOrder(999L, "Raj"))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        mockMvc.perform(
                        put("/api/orders/999")
                                .param("customerName", "Raj")
                )
                .andExpect(status().isNotFound()) // 404!
                .andExpect(
                        jsonPath("$.message").value(
                                "Order not found with id: 999"));
    }

    // ════════════════════════════════
    // DELETE ORDER TESTS
    // ════════════════════════════════

    // TEST 10
    @Test
    void deleteOrder_shouldReturn200_whenExists()
            throws Exception {

        // ARRANGE
        when(orderService.getOrderById(1L))
                .thenReturn(Optional.of(order1));

        doNothing()
                .when(orderService).deleteOrder(1L);

        // ACT + ASSERT
        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk());

        verify(orderService, times(1)).deleteOrder(1L);
    }

    // TEST 11 — EXCEPTION TEST!
    @Test
    void deleteOrder_shouldReturn404_whenNotExists()
            throws Exception {

        // ARRANGE
        when(orderService.getOrderById(999L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        mockMvc.perform(delete("/api/orders/999"))
                .andExpect(status().isNotFound()) // 404!
                .andExpect(
                        jsonPath("$.message").value(
                                "Order not found with id: 999"));
    }
}