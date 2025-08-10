package com.finance.personalTracker.dto;

import com.finance.personalTracker.enums.CategoryType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CategoryDTO {
    private String name;
    private CategoryType type;

}
