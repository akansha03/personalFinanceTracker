package com.finance.personalTracker.dto;

import com.finance.personalTracker.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



public class CategoryDTO {

    @NotBlank
    @Size(min = 2, message = "Name of the category should be more than 2 letter")
    private String name;

    @NotNull(message = "type cannot be null")
    private CategoryType type;

    public CategoryDTO() {
    }

    public CategoryDTO(String name, CategoryType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    public @NotBlank @Size(min = 2, message = "Name of the category should not be more than 2") String getName() {
        return name;
    }

    public void setName(@NotBlank @Size(min = 2, message = "Name of the category should not be more than 2") String name) {
        this.name = name;
    }

    public @NotNull(message = "type cannot be null") CategoryType getType() {
        return type;
    }

    public void setType(@NotNull(message = "type cannot be null") CategoryType type) {
        this.type = type;
    }
}
