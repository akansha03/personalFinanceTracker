package com.finance.personalTracker.services.impl;

import com.finance.personalTracker.dto.CategoryDTO;
import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.enums.CategoryType;
import com.finance.personalTracker.enums.ExpenseCategory;
import com.finance.personalTracker.enums.IncomeCategory;
import com.finance.personalTracker.repositories.CategoryRepo;
import com.finance.personalTracker.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
       Category category = modelMapper.map(categoryDTO, Category.class);
       Category savedCategory = this.categoryRepo.save(category);
       return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> getCategoriesByType(CategoryType type) {
        List<CategoryDTO> allCategories = new ArrayList<>();
        allCategories.addAll(getDefaultCategories(type));
        allCategories.addAll(mapEntitiesToDTO(categoryRepo.findByType(type)));
        return allCategories;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {

        List<CategoryDTO> dbCategories = mapEntitiesToDTO(categoryRepo.findAll());

        List<CategoryDTO> defaultCategories = Arrays.stream(CategoryType.values())
                .flatMap(type -> getDefaultCategories(type).stream())
                .toList();

        List<CategoryDTO> allCategories = new ArrayList<>();
        allCategories.addAll(dbCategories);
        allCategories.addAll(defaultCategories);
        return allCategories;
    }

    // Helper Methods Start Here

    private List<CategoryDTO> getDefaultCategories(CategoryType type) {
            return getEnumCategoryNames(type).stream()
                    .map(name -> {
                        CategoryDTO categoryDTO = new CategoryDTO();
                        categoryDTO.setName(name);
                        categoryDTO.setType(type);
                        return categoryDTO;
                    }).toList();
    }

    private List<CategoryDTO> mapEntitiesToDTO(List<Category> categories) {
        if(categories == null || categories.isEmpty()){
            return Collections.emptyList();
        }
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
    }

    // Generic Enum -> Names Extractor
    private List<String> getEnumCategoryNames(CategoryType type) {
        Class<? extends Enum<?>> enumClass =
                (type == CategoryType.Income)
                ? IncomeCategory.class: ExpenseCategory.class;

        return Stream.of(enumClass.getEnumConstants())
                .map(Enum::name)
                .toList();
    }
}
