package com.estateguru.minibank.service;

import com.estateguru.minibank.model.Currency;
import com.estateguru.minibank.model.User;

public interface LimitCurrencyService {

    boolean accessUserForCurrency(User user, Currency currency);
}
