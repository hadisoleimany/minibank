package com.estateguru.minibank.serviceimpl;

import com.estateguru.minibank.dto.BankAccountDto;
import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.exception.BankAccountException;
import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.model.Currency;
import com.estateguru.minibank.model.Customer;
import com.estateguru.minibank.model.User;
import com.estateguru.minibank.repository.BankAccountRepository;
import com.estateguru.minibank.service.BankAccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository repository;

    public BankAccountServiceImpl(BankAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public void deposit(User user, BigDecimal amount) {

    }

    @Override
    public void withdrawal(User user, BigDecimal amount) {

    }

    @Override
    public void getBalance(User user, BigDecimal amount) {

    }
    @Override
    public Optional<BankAccount> getBankAccount(BankAccountDto dto) {
        return repository.findBankAccountByAccountNumber(dto.getAccountNumber());
    }

    @Override
    public BankAccount getBankAccountById(Long id) {
        return repository.findById(id).orElseThrow(()->new BankAccountException("Account Not Found"));
    }

    @Override
    public BankAccount save(BankAccount account) {
        account.setCreateDate(new Date());
        account.setLastUpdate(new Date());
        return repository.save(account);
    }

    @Override
    public BankAccount update(BankAccount account) {
        account.setLastUpdate(new Date());
        return repository.save(account);
    }

    @Override
    public BankAccount  getBankAccountByAccountNumber(String accountNumber) {
        return repository.findBankAccountByAccountNumber(accountNumber).orElseThrow(()->new BankAccountException("Account Not Found", accountNumber));
    }

    @Override
    public boolean existAccountNumber(String accountNumber) {
        return repository.existsByAccountNumber(accountNumber);
    }

    @Override
    public boolean existAccountWithCustomerWithCurrency(Customer customer, Currency currency) {
        return repository.existsBankAccountByCustomerAndCurrency(customer,currency);
    }
}
