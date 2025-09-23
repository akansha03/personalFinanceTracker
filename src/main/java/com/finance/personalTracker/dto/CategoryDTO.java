package com.finance.personalTracker.dto;

import com.finance.personalTracker.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    @NotBlank(message = "Category name cannot be null or empty")
    @Size(min = 2, max =50, message = "Category name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Category type cannot be null")
    private CategoryType type;
}
