package com.minicoy.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductDTO {

    @NotBlank(message = "Name is required!")
    private String name;

    @Positive(message = "Price must be greater than 0!")
    private double price;

    private String description;

    @NotNull(message = "Category is required!")
    private Long categoryId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}