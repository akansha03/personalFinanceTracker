package com.finance.personalTracker.controller;

import com.finance.personalTracker.dto.TransactionDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @PostMapping("/income")
    public void addIncome(@Valid @RequestBody TransactionDTO transactionDTO){

    }

    @PostMapping("/expense")
    public void addExpense(@Valid @RequestBody TransactionDTO transactionDTO){

    }

    @PutMapping("/{uuid}")
    public void updateIncome_Expense(@Valid @RequestBody TransactionDTO transactionDTO, Integer uuid){

    }

    @GetMapping
    public void getSummaryTransactions(@RequestParam String userID) {

    }

}
