package com.estateguru.minibank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActionAccountDto {
    private String accountNumber;
    private BigDecimal amount;
    private String targetAccountNumber;
}
