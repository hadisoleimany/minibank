package com.estateguru.minibank.serviceimpl;

import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.model.BankTransaction;
import com.estateguru.minibank.model.TransactionType;
import com.estateguru.minibank.repository.TransactionRepository;
import com.estateguru.minibank.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveTransaction(BankTransaction bankTransaction) {
        bankTransaction.setTransactionDate(new Date());
        repository.save(bankTransaction);
    }

    @Override
    public List<BankTransaction> findAllTransactionByBankAccount(BankAccount account) {
        return repository.findAllByAccount(account);
    }

    @Override
    public List<BankTransaction> findAllTransactionByBankAccountAndDeposit(BankAccount account) {
        return repository.findAllByAccountAndTransactionType(account, TransactionType.Deposit);
    }

    @Override
    public List<BankTransaction> findAllTransactionByBankAccountAndWithdrawal(BankAccount account) {
        return repository.findAllByAccountAndTransactionType(account, TransactionType.Withdrawal);
    }

    @Override
    public List<BankTransaction> findAllTransactionByBankAccountAndTransfer(BankAccount account) {
        return repository.findAllByAccountAndTransactionType(account, TransactionType.Transfer);
    }

    @Override
    public List<BankTransaction> findAllTransactionByTransactionDate(BankAccount account, Date date) {
        return repository.findAllByAccountAndTransactionDate(account,date);
    }

    @Override
    public List<BankTransaction> findAllTransactionByTransactionDate(BankAccount account, Date fromDate, Date toDate) {
        return repository.findAllByAccountAndTransactionDateAfterAndTransactionDateBefore(account,fromDate,toDate);
    }

    @Override
    public List<BankTransaction> findAllTransactionByCustomer(CustomerDto customerDto) {
        return repository.findAllByCustomer(customerDto.getCode());
    }

    @Override
    public List<BankTransaction> findAllTransactionByCreationDate(Date creationDate) {
        return repository.findAllByTransactionDate(creationDate);
    }
}
