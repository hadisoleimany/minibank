package com.estateguru.minibank.repository;

import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.model.Currency;
import com.estateguru.minibank.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount,Long> {

    Optional<BankAccount> findBankAccountByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    boolean existsBankAccountByCustomerAndCurrency(Customer customer, Currency currency);
    List<BankAccount> findAllByCustomer_Code(String customerCode);
}
