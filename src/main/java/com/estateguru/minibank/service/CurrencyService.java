package com.estateguru.minibank.service;

import com.estateguru.minibank.dto.CurrencyDto;
import com.estateguru.minibank.model.Currency;

import java.math.BigDecimal;


public interface CurrencyService {
    Currency findCurrencyByCode(String code);
    Currency saveCurrency(CurrencyDto currencyDto);
    BigDecimal convertCurrency(BigDecimal amount,Currency mainCurrency,Currency toCurrency);

}
