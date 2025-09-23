package com.finance.personalTracker.dto;

import com.finance.personalTracker.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private CategoryType type;
}
