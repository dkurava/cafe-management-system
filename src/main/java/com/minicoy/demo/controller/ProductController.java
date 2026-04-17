package com.minicoy.demo.controller;

import com.minicoy.demo.exception.ResourceNotFoundException;
import com.minicoy.demo.model.Product;
import com.minicoy.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // allows UI to call this API!
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // GET ALL products
    // http://localhost:8081/api/products
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // GET ONE product by id
    // http://localhost:8081/api/products/1
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        ResourceNotFoundException.product(id));
        //↑ if not found → throws exception
        //        //    GlobalExceptionHandler catches it
        //        //    returns clean 404 JSON!
    }

    // CREATE new product
    // POST http://localhost:8081/api/products
    // Body: {"name":"Latte","price":4.50,"description":"...","category":{"id":1}}
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        //validate price
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException(
                    "Price must be greater than 0!");
        }
        return productRepository.save(product);
    }


    // UPDATE product price
    // PUT http://localhost:8081/api/products/1
    // Body: {"name":"Latte","price":6.00,"description":"...","category":{"id":1}}
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id,
                                 @RequestBody Product product) {
        // check if product exists first!
        productRepository.findById(id)
                .orElseThrow(() ->
                        ResourceNotFoundException.product(id));

        product.setId(id); // make sure correct id!
        return productRepository.save(product);
    }


    // DELETE product
    // DELETE http://localhost:8081/api/products/1
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        // check if product exists first!
        productRepository.findById(id)
                .orElseThrow(() ->
                        ResourceNotFoundException.product(id));

        productRepository.deleteById(id);
        return "Product deleted successfully!";
    }

}