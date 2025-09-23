package com.finance.personalTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class AmountDTO {
    private BigDecimal amount;
}
