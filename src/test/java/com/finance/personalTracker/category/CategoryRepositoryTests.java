package com.finance.personalTracker.category;

import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.enums.CategoryType;
import com.finance.personalTracker.repositories.CategoryRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepo categoryRepo;

    @Test
    void CategoryRepository_CreateCategory_ReturnSavedCategory() {
        // Follow the arrange act and assert model
        // Arrange
        Category category = Category.builder()
                .name("Beauty")
                .type(CategoryType.Expense).build();
        // Act
        Category savedCategory = categoryRepo.save(category);
        // Assert
        assertThat(savedCategory).isNotNull();
    }

    @Test
    void CategoryRepository_GetAllCategory_ReturnMoreThanOneCategory(){
        Category category1 = Category.builder()
                .name("AirBnB Rent")
                .type(CategoryType.Income)
                .build();
        Category category2 = Category.builder()
                .name("Hair Spa")
                .type(CategoryType.Expense)
                .build();
        categoryRepo.save(category1);
        categoryRepo.save(category2);
        //Act
        List<Category> categoryList = categoryRepo.findAll();
        assertThat(categoryList).isNotNull();
        assertThat(categoryList.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void CategoryRepository_GetCategoryByType_ReturnAllByType() {
        Category category = Category.builder()
                .name("Mobile Bills")
                .type(CategoryType.Expense)
                .build();

        categoryRepo.save(category);
        List<Category> result = categoryRepo.findByType(CategoryType.Expense);
        assertThat(result).isNotNull();
        result.forEach(r -> assertThat(r.getType()).isEqualTo(CategoryType.Expense));
    }

    @Test
    void CategoryRepository_GetCategoryById_ReturnCategory() {

        Category category = Category.builder()
                .name("Medicine Bills")
                .type(CategoryType.Expense)
                .build();
        categoryRepo.save(category);
        Category result = categoryRepo.findById(category.getId()).get();
        assertThat(result).isNotNull();
    }

    @Test
    void CategoryRepository_FindByNameAndType_ReturnNotNull() {

        Category category = Category.builder()
                .name("Topmate")
                .type(CategoryType.Income)
                .build();

        categoryRepo.save(category);
        Category savedCategory = categoryRepo.
                findByNameAndType(category.getName(), category.getType()).get();
        assertThat(savedCategory).isNotNull();
    }

    @Test
    void CategoryRepository_UpdateCategory_ReturnNotNullCategory(){

        Category category = Category.builder()
                .name("Topmate")
                .type(CategoryType.Income)
                .build();

        categoryRepo.save(category);
        Category savedCategory = categoryRepo.findById(category.getId()).get();
        savedCategory.setName("Freelancing");

        Category updatedCategory = categoryRepo.save(savedCategory);
        assertThat(updatedCategory).isNotNull();
    }

    @Test
    void CategoryRepository_DeleteCategoryById_ReturnCategoryIsEmpty() {

        Category category = Category.builder()
                .name("Medicine Bills")
                .type(CategoryType.Expense)
                .build();

        categoryRepo.save(category);
        categoryRepo.deleteById(category.getId());

        Optional<Category> result = categoryRepo.findById(category.getId());
        assertThat(result).isEmpty();
    }

}
