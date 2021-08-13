package com.estateguru.minibank.repository;

import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.model.Transaction;
import com.estateguru.minibank.model.TransactionType;
import com.estateguru.minibank.model.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findAllByAccount(BankAccount account);

    List<Transaction> findAllByAccountAndTransactionType(BankAccount account, TransactionType type);

    List<Transaction> findAllByAccountAndTransactionDate(BankAccount account, Date date);

    List<Transaction> findAllByTransactionDate(Date date);

    List<Transaction> findAllByAccountAndTransactionDateAfterAndTransactionDateBefore(BankAccount account, Date fromDate, Date toDate);

    @Query("SELECT tr FROM Transaction as tr " +
            "WHERE tr.account.Id in(SELECT acc FROM BankAccount  AS acc WHERE acc.customer.code=:customerCode)")
    List<Transaction> findAllByCustomer(@Param("customerCode") String customerCode);


}
