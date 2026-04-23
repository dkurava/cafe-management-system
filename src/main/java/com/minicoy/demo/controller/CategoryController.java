package com.minicoy.demo.controller;

import com.minicoy.demo.dto.CategoryDTO;
import com.minicoy.demo.model.Category;
import com.minicoy.demo.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories",
        description = "Category management APIs")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // GET ALL categories
    @GetMapping
    @Operation(summary = "Get all categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<Category> getCategoryById(
            @PathVariable Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new com.minicoy.demo.exception
                                .ResourceNotFoundException(
                                "Category not found with id: " + id));

        return ResponseEntity.ok(category);
    }

    // CREATE category
    @PostMapping
    @Operation(summary = "Add new category")
    public ResponseEntity<Category> addCategory(
            @RequestBody @Valid CategoryDTO dto) {

        // convert DTO to Entity!
        Category category = new Category();
        category.setName(dto.getName());

        Category saved = categoryRepository.save(category);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryDTO dto) {

        Category existing = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new com.minicoy.demo.exception
                                .ResourceNotFoundException(
                                "Category not found with id: " + id));

        existing.setName(dto.getName());
        Category updated = categoryRepository.save(existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id) {

        categoryRepository.findById(id)
                .orElseThrow(() ->
                        new com.minicoy.demo.exception
                                .ResourceNotFoundException(
                                "Category not found with id: " + id));

        categoryRepository.deleteById(id);
        return ResponseEntity.ok(
                "Category deleted successfully!");
    }

}
