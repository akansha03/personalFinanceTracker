package com.finance.personalTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.personalTracker.dto.CategoryDTO;
import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.enums.CategoryType;
import com.finance.personalTracker.services.CategoryService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDTO categoryDTO;

    @BeforeEach
    public void init() {
       categoryDTO = CategoryDTO.builder()
                .name("Accessories").type(CategoryType.Expense).build();
    }

    @Test
    void CategoryController_AddCategory_ReturnCreatedCategory() throws Exception {

        given(categoryService.createCategory(ArgumentMatchers.any()))
                .willAnswer(invocation -> invocation.getArgument(0));

        ResultActions result = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)));

        result.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(categoryDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is(categoryDTO.getType().toString())));

    }

    @Test
    void CategoryController_GetAllCategories_ReturnCategoryDTO() throws Exception{

        when(categoryService.getAllCategories()).thenReturn(List.of(categoryDTO));

        ResultActions response = mockMvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Accessories"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("Expense"));
    }

    @Test
    void CategoryController_GetCategoryByIncome_ReturnCategoryDTO() throws Exception{

        CategoryDTO incomeDTO = CategoryDTO.builder().name("Salary").type(CategoryType.Income).build();
        when(categoryService.getCategoriesByType(CategoryType.Income)).thenReturn(List.of(incomeDTO));

        ResultActions response = mockMvc.perform(get("/api/categories/"+ CategoryType.Income)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Salary"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("Income"));
    }

    @Test
    void CategoryController_GetCategoryByExpense_ReturnCategoryDTO() throws Exception {

        CategoryDTO expenseDTO = CategoryDTO.builder().name("Rent").type(CategoryType.Expense).build();
        when(categoryService.getCategoriesByType(CategoryType.Expense)).thenReturn(List.of(expenseDTO));

        ResultActions response = mockMvc.perform(get("/api/categories/"+ CategoryType.Expense)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Rent"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("Expense"));
    }


}
