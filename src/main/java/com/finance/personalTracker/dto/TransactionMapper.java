package com.finance.personalTracker.dto;

import com.finance.personalTracker.entity.Transaction;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDTO toDTO(Transaction transaction);

    UpdateTransactionDTO toUpdateDTO(Transaction transaction);

    Transaction toEntity(TransactionDTO dto);

}
