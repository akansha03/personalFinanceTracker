package com.finance.personalTracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private UUID transactionId;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private CategoryType type;

    private String categoryName;

    @JsonIgnore
    private Long categoryId;

    private Account account;

    private String note;

    private String imageUrl;

    private String description;

}
