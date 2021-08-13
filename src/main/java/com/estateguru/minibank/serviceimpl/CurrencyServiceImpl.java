package com.estateguru.minibank.serviceimpl;

import com.estateguru.minibank.dto.CurrencyDto;
import com.estateguru.minibank.exception.BusinessException;
import com.estateguru.minibank.model.Currency;
import com.estateguru.minibank.repository.CurrencyRepository;
import com.estateguru.minibank.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository repository;

    public CurrencyServiceImpl(CurrencyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Currency findCurrencyByCode(String code) {
        return repository.findCurrencyByCode(code.toUpperCase()).orElseThrow(
                ()-> new BusinessException("Currency Does Not Exist with Code : "+ code));

    }

    @Override
    public Currency saveCurrency(CurrencyDto currencyDto) {
        return repository.save(new Currency(currencyDto.getName(),currencyDto.getCode().toUpperCase()));
    }
}
