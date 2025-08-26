package com.finance.personalTracker.category;

import com.finance.personalTracker.dto.CategoryDTO;
import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.enums.CategoryType;
import com.finance.personalTracker.exception.BusinessValidationException;
import com.finance.personalTracker.repositories.CategoryRepo;
import com.finance.personalTracker.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepo categoryRepo;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryDTO categoryDTO;

    @Test
    void CategoryService_AddANewCategory_ReturnSavedCategory() {
        categoryDTO = new CategoryDTO();
        categoryDTO.setName("Beauty");
        categoryDTO.setType(CategoryType.Expense);

        // Stub to mock the response
        Category saved = new Category();
        saved.setId(1L);
        saved.setName("Beauty");
        saved.setType(CategoryType.Expense);

        given(categoryRepo.save(any(Category.class))).willReturn(saved);

        CategoryDTO result = categoryService.createCategory(categoryDTO);
        assertThat(result).isNotNull();
        assertThat(result.getName().equalsIgnoreCase("Beauty"));
        assertThat(result.getType().equals(CategoryType.Expense));
        verify(categoryRepo).existsByNameAndType("Beauty", CategoryType.Expense);
    }

    @Test
    void addCategoryWithAnEmptyName(){
        // Stub to mock the response
        Category saved = new Category();
        saved.setId(1L);
        saved.setType(CategoryType.Income);

        given(categoryRepo.save(any(Category.class))).willThrow(BusinessValidationException.class);
        categoryDTO = new CategoryDTO();
        categoryDTO.setType(CategoryType.Income);
        CategoryDTO result = categoryService.createCategory(categoryDTO);

    }
}
