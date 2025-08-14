package com.finance.personalTracker.services.impl;

import com.finance.personalTracker.dto.TransactionDTO;
import com.finance.personalTracker.entity.Transaction;
import com.finance.personalTracker.enums.CategoryType;
import com.finance.personalTracker.exception.ResourceNotFoundException;
import com.finance.personalTracker.repositories.TransactionRepo;
import com.finance.personalTracker.services.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TransactionDTO addTransaction(TransactionDTO transactionDTO) {

        Transaction income = modelMapper.map(transactionDTO, Transaction.class);
        Transaction savedIncome = this.transactionRepo.save(income);
        return modelMapper.map(savedIncome, TransactionDTO.class);
    }

    @Override
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO, UUID transactionId) {

        Transaction income = this.transactionRepo.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction ID", transactionId.toString()));
        income.setAmount(transactionDTO.getAmount());
        income.setCategory(transactionDTO.getCategory());
        income.setAccount(transactionDTO.getAccount());

        income.setNote(transactionDTO.getNote());
        income.setImageUrl(transactionDTO.getImageUrl());
        income.setDescription(transactionDTO.getDescription());

        Transaction updatedIncome = transactionRepo.save(income);
        return modelMapper.map(updatedIncome, TransactionDTO.class);
    }

    @Override
    public void deleteTransaction(UUID id) {

        Transaction transaction = this.transactionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UUID", id.toString()));
        this.transactionRepo.delete(transaction);
    }

    @Override
    public BigDecimal getTotalAmount(LocalDateTime date, CategoryType type) {

        if(type == null) {
            BigDecimal incomeTotal = getSum(mapEntitiesToDTO(transactionRepo.findByDateAndType(date, CategoryType.Income)));
            BigDecimal expenseTotal = getSum(mapEntitiesToDTO(transactionRepo.findByDateAndType(date, CategoryType.Expense)));
            return incomeTotal.subtract(expenseTotal);
        }
        List<TransactionDTO> transactions = mapEntitiesToDTO(transactionRepo.findByDateAndType(date, type));
        return getSum(transactions);
    }

    private List<TransactionDTO> mapEntitiesToDTO(List<Transaction> transactions) {

        if(transactions == null || transactions.isEmpty()){
            return Collections.emptyList();
        }
        return transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .toList();
    }

    private BigDecimal getSum(List<TransactionDTO> transactions) {

        return transactions.stream().map(TransactionDTO::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
