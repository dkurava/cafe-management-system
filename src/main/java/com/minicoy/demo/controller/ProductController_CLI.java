package com.minicoy.demo.controller;

import com.minicoy.demo.model.Product;
import com.minicoy.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController // Tells Spring: "I am a web endpoint that returns JSON"
//@RequestMapping("/api/products") // The base URL for all methods in this class
public class ProductController_CLI {

    @Autowired // "Dependency Injection" - Spring automatically plugs in the Repository
    private ProductService productService;

    @GetMapping // Handles HTTP GET requests (like typing the URL in a browser)
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }
}