package com.minicoy.demo.service;

import com.minicoy.demo.model.Product;
import com.minicoy.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // Tells Spring: "I am the Business Logic layer"
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // This is a CRUD operation: READ
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // This is a CRUD operation: CREATE
    public Product saveProduct(Product product) {
        // Here you could add logic: if (product.getPrice() < 0) throw error!
        return productRepository.save(product);
    }
}