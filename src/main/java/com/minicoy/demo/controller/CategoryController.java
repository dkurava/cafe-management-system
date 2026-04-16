package com.minicoy.demo.controller;

import com.minicoy.demo.model.Category;
import com.minicoy.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // GET ALL categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // CREATE category
    @PostMapping
    public Category addCategory(
            @RequestBody Category category) {
        return categoryRepository.save(category);
    }
}
