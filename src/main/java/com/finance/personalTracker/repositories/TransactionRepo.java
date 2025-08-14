package com.finance.personalTracker.repositories;

import com.finance.personalTracker.entity.Transaction;
import com.finance.personalTracker.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.*;

public interface TransactionRepo extends JpaRepository<Transaction, UUID> {


    List<Transaction> findByDateAndType(LocalDateTime date, CategoryType type);
}
