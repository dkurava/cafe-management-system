package com.minicoy.demo.controller;

import com.minicoy.demo.dto.ProductDTO;
import com.minicoy.demo.exception.ResourceNotFoundException;
import com.minicoy.demo.model.Category;
import com.minicoy.demo.model.Product;
import com.minicoy.demo.repository.ProductRepository;
import com.minicoy.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products",
        description = "Product management APIs")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    // ════════════════════════════════
    // GET ALL PRODUCTS
    // ════════════════════════════════
    @GetMapping
    @Operation(summary = "Get all products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // ════════════════════════════════
    // GET PRODUCT BY ID
    // ════════════════════════════════
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long id) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        return ResponseEntity.ok(product);
    }

    // ════════════════════════════════
    // ADD PRODUCT
    // ════════════════════════════════
    @PostMapping
    @Operation(summary = "Add new product")
    public ResponseEntity<Product> addProduct(
            @RequestBody @Valid ProductDTO dto) {

        // convert DTO to Entity!
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());

        // set category by ID!
        Category category = new Category();
        category.setId(dto.getCategoryId());
        product.setCategory(category);

        Product saved = productService.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    // ════════════════════════════════
    // UPDATE PRODUCT
    // ════════════════════════════════
    @PutMapping("/{id}")
    @Operation(summary = "Update product")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductDTO dto) {

        Product existing = productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        // update fields from DTO!
        existing.setName(dto.getName());
        existing.setPrice(dto.getPrice());
        existing.setDescription(dto.getDescription());

        // update category!
        Category category = new Category();
        category.setId(dto.getCategoryId());
        existing.setCategory(category);

        Product updated = productRepository.save(existing);
        return ResponseEntity.ok(updated);
    }

    // ════════════════════════════════
    // DELETE PRODUCT
    // ════════════════════════════════
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long id) {

        productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        productRepository.deleteById(id);
        return ResponseEntity.ok(
                "Product deleted successfully!");
    }
}