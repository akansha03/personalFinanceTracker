package com.finance.personalTracker.dto;

import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.enums.Account;
import com.finance.personalTracker.enums.CategoryType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class TransactionDTO {

    private Double amount;

    private CategoryType type;

    @ManyToOne
    private Category categoryName;

    @Enumerated(EnumType.STRING)
    private Account account;

    private String note;

    private String image_url;

    private String description;
}
