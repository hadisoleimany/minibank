package com.estateguru.minibank.service;

import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.model.Transaction;

import java.util.Date;
import java.util.List;


public interface TransactionService {
    void saveTransaction(Transaction transaction);

    List<Transaction> findAllTransactionByBankAccount(BankAccount account);

    List<Transaction> findAllTransactionByBankAccountAndDeposit(BankAccount account);

    List<Transaction> findAllTransactionByBankAccountAndWithdrawal(BankAccount account);

    List<Transaction> findAllTransactionByBankAccountAndTransfer(BankAccount account);

    List<Transaction> findAllTransactionByTransactionDate(BankAccount account, Date date);

    List<Transaction> findAllTransactionByTransactionDate(BankAccount account, Date fromDate, Date toDate);

    List<Transaction> findAllTransactionByCustomer(CustomerDto customerDto);

    List<Transaction> findAllTransactionByCreationDate(Date creationDate);


}
