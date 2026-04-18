package com.minicoy.demo.service;

import com.minicoy.demo.exception.ResourceNotFoundException;
import com.minicoy.demo.model.Order;
import com.minicoy.demo.model.OrderItem;
import com.minicoy.demo.model.Product;
import com.minicoy.demo.repository.OrderItemRepository;
import com.minicoy.demo.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// tells JUnit to use Mockito!
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    // ── MOCKS ──
    // fake repositories!
    // no real database!
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    // ── REAL CLASS BEING TESTED ──
    // injects the mocks above automatically!
    @InjectMocks
    private OrderService orderService;

    // ── TEST DATA ──
    // shared across tests!
    private OrderItem item1;
    private OrderItem item2;
    private Order savedOrder;

    // runs BEFORE each test!
    // fresh data for every test!
    @BeforeEach
    void setUp() {
        // create product
        Product coffee = new Product();
        coffee.setId(1L);
        coffee.setName("Cappuccino");
        coffee.setPrice(5.50);

        Product croissant = new Product();
        croissant.setId(2L);
        croissant.setName("Croissant");
        croissant.setPrice(3.50);

        // create order items
        item1 = new OrderItem();
        item1.setProduct(coffee);
        item1.setQuantity(2);
        item1.setPrice(5.50);
        // item1 total = 5.50 × 2 = 11.00

        item2 = new OrderItem();
        item2.setProduct(croissant);
        item2.setQuantity(1);
        item2.setPrice(3.50);
        // item2 total = 3.50 × 1 = 3.50

        // grand total = 11.00 + 3.50 = 14.50

        // create saved order (what DB returns)
        savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setCustomerName("Ram");
        savedOrder.setTotalAmount(14.50);
    }

    // ════════════════════════════════
    // placeOrder() TESTS
    // ════════════════════════════════

    // TEST 1
    // most important test!
    // does total calculate correctly?
    @Test
    void placeOrder_shouldCalculateTotalCorrectly() {

        // ARRANGE
        // when save() called on ANY Order object
        // return our savedOrder!
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    // return whatever order was passed in!
                    // so we can check its values!
                    return invocation.getArgument(0);
                });

        when(orderItemRepository.saveAll(any()))
                .thenReturn(Arrays.asList(item1, item2));

        // ACT
        // call the REAL placeOrder method!
        Order result = orderService.placeOrder(
                "Ram",
                Arrays.asList(item1, item2));

        // ASSERT
        // was total calculated correctly?
        assertEquals(14.50, result.getTotalAmount(),
                "Total should be 14.50!");
        // 5.50×2 + 3.50×1 = 14.50 ✅
    }

    // TEST 2
    // does customer name get set?
    @Test
    void placeOrder_shouldSetCustomerName() {

        // ARRANGE
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(i -> i.getArgument(0));

        when(orderItemRepository.saveAll(any()))
                .thenReturn(Arrays.asList(item1));

        // ACT
        Order result = orderService.placeOrder(
                "Divya",
                Arrays.asList(item1));

        // ASSERT
        assertEquals("Divya", result.getCustomerName(),
                "Customer name should be Divya!");
    }

    // TEST 3
    // does order date get set?
    @Test
    void placeOrder_shouldSetOrderDate() {

        // ARRANGE
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(i -> i.getArgument(0));

        when(orderItemRepository.saveAll(any()))
                .thenReturn(Arrays.asList(item1));

        // ACT
        Order result = orderService.placeOrder(
                "Ram",
                Arrays.asList(item1));

        // ASSERT
        assertNotNull(result.getOrderDate(),
                "Order date should not be null!");
    }

    // TEST 4
    // does each item get linked to order?
    @Test
    void placeOrder_shouldLinkItemsToOrder() {

        // ARRANGE
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(i -> i.getArgument(0));

        when(orderItemRepository.saveAll(any()))
                .thenReturn(Arrays.asList(item1, item2));

        // ACT
        Order result = orderService.placeOrder(
                "Ram",
                Arrays.asList(item1, item2));

        // ASSERT
        // each item should have order linked!
        assertNotNull(item1.getOrder(),
                "Item1 should be linked to order!");
        assertNotNull(item2.getOrder(),
                "Item2 should be linked to order!");
    }

    // TEST 5
    // does repository save get called?
    @Test
    void placeOrder_shouldCallRepositorySave() {

        // ARRANGE
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(i -> i.getArgument(0));

        when(orderItemRepository.saveAll(any()))
                .thenReturn(Arrays.asList(item1));

        // ACT
        orderService.placeOrder(
                "Ram",
                Arrays.asList(item1));

        // ASSERT
        // verify save was called exactly ONCE!
        verify(orderRepository, times(1))
                .save(any(Order.class));

        // verify saveAll was called exactly ONCE!
        verify(orderItemRepository, times(1))
                .saveAll(any());
    }

    // TEST 6
    // does single item total calculate?
    @Test
    void placeOrder_shouldCalculateSingleItemTotal() {

        // ARRANGE
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(i -> i.getArgument(0));

        when(orderItemRepository.saveAll(any()))
                .thenReturn(Arrays.asList(item1));

        // ACT
        // only item1: 5.50 × 2 = 11.00
        Order result = orderService.placeOrder(
                "Ram",
                Arrays.asList(item1));

        // ASSERT
        assertEquals(11.00, result.getTotalAmount(),
                "Single item total should be 11.00!");
    }

    // ════════════════════════════════
    // getAllOrders() TESTS
    // ════════════════════════════════

    // TEST 7
    @Test
    void getAllOrders_shouldReturnAllOrders() {

        // ARRANGE
        Order order1 = new Order();
        order1.setCustomerName("Ram");

        Order order2 = new Order();
        order2.setCustomerName("Raj");

        when(orderRepository.findAll())
                .thenReturn(Arrays.asList(order1, order2));

        // ACT
        List<Order> result = orderService.getAllOrders();

        // ASSERT
        assertEquals(2, result.size(),
                "Should return 2 orders!");
        assertEquals("Ram",
                result.get(0).getCustomerName());
        assertEquals("Raj",
                result.get(1).getCustomerName());
    }

    // TEST 8
    // empty list scenario
    @Test
    void getAllOrders_shouldReturnEmptyList() {

        // ARRANGE
        when(orderRepository.findAll())
                .thenReturn(Arrays.asList());
        // empty list!

        // ACT
        List<Order> result = orderService.getAllOrders();

        // ASSERT
        assertTrue(result.isEmpty(),
                "Should return empty list!");
    }

    // ════════════════════════════════
    // getOrderById() TESTS
    // ════════════════════════════════

    // TEST 9
    @Test
    void getOrderById_shouldReturnOrder_whenExists() {

        // ARRANGE
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(savedOrder));
        // Optional.of() = box WITH order inside!

        // ACT
        Optional<Order> result =
                orderService.getOrderById(1L);

        // ASSERT
        assertTrue(result.isPresent(),
                "Order should be present!");
        assertEquals("Ram",
                result.get().getCustomerName());
    }

    // TEST 10
    // what if order doesn't exist?
    @Test
    void getOrderById_shouldReturnEmpty_whenNotExists() {

        // ARRANGE
        when(orderRepository.findById(999L))
                .thenReturn(Optional.empty());
        // Optional.empty() = empty box!

        // ACT
        Optional<Order> result =
                orderService.getOrderById(999L);

        // ASSERT
        assertFalse(result.isPresent(),
                "Order should not be present!");
    }

    // ════════════════════════════════
    // updateOrder() TESTS
    // ════════════════════════════════

    // TEST 11
    @Test
    void updateOrder_shouldUpdateCustomerName() {

        // ARRANGE
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setCustomerName("Ram");

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(existingOrder));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(i -> i.getArgument(0));

        // ACT
        Optional<Order> result =
                orderService.updateOrder(1L, "Raj");

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals("Raj",
                result.get().getCustomerName(),
                "Name should be updated to Raj!");
    }

    // TEST 12
    // update non-existent order
    @Test
    void updateOrder_shouldReturnEmpty_whenNotExists() {

        // ARRANGE
        when(orderRepository.findById(999L))
                .thenReturn(Optional.empty());

        // ACT
        Optional<Order> result =
                orderService.updateOrder(999L, "Raj");

        // ASSERT
        assertFalse(result.isPresent(),
                "Should return empty for non-existent order!");
    }
}