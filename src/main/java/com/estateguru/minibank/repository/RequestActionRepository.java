package com.estateguru.minibank.repository;

import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.model.RequestAction;
import com.estateguru.minibank.model.RequestStatus;
import com.estateguru.minibank.model.TransactionType;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestActionRepository extends CrudRepository<RequestAction,Long> {
    Optional<RequestAction>  findRequestActionByAmountAndTransactionTypeAndStatusAndSourceAccountAndCreateDate
            (BigDecimal amount, TransactionType transactionType, RequestStatus status, BankAccount sourceAccount
           , Date createDate);

    List<RequestAction> findAllByStatusAndCreateDateAfterAndCreateDateBefore(RequestStatus status,Date from,Date to);

}
