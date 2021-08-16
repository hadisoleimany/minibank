package com.estateguru.minibank.serviceimpl;

import com.estateguru.minibank.model.Currency;
import com.estateguru.minibank.model.LimitationUserCurrency;
import com.estateguru.minibank.model.User;
import com.estateguru.minibank.repository.LimitCurrencyRepository;
import com.estateguru.minibank.service.LimitCurrencyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LimitCurrencyServiceImpl implements LimitCurrencyService {
    private final LimitCurrencyRepository repository;

    public LimitCurrencyServiceImpl(LimitCurrencyRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean accessUserForCurrency(User user, Currency currency) {
        List<LimitationUserCurrency>userCurrencyList=new ArrayList<>();
        repository.findAll().forEach(
                userCurrencyList::add
        );

        return userCurrencyList.stream().anyMatch(c->c.getCurrency().equals(currency)&&c.getUser().equals(user));
    }
}
