package com.estateguru.minibank.serviceimpl;

import com.estateguru.minibank.dto.BankAccountDto;
import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.exception.BankAccountException;
import com.estateguru.minibank.exception.BusinessException;
import com.estateguru.minibank.exception.CustomerException;
import com.estateguru.minibank.model.*;
import com.estateguru.minibank.service.*;
import com.estateguru.minibank.util.Utils;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    private final CustomerService customerService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final CurrencyService currencyService;

    public UserServiceImpl(CustomerService customerService, BankAccountService bankAccountService, TransactionService transactionService, CurrencyService currencyService) {
        this.customerService = customerService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.currencyService = currencyService;
    }

    @Override
    public BankAccount createAccount(CustomerDto customerDto, String accountNumber, String currencyCode, BigDecimal firstDeposit) {

        //check accountNumber
        validationAccountNumberCreateAccount(accountNumber);
        //check Currency Exist
        Currency currency = validationCurrency(currencyCode);
        //check Customer Exist
        Customer customer = existCustomer(customerDto);
        //check Duplicate Currency
        validationCustomerCurrency(customer, currency);

        BankAccount account = new BankAccount();
        account.setCustomer(customer);
        account.setAccountNumber(accountNumber);
        account.setCurrency(currency);
        account.setCurrentBalance(BigDecimal.ZERO);
        bankAccountService.save(account);
        return depositAccount(Utils.convertBankAccountToBankAccountDto(account), firstDeposit);
    }

    private Currency validationCurrency(String currencyCode) {
      return currencyService.findCurrencyByCode(currencyCode);
    }

    private void validationCustomerCurrency(Customer customer, Currency currency) {
        bankAccountService.existAccountWithCustomerWithCurrency(customer, currency);
    }

    private void validationAccountNumberCreateAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new BankAccountException("Account Number is Null");
        }
        if (bankAccountService.existAccountNumber(accountNumber)) {
            throw new BankAccountException("AccountNumber Already Exist", accountNumber);
        }
    }

    @Override
    @Transactional
    public synchronized BankAccount depositAccount(BankAccountDto accountDto, BigDecimal amount) {
        //check Amount
        checkAmount(amount);
        //check Exist Account
        BankAccount account = existBankAccount(accountDto);
        account.setCurrentBalance(account.getCurrentBalance().add(amount));
        bankAccountService.update(account);
        return depositTransaction(account, amount);
    }

    private BankAccount depositTransaction(BankAccount account, BigDecimal amount) {
        Transaction tr = new Transaction();
        tr.setTransactionType(TransactionType.Deposit);
        tr.setTransactionAmount(amount);
        tr.setAccount(account);
        transactionService.saveTransaction(tr);
        return account;
    }
    private BankAccount withdrawalTransaction(BankAccount account, BigDecimal amount) {
        Transaction tr = new Transaction();
        tr.setTransactionType(TransactionType.Withdrawal);
        tr.setTransactionAmount(amount);
        tr.setAccount(account);
        transactionService.saveTransaction(tr);
        return account;
    }
    @Override
    @Transactional
    public synchronized BankAccount withdrawalAccount(BankAccountDto accountDto, BigDecimal amount) {
        //check Amount
        checkAmount(amount);
        //check Exist Account
        BankAccount account = existBankAccount(accountDto);
        //check Amount In Account
        checkAmountInAccount(account,amount);
        account.setCurrentBalance(account.getCurrentBalance().subtract(amount));
        bankAccountService.update(account);
        return withdrawalTransaction(account, amount);
    }

    private void checkAmountInAccount(BankAccount account, BigDecimal amount) {
        if(account.getCurrentBalance().compareTo(amount) <0){
            throw new BusinessException("The Account Does Not Have Enough Amount");
        }
    }

    @Override
    @Transactional
    public synchronized BankAccount transfer(BankAccountDto fromAccount, BankAccountDto toAccount, BigDecimal amount) {
        //check Amount
        checkAmount(amount);
        //check Exist Source Account
        BankAccount sourceAccount = existBankAccount(fromAccount);
        //check Exist Destination Account
        existBankAccount(fromAccount);
        //check Amount In Account
        checkAmountInAccount(sourceAccount,amount);
        //withdrawalAccount
        withdrawalAccount(fromAccount,amount);
        return depositAccount(toAccount, amount);

    }

    private Customer existCustomer(CustomerDto customer) {
        if(customer ==null){
            throw new CustomerException("Customer Is Null");
        }
        return customerService.getCustomer(customer).
                orElseThrow(() -> new CustomerException("Customer Not Found", customer.getCode()));
    }

    private BankAccount existBankAccount(BankAccountDto bankAccountDto) {
        return bankAccountService.getBankAccount(bankAccountDto).
                orElseThrow(() -> new BankAccountException("Account Not Found", bankAccountDto.getAccountNumber()));
    }

    private void checkAccountNumber(String accountNumber) {
        bankAccountService.getBankAccountByAccountNumber(accountNumber);
    }

    private void checkAmount(BigDecimal amount) {
        if (amount == null ||
                amount.equals(BigDecimal.ZERO) ||
                (amount.compareTo(BigDecimal.ZERO) < 0)) {
            throw new BusinessException("You Should Enter The Correct amount");
        }
    }

}
