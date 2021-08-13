package com.estateguru.minibank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    public CurrencyDto(String code) {
        this.code = code;
    }

    private String name;
    private String code;
}
