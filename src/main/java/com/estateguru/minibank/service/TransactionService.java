package com.estateguru.minibank.service;

import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.model.BankTransaction;

import java.util.Date;
import java.util.List;


public interface TransactionService {
    void saveTransaction(BankTransaction bankTransaction);

    List<BankTransaction> findAllTransactionByBankAccount(BankAccount account);

    List<BankTransaction> findAllTransactionByBankAccountAndDeposit(BankAccount account);

    List<BankTransaction> findAllTransactionByBankAccountAndWithdrawal(BankAccount account);

    List<BankTransaction> findAllTransactionByBankAccountAndTransfer(BankAccount account);

    List<BankTransaction> findAllTransactionByTransactionDate(BankAccount account, Date date);

    List<BankTransaction> findAllTransactionByTransactionDate(BankAccount account, Date fromDate, Date toDate);

    List<BankTransaction> findAllTransactionByCustomer(CustomerDto customerDto);

    List<BankTransaction> findAllTransactionByCreationDate(Date creationDate);


}
