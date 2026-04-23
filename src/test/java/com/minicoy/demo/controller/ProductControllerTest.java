package com.minicoy.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minicoy.demo.model.Category;
import com.minicoy.demo.model.Product;
import com.minicoy.demo.repository.ProductRepository;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest loads ONLY web layer!
// no full Spring context!
// no real database!
// FAST! ✅

@SpringBootTest           // loads full Spring context!
@AutoConfigureMockMvc(addFilters = false)     // sets up MockMvc automatically!
@ActiveProfiles("test")
class ProductControllerTest {

    // MockMvc simulates HTTP requests!
    // no real server needed!
    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper converts objects to JSON string!
    @Autowired
    private ObjectMapper objectMapper;

    // @MockBean = @Mock but for Spring context!
    // replaces real repository with fake!
    @MockitoBean
    private ProductRepository productRepository;

    // test data
    private Product cappuccino;
    private Product latte;
    private Category hotBeverages;

    @BeforeEach
    void setUp() {
        hotBeverages = new Category();
        hotBeverages.setId(1L);
        hotBeverages.setName("Hot Beverages");

        cappuccino = new Product();
        cappuccino.setId(1L);
        cappuccino.setName("Cappuccino");
        cappuccino.setPrice(5.50);
        cappuccino.setDescription("Espresso with milk foam");
        cappuccino.setCategory(hotBeverages);

        latte = new Product();
        latte.setId(2L);
        latte.setName("Latte");
        latte.setPrice(5.00);
        latte.setDescription("Espresso with milk");
        latte.setCategory(hotBeverages);
    }

    private String validProductDtoJson()
            throws Exception {
        return """
            {
                "name": "Cappuccino",
                "price": 5.50,
                "description": "Espresso with milk foam",
                "categoryId": 1
            }
            """;
    }

    // ════════════════════════════════
    // GET ALL PRODUCTS TESTS
    // ════════════════════════════════

    // TEST 1
    // GET /api/products → 200 OK
    @Test
    void getAllProducts_shouldReturn200() throws Exception {

        // ARRANGE
        when(productRepository.findAll())
                .thenReturn(Arrays.asList(cappuccino, latte));

        // ACT + ASSERT
        mockMvc.perform(
                        get("/api/products") // simulate GET request!
                )
                .andExpect(status().isOk()) // expect 200!
                .andExpect(
                        jsonPath("$.length()").value(2)) // 2 products!
                .andExpect(
                        jsonPath("$[0].name").value("Cappuccino"))
                .andExpect(
                        jsonPath("$[1].name").value("Latte"));
    }

    // TEST 2
    // GET /api/products → empty list
    @Test
    void getAllProducts_shouldReturnEmptyList() throws Exception {

        // ARRANGE
        when(productRepository.findAll())
                .thenReturn(Arrays.asList());

        // ACT + ASSERT
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()").value(0));
    }

    // ════════════════════════════════
    // GET PRODUCT BY ID TESTS
    // ════════════════════════════════

    // TEST 3
    // GET /api/products/1 → 200 OK
    @Test
    void getProductById_shouldReturn200_whenExists()
            throws Exception {

        // ARRANGE
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(cappuccino));

        // ACT + ASSERT
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name").value("Cappuccino"))
                .andExpect(
                        jsonPath("$.price").value(5.50));
    }

    // TEST 4 — EXCEPTION TEST!
    // GET /api/products/999 → 404!
    @Test
    void getProductById_shouldReturn404_whenNotExists()
            throws Exception {

        // ARRANGE
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound()) // 404!
                .andExpect(
                        jsonPath("$.status").value(404))
                .andExpect(
                        jsonPath("$.error").value("Not Found"))
                .andExpect(
                        jsonPath("$.message").value(
                                "Product not found with id: 999"));
    }

    // ════════════════════════════════
    // POST ADD PRODUCT TESTS
    // ════════════════════════════════

    // TEST 5
    // POST /api/products → 200 OK
    @Test
    void addProduct_shouldReturn200_whenValid()
            throws Exception {

        // ARRANGE
        when(productRepository.save(any(Product.class)))
                .thenReturn(cappuccino);


        // ACT + ASSERT
        mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                // ↑ tells server: sending JSON!
                                .content(validProductDtoJson())
                        // ↑ JSON body!
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name").value("Cappuccino"));
    }

    // TEST 6 — EXCEPTION TEST!
    // POST with invalid price → 400!
    @Test
    void addProduct_shouldReturn400_whenPriceInvalid()
            throws Exception {

        // send valid categoryId but invalid price!
        String invalidProductJson = """
            {
                "name": "Test",
                "price": -5.0,
                "description": "test",
                "categoryId": 1
            }
            """;

        // ACT + ASSERT
        mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidProductJson)
                )
                .andExpect(status().isBadRequest()) // 400!
                .andExpect(
                        jsonPath("$.status").value(400))
                .andExpect(
                        jsonPath("$.message").value(
                                "Price must be greater than 0!"));
    }

    // ════════════════════════════════
    // PUT UPDATE PRODUCT TESTS
    // ════════════════════════════════

    // TEST 7
    // PUT /api/products/1 → 200 OK
    @Test
    void updateProduct_shouldReturn200_whenExists()
            throws Exception {

        // ARRANGE
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Cappuccino");
        updatedProduct.setPrice(6.00); // updated price!
        updatedProduct.setDescription("Espresso with milk foam");
        updatedProduct.setCategory(hotBeverages);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(cappuccino));

        when(productRepository.save(any(Product.class)))
                .thenReturn(updatedProduct);

        // use DTO with updated price!
        String updatedDtoJson = """
            {
                "name": "Cappuccino",
                "price": 6.00,
                "description": "Espresso with milk foam",
                "categoryId": 1
            }
            """;

        // ACT + ASSERT
        mockMvc.perform(
                        put("/api/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatedDtoJson)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.price").value(6.00));
        // price updated to 6.00! ✅
    }

    // TEST 8 — EXCEPTION TEST!
    // PUT /api/products/999 → 404!
    @Test
    void updateProduct_shouldReturn404_whenNotExists()
            throws Exception {

        // ARRANGE
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());


        // ACT + ASSERT
        mockMvc.perform(
                        put("/api/products/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validProductDtoJson())
                )
                .andExpect(status().isNotFound()) // 404!
                .andExpect(
                        jsonPath("$.message").value(
                                "Product not found with id: 999"));
    }

    // ════════════════════════════════
    // DELETE PRODUCT TESTS
    // ════════════════════════════════

    // TEST 9
    // DELETE /api/products/1 → 200 OK
    @Test
    void deleteProduct_shouldReturn200_whenExists()
            throws Exception {

        // ARRANGE
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(cappuccino));

        doNothing()
                .when(productRepository).deleteById(1L);
        // tell mock: deleteById does nothing!
        // (void method mock!)

        // ACT + ASSERT
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());

        // verify deleteById was called!
        verify(productRepository, times(1))
                .deleteById(1L);
    }

    // TEST 10 — EXCEPTION TEST!
    // DELETE /api/products/999 → 404!
    @Test
    void deleteProduct_shouldReturn404_whenNotExists()
            throws Exception {

        // ARRANGE
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        mockMvc.perform(delete("/api/products/999"))
                .andExpect(status().isNotFound()) // 404!
                .andExpect(
                        jsonPath("$.message").value(
                                "Product not found with id: 999"));
    }
}