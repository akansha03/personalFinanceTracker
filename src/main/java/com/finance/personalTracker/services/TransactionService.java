package com.finance.personalTracker.services;

import com.finance.personalTracker.dto.AmountDTO;
import com.finance.personalTracker.dto.TransactionDTO;
import com.finance.personalTracker.dto.UpdateTransactionDTO;
import com.finance.personalTracker.enums.CategoryType;

import java.time.LocalDate;
import java.util.UUID;

public interface TransactionService {

    TransactionDTO addTransaction(TransactionDTO transactionDTO);

    UpdateTransactionDTO updateTransaction(UpdateTransactionDTO transactionDTO, UUID transactionId);

    void deleteTransaction(UUID id);

    AmountDTO getTotalAmount(LocalDate date, CategoryType type);
}
