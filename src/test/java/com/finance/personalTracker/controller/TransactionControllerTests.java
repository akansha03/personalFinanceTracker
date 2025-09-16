package com.finance.personalTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.personalTracker.dto.*;
import com.finance.personalTracker.enums.Account;
import com.finance.personalTracker.enums.CategoryType;
import com.finance.personalTracker.services.TransactionService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private TransactionRequestDTO transactionDTO;

    private UpdateTransactionDTO updateTransactionDTO;

    @BeforeEach
    public void init() {
        transactionDTO = TransactionRequestDTO.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(5000))
                .type(CategoryType.Income)
                .categoryName("Salary")
                .account(Account.BANK_ACCOUNTS)
                .note("Update Note")
                .imageUrl("image.png")
                .description("Update Description")
                .build();

        updateTransactionDTO = UpdateTransactionDTO.builder()
                .amount(BigDecimal.valueOf(4000))
                .account(Account.CARD)
                .note("New Note")
                .build();
    }

    @Test
    void TransactionController_AddTransaction_ReturnTransactionDTO() throws Exception {

        TransactionResponseDTO responseDTO = TransactionResponseDTO.builder()
                .transactionId(UUID.randomUUID())
                .date(transactionDTO.getDate())
                .amount(transactionDTO.getAmount())
                .categoryDTO(
                        CategoryResponseDTO.builder()
                                .id(1L)
                                .name(transactionDTO.getCategoryName())
                                .type(transactionDTO.getType())
                                .build()
                )
                .account(transactionDTO.getAccount())
                .note(transactionDTO.getNote())
                .imageUrl(transactionDTO.getImageUrl())
                .description(transactionDTO.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        given(transactionService.addTransaction(any(TransactionRequestDTO.class)))
                .willReturn(responseDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)));

        result.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryDTO.type", CoreMatchers.is(CategoryType.Income.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryDTO.name", CoreMatchers.is("Salary")));
    }

    @Test
    void TransactionController_UpdateTransaction_ReturnUpdateDTO() throws Exception{
        UUID transactionID = UUID.randomUUID();
        given(transactionService.updateTransaction(ArgumentMatchers.any(UpdateTransactionDTO.class), eq(transactionID)))
                .willReturn(updateTransactionDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/api/transactions/"+ transactionID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTransactionDTO)));

        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.note", CoreMatchers.is("New Note")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", CoreMatchers.is(4000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account", CoreMatchers.is("CARD")));
    }

    @Test
    void TransactionController_DeleteTransaction_ReturnNothing() throws Exception{
        UUID id = UUID.randomUUID();
        doNothing().when(transactionService).deleteTransaction(id);
        ResultActions result = mockMvc.perform(delete("/api/transactions/"+ id)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void TransactionController_GetAmount_ReturnTotalAmount() throws Exception{

        AmountDTO amountDTO = AmountDTO.builder().amount(BigDecimal.valueOf(1000)).build();

        when(transactionService.getTotalAmount(LocalDate.now(), CategoryType.Income)).thenReturn(amountDTO);

        ResultActions response = mockMvc.perform(get("/api/transactions/total")
                .contentType(MediaType.APPLICATION_JSON)
                .param("date", String.valueOf(LocalDate.now()))
                .param("type", String.valueOf(CategoryType.Income)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", CoreMatchers.is(1000)));
    }
}
