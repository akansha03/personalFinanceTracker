package com.finance.personalTracker.dto;

import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.enums.Account;
import com.finance.personalTracker.enums.CategoryType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionDTO {

    private UUID transactionId;

    private LocalDateTime date;

    private BigDecimal amount;

    private CategoryType type;

    private Category category;

    private Account account;

    private String note;

    private String imageUrl;

    private String description;
}
