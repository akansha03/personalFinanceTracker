package com.finance.personalTracker.services;

import com.finance.personalTracker.dto.*;
import com.finance.personalTracker.enums.CategoryType;

import java.time.LocalDate;
import java.util.UUID;

public interface TransactionService {

    TransactionResponseDTO addTransaction(TransactionRequestDTO transactionDTO);

    UpdateTransactionDTO updateTransaction(UpdateTransactionDTO transactionDTO, UUID transactionId);

    void deleteTransaction(UUID id);

    AmountDTO getTotalAmount(LocalDate date, CategoryType type);
}
