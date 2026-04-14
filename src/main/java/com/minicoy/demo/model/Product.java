package com.minicoy.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // This tells Spring Boot: "Create a table in the database for this class"
@Data   // This is Lombok! It creates Getters, Setters, and toString() for you
@AllArgsConstructor // Allows the new Product(null, "Name", ...) constructor
@NoArgsConstructor // Required by Spring/JPA. This tells Lombok to create the 5-argument constructor
public class Product {

    @Id // Tells Spring this is the Primary Key (Unique ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments (1, 2, 3...)
    private Long id;

    private String name;
    private Double price;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // This is the 5th ingredient!
}