package com.finance.personalTracker.repository;

import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.entity.Transaction;
import com.finance.personalTracker.enums.Account;
import com.finance.personalTracker.enums.CategoryType;
import com.finance.personalTracker.repositories.CategoryRepo;
import com.finance.personalTracker.repositories.TransactionRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTests {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Test
    void TransactionRepository_AddTransaction_ReturnANewTransactions() {

        // Arrange Act Assert

        Category category = Category.builder().id(1L).name("Brokerage").type(CategoryType.Income).build();
        Category savedCategory = categoryRepo.save(category);

        Transaction transaction = Transaction.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(3000))
                .type(CategoryType.Income)
                .category(savedCategory)
                .account(Account.CASH)
                .note("Brokerage amount is required")
                .imageUrl("invoice.png")
                .description("Monthly brokerage amount updated")
                .build();

        Transaction result = transactionRepo.save(transaction);
        assertThat(result).isNotNull();
    }

    @Test
    void TransactionRepository_UpdateTransaction_ReturnUpdatedTransaction() {

        Category category = Category.builder().id(1L).name("Vacation").type(CategoryType.Expense).build();
        Category savedCategory = categoryRepo.save(category);

        Transaction transaction = Transaction.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(2000))
                .type(CategoryType.Expense)
                .category(savedCategory)
                .account(Account.CARD)
                .note("Himalaya Retreat")
                .imageUrl("/vacation.png")
                .description("Yearly Himalaya Vacation")
                .build();

        transactionRepo.save(transaction);
        Transaction transaction1 = transactionRepo.findByDateAndType(LocalDate.now(), CategoryType.Expense).get(0);
        transaction1.setNote("Beach Vacation");
        transaction1.setCreatedAt(LocalDateTime.now());

        Transaction updatedTransaction = transactionRepo.save(transaction1);
        assertThat(updatedTransaction).isNotNull();
    }

    @Test
    void TransactionRepository_GetAll_ReturnAllTransactions() {

        Category category = Category.builder().id(1L).name("Vacation").type(CategoryType.Expense).build();
        Category savedCategory = categoryRepo.save(category);

        Transaction transaction1 = Transaction.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(2000))
                .type(CategoryType.Expense)
                .category(savedCategory)
                .account(Account.CARD)
                .note("Himalaya Retreat")
                .imageUrl("/vacation.png")
                .description("Yearly Himalaya Vacation")
                .build();

        Transaction transaction2 = Transaction.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(2000))
                .type(CategoryType.Expense)
                .category(savedCategory)
                .account(Account.CARD)
                .note("Himalaya Retreat")
                .imageUrl("/vacation.png")
                .description("Yearly Himalaya Vacation")
                .build();

        transactionRepo.save(transaction1);
        transactionRepo.save(transaction2);

        List<Transaction> transactions = transactionRepo.findAll();
        assertThat(transactions.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void TransactionRepository_GetTransactionByID_ReturnTransaction() {

        Category category = Category.builder().id(1L).name("Vacation").type(CategoryType.Expense).build();
        category.setType(CategoryType.Expense);

        Category savedCategory = categoryRepo.save(category);

        Transaction transaction = Transaction.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(2000))
                .type(CategoryType.Expense)
                .category(savedCategory)
                .account(Account.CARD)
                .note("Himalaya Retreat")
                .imageUrl("/vacation.png")
                .description("Yearly Himalaya Vacation")
                .build();

        transactionRepo.save(transaction);
        Transaction savedTransaction = transactionRepo.findByTransactionId(transaction.getTransactionId());
        assertThat(savedTransaction).isNotNull();
    }

    @Test
    void TransactionRepository_FindAllTransactionByType_ReturnListOfTransactions() {

        Category category1 = Category.builder().id(1L).name("Water Adventure").type(CategoryType.Expense).build();
        Category savedCategory1 = categoryRepo.save(category1);

        Category category2 = Category.builder().id(2L).name("Rental Gears").type(CategoryType.Income).build();
        Category savedCategory2 = categoryRepo.save(category2);

        Transaction transaction1 = Transaction.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(1000))
                .type(CategoryType.Expense)
                .category(savedCategory1)
                .account(Account.CASH)
                .build();
        transactionRepo.save(transaction1);

        Transaction transaction2 = Transaction.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(2000))
                .type(CategoryType.Income)
                .category(savedCategory2)
                .account(Account.BANK_ACCOUNTS)
                .build();
        transactionRepo.save(transaction2);

        List<Transaction> incomeTransactions = transactionRepo.findByCategoryType(CategoryType.Income);
        assertThat(incomeTransactions).isNotNull();
        incomeTransactions.forEach(t -> assertThat(t.getType()).isEqualTo(CategoryType.Income));

        List<Transaction> expenseTransactions = transactionRepo.findByCategoryType(CategoryType.Expense);
        assertThat(expenseTransactions).isNotNull();
        expenseTransactions.forEach(expense -> assertThat(expense.getType()).isEqualTo(CategoryType.Expense));
    }

    @Test
    void TransactionRepository_DeleteTransaction_ReturnTransactionIsEmpty() {

        Category category = Category.builder().id(2L).name("Rental Gears").type(CategoryType.Income).build();
        Category savedCategory = categoryRepo.save(category);

        Transaction transaction = Transaction.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(1000))
                .type(CategoryType.Expense)
                .category(savedCategory)
                .account(Account.CASH)
                .build();
        transactionRepo.save(transaction);
        transactionRepo.deleteById(transaction.getTransactionId());

        Transaction result = transactionRepo.findByTransactionId(transaction.getTransactionId());
        assertThat(result).isNull();
    }


}
