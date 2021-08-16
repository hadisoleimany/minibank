package com.estateguru.minibank.repository;

import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.model.BankTransaction;
import com.estateguru.minibank.model.TransactionType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<BankTransaction, Long> {
    List<BankTransaction> findAllByAccount(BankAccount account);

    List<BankTransaction> findAllByAccountAndTransactionType(BankAccount account, TransactionType type);

    List<BankTransaction> findAllByAccountAndTransactionDate(BankAccount account, Date date);

    List<BankTransaction> findAllByTransactionDate(Date date);

    List<BankTransaction> findAllByAccountAndTransactionDateAfterAndTransactionDateBefore(BankAccount account, Date fromDate, Date toDate);

    @Query("SELECT tr FROM BankTransaction as tr " +
            "WHERE tr.account.Id in(SELECT acc FROM BankAccount  AS acc WHERE acc.customer.code=:customerCode)")
    List<BankTransaction> findAllByCustomer(@Param("customerCode") String customerCode);


}
