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


}
