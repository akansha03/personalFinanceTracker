package com.finance.personalTracker.service;

import com.finance.personalTracker.dto.CategoryDTO;
import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.enums.CategoryType;
import com.finance.personalTracker.repositories.CategoryRepo;
import com.finance.personalTracker.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void CategoryService_AddANewCategory_ReturnCategoryDTO() {

        Category category = Category.builder().id(1L).name("Vacation").type(CategoryType.Expense).build();
        CategoryDTO categoryDTO = CategoryDTO.builder().name("Vacation").type(CategoryType.Expense).build();

        //mock the db save
        when(categoryRepo.save(any(Category.class))).thenReturn(category);
        CategoryDTO savedCategory =  categoryService.createCategory(categoryDTO);
        assertThat(savedCategory).isNotNull();
    }

    @Test
    void CategoryService_GetAllCategories_ReturnsCategoryDTO() {

       Category category1 = Category.builder().id(1L).name("Food").type(CategoryType.Expense).build();
       Category category2 = Category.builder().id(2L).name("Rent").type(CategoryType.Income).build();

       when(categoryRepo.findAll()).thenReturn(List.of(category1, category2));

       List<CategoryDTO> categories = categoryService.getAllCategories();

       assertThat(categories).isNotNull();
       assertThat(categories.size()).isGreaterThanOrEqualTo(2);

       assertThat(categories.get(0).getType()).isEqualTo(CategoryType.Expense);
       assertThat(categories.get(0).getName()).isEqualTo("Food");

       assertThat(categories.get(1).getType()).isEqualTo(CategoryType.Income);
       assertThat(categories.get(1).getName()).isEqualTo("Rent");

       verify(categoryRepo, times(1)).findAll();
    }

    @Test
    void CategoryService_GetCategoryByType_ReturnCategoryDTO() {

        Category category = Category.builder().id(1L).name("Vacation").type(CategoryType.Expense).build();
        when(categoryRepo.findByType(CategoryType.Expense)).thenReturn(List.of(category));
        List<CategoryDTO> categoryDTO = categoryService.getCategoriesByType(CategoryType.Expense);

        assertThat(categoryDTO).isNotNull();
        assertThat(categoryDTO.get(0).getType()).isEqualTo(CategoryType.Expense);
    }
}
