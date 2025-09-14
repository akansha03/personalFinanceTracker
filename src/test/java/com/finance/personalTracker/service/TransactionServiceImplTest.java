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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

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

    private TransactionMapperImpl transactionMapper;

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private CategoryRepo categoryRepo;

    private TransactionDTO transactionDTO;

    private Category category;

    @BeforeEach
    void setUp() {
        transactionMapper = new TransactionMapperImpl();
        transactionService = new TransactionServiceImpl(transactionRepo, transactionMapper, categoryRepo);

        transactionDTO = TransactionDTO.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(5000))
                .type(CategoryType.Expense)
                .categoryId(1L)
                .account(Account.CASH)
                .note("Dog Grooming")
                .description("Monthly Dog Grooming")
                .build();

        category = Category.builder()
                .id(1L)
                .name("PETS")
                .type(CategoryType.Expense)
                .build();

    }

    @Test
    void TransactionService_AddATransaction_ReturnTransactionDTO() {

        // Arrange
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepo.save(any(Transaction.class)))
                .thenAnswer(invocation -> {
                    Transaction transactionToSave = invocation.getArgument(0);
                    if (transactionToSave.getTransactionId() == null) {
                        transactionToSave.setTransactionId(UUID.randomUUID());
                    }
                    return transactionToSave;
                });

        // Act
        TransactionDTO result = transactionService.addTransaction(transactionDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(5000));
        assertThat(result.getNote()).isEqualTo("Dog Grooming");
        assertThat(result.getDescription()).isEqualTo("Monthly Dog Grooming");
        assertThat(result.getAccount()).isEqualTo(Account.CASH);
        assertThat(result.getType()).isEqualTo(CategoryType.Expense);
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

        when(transactionRepo.findById(uuid)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepo.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateTransactionDTO result = transactionService.updateTransaction(updateTransactionDTO, uuid);

        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(3000));
        assertThat(result.getAccount()).isEqualTo(Account.BANK_ACCOUNTS);

        assertThat(result.getNote()).isEqualTo("Update Note");
        assertThat(result.getDescription()).isEqualTo("Updated Description");

        verify(transactionRepo).findById(uuid);
        verify(transactionRepo).save(existingTransaction);
    }

    @Test
    void TransactionService_DeleteTransaction_ReturnNull() {

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
        transactionService.deleteTransaction(uuid);

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

        when(transactionRepo.findByDateAndType(date, CategoryType.Income)).thenReturn(List.of(income));
        when(transactionRepo.findByDateAndType(date, CategoryType.Expense)).thenReturn(List.of(expense));

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

        when(transactionRepo.findByDateAndType(date, CategoryType.Income)).thenReturn(List.of(income));

        AmountDTO result = transactionService.getTotalAmount(date, CategoryType.Income);
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    void TransactionService_GetTotalAmount_WhenTypeExpense_ReturnExpenseTotal() {

        LocalDate date = LocalDate.now();

        Transaction expense = Transaction.builder()
                .amount(BigDecimal.valueOf(1000))
                .type(CategoryType.Expense)
                .date(date)
                .build();

        when(transactionRepo.findByDateAndType(date, CategoryType.Expense)).thenReturn(List.of(expense));
        AmountDTO result = transactionService.getTotalAmount(date, CategoryType.Expense);
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(1000));
    }

}
