package com.minicoy.demo.service;

import com.minicoy.demo.model.Category;
import com.minicoy.demo.model.Product;
import com.minicoy.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

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

    // ════════════════════════════════
    // getAllProducts() TESTS
    // ════════════════════════════════

    // TEST 1
    @Test
    void getAllProducts_shouldReturnAllProducts() {

        // ARRANGE
        when(productRepository.findAll())
                .thenReturn(Arrays.asList(cappuccino, latte));

        // ACT
        List<Product> result =
                productService.getAllProducts();

        // ASSERT
        assertEquals(2, result.size(),
                "Should return 2 products!");
        assertEquals("Cappuccino",
                result.get(0).getName());
        assertEquals("Latte",
                result.get(1).getName());

        // verify findAll called once!
        verify(productRepository, times(1)).findAll();
    }

    // TEST 2
    @Test
    void getAllProducts_shouldReturnEmptyList() {

        // ARRANGE
        when(productRepository.findAll())
                .thenReturn(Arrays.asList());

        // ACT
        List<Product> result =
                productService.getAllProducts();

        // ASSERT
        assertTrue(result.isEmpty(),
                "Should return empty list!");
    }

    // ════════════════════════════════
    // saveProduct() TESTS
    // ════════════════════════════════

    // TEST 3
    @Test
    void saveProduct_shouldSaveAndReturnProduct() {

        // ARRANGE
        when(productRepository.save(cappuccino))
                .thenReturn(cappuccino);

        // ACT
        Product result =
                productService.saveProduct(cappuccino);

        // ASSERT
        assertNotNull(result,
                "Saved product should not be null!");
        assertEquals("Cappuccino", result.getName());
        assertEquals(5.50, result.getPrice());

        // verify save called once!
        verify(productRepository, times(1))
                .save(cappuccino);
    }

    // TEST 4
    // save product with category
    @Test
    void saveProduct_shouldPreserveCategory() {

        // ARRANGE
        when(productRepository.save(any(Product.class)))
                .thenReturn(cappuccino);

        // ACT
        Product result =
                productService.saveProduct(cappuccino);

        // ASSERT
        assertNotNull(result.getCategory(),
                "Category should not be null!");
        assertEquals("Hot Beverages",
                result.getCategory().getName());
    }

    // TEST 5
    // save should call repository
    @Test
    void saveProduct_shouldCallRepository() {

        // ARRANGE
        when(productRepository.save(any(Product.class)))
                .thenReturn(cappuccino);

        // ACT
        productService.saveProduct(cappuccino);

        // ASSERT
        // verify save was called!
        verify(productRepository, times(1))
                .save(any(Product.class));

        // verify findAll was NOT called!
        verify(productRepository, never()).findAll();
    }
}