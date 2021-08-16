package com.estateguru.minibank.repository;

import com.estateguru.minibank.model.LimitationUserCurrency;
import com.estateguru.minibank.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimitCurrencyRepository extends CrudRepository<LimitationUserCurrency,Long> {
    List<LimitationUserCurrency> findAllByUser(User user);
}
