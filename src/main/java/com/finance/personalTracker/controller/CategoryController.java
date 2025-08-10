package com.finance.personalTracker.controller;

import com.finance.personalTracker.dto.CategoryDTO;
import com.finance.personalTracker.enums.CategoryType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {


    @PostMapping("/")
    public void addCategory(@Valid @RequestBody CategoryDTO category){
        // Todo
    }

    @GetMapping("/{type}")
    public void retrieveCategoryByType(@PathVariable CategoryType type) {
        // Todo
    }



}
