package com.finance.personalTracker.services;

import com.finance.personalTracker.dto.CategoryDTO;
import com.finance.personalTracker.enums.CategoryType;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> getCategoriesByType(CategoryType type);

    List<CategoryDTO> getAllCategories();

}
