package com.minicoy.demo.exception;

// extends RuntimeException so we don't
// need to declare it in method signatures!
public class ResourceNotFoundException
        extends RuntimeException {

    // constructor takes a message
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // convenience constructors for common cases!
    public static ResourceNotFoundException
    product(Long id) {
        return new ResourceNotFoundException(
                "Product not found with id: " + id);
    }

    public static ResourceNotFoundException
    order(Long id) {
        return new ResourceNotFoundException(
                "Order not found with id: " + id);
    }

    public static ResourceNotFoundException
    category(Long id) {
        return new ResourceNotFoundException(
                "Category not found with id: " + id);
    }
}
