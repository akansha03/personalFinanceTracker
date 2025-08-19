package com.finance.personalTracker.dto;

import com.finance.personalTracker.entity.Category;
import com.finance.personalTracker.enums.Account;
import com.finance.personalTracker.enums.CategoryType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionDTO {

    private UUID transactionId;

    @NotNull(message = "Date cannot be null")
    private LocalDateTime date;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private CategoryType type;

    private String categoryName;

    private Long categoryId;

    private Account account;

    private String note;

    private String imageUrl;

    private String description;

    public TransactionDTO() {
    }

    public TransactionDTO(UUID transactionId,
                          LocalDateTime date, BigDecimal amount, CategoryType type, String categoryName, Long categoryId,
                          Account account, String note, String imageUrl, String description) {
        this.transactionId = transactionId;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.categoryName = categoryName;

        this.categoryId = categoryId;
        this.account = account;
        this.note = note;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
