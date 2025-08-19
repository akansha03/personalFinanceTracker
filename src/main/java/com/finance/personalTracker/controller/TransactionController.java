package com.finance.personalTracker.controller;

import com.finance.personalTracker.dto.TransactionDTO;
import com.finance.personalTracker.enums.CategoryType;
import com.finance.personalTracker.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> addTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO newTransaction = this.transactionService.addTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable UUID uuid,
            @Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO updateTransaction = this.transactionService.updateTransaction(transactionDTO, uuid);
        return ResponseEntity.ok(updateTransaction);
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getAmount(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @RequestParam(value = "type", required = false) CategoryType type) {
        BigDecimal amount = this.transactionService.getTotalAmount(date, type);
        return new ResponseEntity<BigDecimal>(amount, HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID uuid) {
        this.transactionService.deleteTransaction(uuid);
        return ResponseEntity.noContent().build();
    }
}
