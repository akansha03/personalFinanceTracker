package com.finance.personalTracker.controller;

import com.finance.personalTracker.dto.CategoryDTO;
import com.finance.personalTracker.enums.CategoryType;
import com.finance.personalTracker.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createCategory = this.categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDTO>> getCategoryByType(@PathVariable CategoryType type) {
        List<CategoryDTO> categoriesByType = this.categoryService.getCategoriesByType(type);
        return ResponseEntity.ok(categoriesByType);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = this.categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
