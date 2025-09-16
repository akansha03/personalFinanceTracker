package com.finance.personalTracker.repositories;

import com.finance.personalTracker.entity.Transaction;
import com.finance.personalTracker.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.*;

public interface TransactionRepo extends JpaRepository<Transaction, UUID> {

    Transaction findByTransactionId(UUID id);

    List<Transaction> findByDateAndType(LocalDate date, CategoryType type);

    List<Transaction> findByCategoryType(CategoryType type);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.category WHERE t.transactionId = :id")
    Optional<Transaction> findByIdWithCategory(@Param("id") UUID id);
}
