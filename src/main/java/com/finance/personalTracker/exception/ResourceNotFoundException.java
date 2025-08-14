package com.finance.personalTracker.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    String resourceName;
    String fieldName;

    public ResourceNotFoundException(String resourceName, String fieldName) {
        super(String.format("Resource %s not found with value : %s", resourceName, fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;

    }

}
