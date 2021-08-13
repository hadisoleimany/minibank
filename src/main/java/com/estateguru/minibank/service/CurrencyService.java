package com.estateguru.minibank.service;

import com.estateguru.minibank.dto.CurrencyDto;
import com.estateguru.minibank.model.Currency;

import java.util.Optional;

public interface CurrencyService {
    Currency findCurrencyByCode(String code);
    Currency saveCurrency(CurrencyDto currencyDto);

}
