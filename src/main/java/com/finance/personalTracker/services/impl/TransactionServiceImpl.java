package com.finance.personalTracker.services.impl;

import com.finance.personalTracker.dto.AmountDTO;
import com.finance.personalTracker.dto.TransactionDTO;
import com.finance.personalTracker.dto.TransactionMapper;
import com.finance.personalTracker.dto.UpdateTransactionDTO;
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
    public TransactionDTO addTransaction(TransactionDTO transactionDTO) {

       /* Transaction transaction = new Transaction();

        transaction.setDate(transactionDTO.getDate());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());

        transaction.setAccount(transactionDTO.getAccount());
        transaction.setNote(transactionDTO.getNote());
        transaction.setImageUrl(transactionDTO.getImageUrl());
        transaction.setDescription(transactionDTO.getDescription());*/

        Transaction transaction = transactionMapper.toEntity(transactionDTO);

        if (transactionDTO.getCategoryId() != null) {
            Category category = categoryRepo.findById(transactionDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", transactionDTO.getCategoryId().toString()));
            transaction.setCategory(category);
        } else if (transactionDTO.getCategoryName() != null) {
            Category category = categoryRepo.findByNameAndType(transactionDTO.getCategoryName(),
                    transactionDTO.getType()).orElseThrow(() -> new ResourceNotFoundException("Category", transactionDTO.getCategoryName()));
            transaction.setCategory(category);
        } else {
            throw new IllegalArgumentException("Either categoryId or categoryName should be provided");
        }
        Transaction savedTransaction = this.transactionRepo.save(transaction);
        return transactionMapper.toDTO(savedTransaction);
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

  /*  private TransactionDTO convertToDTO(Transaction transaction) {

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transaction.getTransactionId());
        transactionDTO.setDate(transaction.getDate());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setType(transaction.getType());

        transactionDTO.setAccount(transaction.getAccount());
        transactionDTO.setNote(transaction.getNote());
        transactionDTO.setImageUrl(transaction.getImageUrl());
        transactionDTO.setDescription(transaction.getDescription());

        if (transaction.getCategory() != null) {
            transactionDTO.setId(transaction.getCategory().getId());
            transactionDTO.setCategoryName(transaction.getCategory().getName());
        }

        return transactionDTO;
    }

    private UpdateTransactionDTO convertToUpdateDTO(Transaction transaction) {

        UpdateTransactionDTO transactionDTO = new UpdateTransactionDTO();
        transactionDTO.setAmount(transaction.getAmount());

        transactionDTO.setAccount(transaction.getAccount());
        transactionDTO.setNote(transaction.getNote());

        transactionDTO.setImageUrl(transaction.getImageUrl());
        transactionDTO.setDescription(transaction.getDescription());
        return transactionDTO;
    }*/
}
