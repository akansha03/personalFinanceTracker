package com.finance.personalTracker.dto;

import com.finance.personalTracker.enums.CategoryType;
import lombok.Data;

@Data
public class CategoryDTO {
    private String name;
    private CategoryType type;

}
