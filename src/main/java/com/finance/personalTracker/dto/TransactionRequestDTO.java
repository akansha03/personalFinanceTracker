package com.finance.personalTracker.dto;

import com.finance.personalTracker.enums.Account;
import com.finance.personalTracker.enums.CategoryType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
    @NotNull
    private LocalDate date;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private CategoryType type;

    @NotNull
    private String categoryName;

    @NotNull
    private Account account;

    private String note;
    private String imageUrl;
    private String description;
}