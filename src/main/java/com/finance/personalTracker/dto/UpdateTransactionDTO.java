package com.finance.personalTracker.dto;
import com.finance.personalTracker.enums.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionDTO {

    private BigDecimal amount;

    private Account account;

    private String note;

    private String imageUrl;

    private String description;

    /*public UpdateTransactionDTO() {

    }

    public UpdateTransactionDTO(BigDecimal amount,
                                Account account,
                                String note,
                                String imageUrl,
                                String description)
    {
        this.amount = amount;
        this.account = account;
        this.note = note;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
    }*/
}
