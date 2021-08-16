package com.estateguru.minibank.dto;

import com.estateguru.minibank.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateAccountDto {
    private UserDto user;
    private CustomerDto customer;
    private String accountNumber;
    private String currencyCode;
    private BigDecimal firstDeposit;
}
