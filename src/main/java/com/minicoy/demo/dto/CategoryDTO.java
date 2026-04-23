package com.minicoy.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

    @NotBlank(message = "Category name is required!")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}