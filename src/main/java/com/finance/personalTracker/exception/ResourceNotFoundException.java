package com.finance.personalTracker.exception;

public class ResourceNotFoundException extends RuntimeException {

    String resourceName;
    String fieldName;

    public ResourceNotFoundException(String resourceName, String fieldName) {
        super(String.format("Resource %s not found with value : %s", resourceName, fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;

    }
}
