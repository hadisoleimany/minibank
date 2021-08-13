package com.estateguru.minibank.repository;

import com.estateguru.minibank.model.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency ,Long> {
    Optional<Currency> findCurrencyByCode(String code);
}
