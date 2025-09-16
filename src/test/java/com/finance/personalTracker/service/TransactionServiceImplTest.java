package com.finance.personalTracker.service;

import com.finance.personalTracker.dto.*;
import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.entity.Transaction;
import com.finance.personalTracker.enums.Account;
import com.finance.personalTracker.enums.CategoryType;
import com.finance.personalTracker.repositories.CategoryRepo;
import com.finance.personalTracker.repositories.TransactionRepo;

import com.finance.personalTracker.services.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private CategoryRepo categoryRepo;

    private TransactionRequestDTO transactionDTO;
    private Category category;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl(transactionRepo, transactionMapper, categoryRepo);

        transactionDTO = TransactionRequestDTO.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(5000))
                .type(CategoryType.Expense)
                .categoryName("PETS")
                .account(Account.CASH)
                .note("Dog Grooming")
                .description("Monthly Dog Grooming")
                .build();

        category = Category.builder()
                .id(1L)
                .name("PETS")
                .type(CategoryType.Expense)
                .build();

        transaction = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(5000))
                .type(CategoryType.Expense)
                .category(category)
                .account(Account.CASH)
                .note("Golden Grooming Days")
                .description("Monthly Grooming Days")
                .build();
    }

    @Test
    void TransactionService_AddATransaction_ReturnTransactionDTO() {

        // Arrange
        TransactionResponseDTO expectedResponse = TransactionResponseDTO.builder()
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .note(transaction.getNote())
                .description(transaction.getDescription())
                .account(transaction.getAccount())
                .build();

        when(transactionMapper.toEntity(transactionDTO)).thenReturn(transaction);
        when(categoryRepo.findByNameAndType("PETS", CategoryType.Expense)).thenReturn(Optional.of(category));
        when(transactionRepo.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toResponseDTO(transaction)).thenReturn(expectedResponse);

        // Act
        TransactionResponseDTO result = transactionService.addTransaction(transactionDTO);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(5000));
        assertThat(result.getNote()).isEqualTo("Golden Grooming Days");
        assertThat(result.getDescription()).isEqualTo("Monthly Grooming Days");
        assertThat(result.getAccount()).isEqualTo(Account.CASH);

        verify(transactionMapper).toEntity(transactionDTO);
        verify(categoryRepo).findByNameAndType("PETS", CategoryType.Expense);
        verify(transactionRepo).save(any(Transaction.class));
        verify(transactionMapper).toResponseDTO(transaction);
    }

    @Test
    void TransactionService_UpdateTransaction_ReturnTransactionDTO() {

        // Arrange
        UUID uuid = UUID.randomUUID();
        Transaction existingTransaction = Transaction.builder()
                .transactionId(uuid)
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(5000))
                .type(CategoryType.Expense)
                .category(category)
                .account(Account.CASH)
                .note("Old Note")
                .description("Old Description")
                .build();

        UpdateTransactionDTO updateTransactionDTO = UpdateTransactionDTO.builder()
                .amount(BigDecimal.valueOf(3000))
                .account(Account.BANK_ACCOUNTS)
                .note("Update Note")
                .imageUrl("image.png")
                .description("Updated Description")
                .build();

        UpdateTransactionDTO expectedResponse = UpdateTransactionDTO.builder()
                        .amount(BigDecimal.valueOf(3000))
                        .account(Account.BANK_ACCOUNTS)
                        .note("Update Note")
                        .imageUrl("image.png")
                        .description("Updated Description")
                        .build();

        when(transactionRepo.findById(uuid)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepo.save(any(Transaction.class))).thenReturn(existingTransaction);
        when(transactionMapper.toUpdateDTO(existingTransaction)).thenReturn(expectedResponse);

        // Act
        UpdateTransactionDTO result = transactionService.updateTransaction(updateTransactionDTO, uuid);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(3000));
        assertThat(result.getAccount()).isEqualTo(Account.BANK_ACCOUNTS);
        assertThat(result.getNote()).isEqualTo("Update Note");
        assertThat(result.getDescription()).isEqualTo("Updated Description");

        verify(transactionRepo).findById(uuid);
        verify(transactionRepo).save(existingTransaction);
        verify(transactionMapper).toUpdateDTO(existingTransaction);
    }

    @Test
    void TransactionService_DeleteTransaction_ReturnNull() {
        // Arrange

        UUID uuid = UUID.randomUUID();
        Transaction existingTransaction = Transaction.builder()
                .transactionId(uuid)
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(5000))
                .type(CategoryType.Expense)
                .category(category)
                .account(Account.CASH)
                .note("Old Note")
                .description("Old Description")
                .build();

        when(transactionRepo.findById(uuid)).thenReturn(Optional.of(existingTransaction));
        // Act
        transactionService.deleteTransaction(uuid);

        // Assert
        verify(transactionRepo).findById(uuid);
        verify(transactionRepo).delete(existingTransaction);
    }

    @Test
    void TransactionService_GetTotalAmount_WhenTypeNull_ReturnsNetAmount() {

        LocalDate date = LocalDate.now();
        Transaction income = Transaction.builder()
                .amount(BigDecimal.valueOf(1000))
                .type(CategoryType.Income)
                .date(date)
                .build();

        Transaction expense = Transaction.builder()
                .amount(BigDecimal.valueOf(600))
                .type(CategoryType.Expense)
                .date(date)
                .build();

        TransactionDTO incomeDTO = TransactionDTO.builder()
                .amount(BigDecimal.valueOf(1000))
                .type(CategoryType.Income)
                .date(date)
                .build();

        TransactionDTO expenseDTO = TransactionDTO.builder()
                .amount(BigDecimal.valueOf(600))
                .type(CategoryType.Expense)
                .date(date)
                .build();

        when(transactionRepo.findByDateAndType(date, CategoryType.Income)).thenReturn(List.of(income));
        when(transactionRepo.findByDateAndType(date, CategoryType.Expense)).thenReturn(List.of(expense));
        when(transactionMapper.toDTO(expense)).thenReturn(expenseDTO);
        when(transactionMapper.toDTO(income)).thenReturn(incomeDTO);

        AmountDTO result = transactionService.getTotalAmount(date, null);
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(400));
    }

    @Test
    void TransactionService_GetTotalAmount_WhenTypeIncome_ReturnIncomeTotal() {

        LocalDate date = LocalDate.now();
        Transaction income = Transaction.builder()
                .amount(BigDecimal.valueOf(1000))
                .type(CategoryType.Income)
                .date(date)
                .build();

        TransactionDTO incomeDTO = TransactionDTO.builder()
                .amount(BigDecimal.valueOf(1000))
                .type(CategoryType.Income)
                .date(date)
                .build();

        when(transactionRepo.findByDateAndType(date, CategoryType.Income)).thenReturn(List.of(income));
        when(transactionMapper.toDTO(income)).thenReturn(incomeDTO);

        // Act
        AmountDTO result = transactionService.getTotalAmount(date, CategoryType.Income);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    void TransactionService_GetTotalAmount_WhenTypeExpense_ReturnExpenseTotal() {

        // Arrange
        LocalDate date = LocalDate.now();
        Transaction expense = Transaction.builder()
                .amount(BigDecimal.valueOf(1000))
                .type(CategoryType.Expense)
                .date(date)
                .build();

        TransactionDTO expenseDTO = TransactionDTO.builder()
                .amount(BigDecimal.valueOf(1000))
                .type(CategoryType.Expense)
                .date(date)
                .build();

        when(transactionRepo.findByDateAndType(date, CategoryType.Expense)).thenReturn(List.of(expense));
        when(transactionMapper.toDTO(expense)).thenReturn(expenseDTO);
        // Act
        AmountDTO result = transactionService.getTotalAmount(date, CategoryType.Expense);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(1000));
    }

}

