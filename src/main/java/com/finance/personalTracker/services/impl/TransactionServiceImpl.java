package com.finance.personalTracker.services.impl;

import com.finance.personalTracker.dto.*;
import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.entity.Transaction;

import com.finance.personalTracker.enums.CategoryType;
import com.finance.personalTracker.exception.ResourceNotFoundException;
import com.finance.personalTracker.repositories.CategoryRepo;
import com.finance.personalTracker.repositories.TransactionRepo;
import com.finance.personalTracker.services.TransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
    private final TransactionMapper transactionMapper;
    private final CategoryRepo categoryRepo;

    public TransactionServiceImpl(TransactionRepo transactionRepo,
                                  TransactionMapper transactionMapper,
                                  CategoryRepo categoryRepo) {
        this.transactionRepo = transactionRepo;
        this.transactionMapper = transactionMapper;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public TransactionResponseDTO addTransaction(TransactionRequestDTO transactionDTO) {

        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        Category category = categoryRepo.findByNameAndType(transactionDTO.getCategoryName(),
                transactionDTO.getType()).orElseThrow(() -> new ResourceNotFoundException("Category", transactionDTO.getCategoryName()));
        transaction.setCategory(category);
        Transaction savedTransaction = transactionRepo.save(transaction);
        return transactionMapper.toResponseDTO(savedTransaction);
    }

    @Override
    public UpdateTransactionDTO updateTransaction(UpdateTransactionDTO transactionDTO, UUID transactionId) {

        Transaction transaction = this.transactionRepo.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction ID", transactionId.toString()));

        transaction.setAmount(transactionDTO.getAmount());
        transaction.setAccount(transactionDTO.getAccount());

        transaction.setNote(transactionDTO.getNote());
        transaction.setImageUrl(transactionDTO.getImageUrl());
        transaction.setDescription(transactionDTO.getDescription());

        Transaction updatedIncome = transactionRepo.save(transaction);
        return transactionMapper.toUpdateDTO(updatedIncome);
    }

    @Override
    public void deleteTransaction(UUID id) {
        Transaction transaction = this.transactionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UUID", id.toString()));
        this.transactionRepo.delete(transaction);
    }

    @Override
    public AmountDTO getTotalAmount(LocalDate date, CategoryType type) {
        if (type == null) {
            BigDecimal incomeTotal = getSum(mapEntitiesToDTO(transactionRepo.findByDateAndType(date, CategoryType.Income)));
            BigDecimal expenseTotal = getSum(mapEntitiesToDTO(transactionRepo.findByDateAndType(date, CategoryType.Expense)));
            return new AmountDTO(incomeTotal.subtract(expenseTotal));
        }

        List<TransactionDTO> transactions = mapEntitiesToDTO(transactionRepo.findByDateAndType(date, type));
        return new AmountDTO(getSum(transactions));
    }

    private List<TransactionDTO> mapEntitiesToDTO(List<Transaction> transactions) {

        if (transactions == null || transactions.isEmpty()) {
            return Collections.emptyList();
        }
        return transactions.stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    private BigDecimal getSum(List<TransactionDTO> transactions) {
        return transactions == null
                ? BigDecimal.ZERO
                : transactions.stream().map(TransactionDTO::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        }
}
