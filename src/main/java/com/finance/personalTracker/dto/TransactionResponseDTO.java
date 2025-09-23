package com.finance.personalTracker.dto;


import com.finance.personalTracker.enums.Account;
import com.finance.personalTracker.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {

    private UUID transactionId;
    private LocalDate date;
    private BigDecimal amount;
    /*private CategoryType type;

    private String categoryName;*/
    private CategoryResponseDTO categoryDTO;
    private Account account;
    private String note;
    private String imageUrl;
    private String description;

    private LocalDateTime createdAt;
}