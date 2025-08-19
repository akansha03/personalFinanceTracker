package com.finance.personalTracker.services;

import com.finance.personalTracker.dto.TransactionDTO;
import com.finance.personalTracker.enums.CategoryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface TransactionService {

    TransactionDTO addTransaction(TransactionDTO transactionDTO);

    TransactionDTO updateTransaction(TransactionDTO transactionDTO, UUID transactionId);

    void deleteTransaction(UUID id);

    BigDecimal getTotalAmount(LocalDate date, CategoryType type);
}
