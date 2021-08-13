package com.estateguru.minibank.serviceimpl;

import com.estateguru.minibank.dto.CurrencyDto;
import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.dto.RequestActionDto;
import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.model.RequestAction;
import com.estateguru.minibank.model.RequestStatus;
import com.estateguru.minibank.model.TransactionType;
import com.estateguru.minibank.service.CurrencyService;
import com.estateguru.minibank.service.CustomerService;
import com.estateguru.minibank.service.RequestActionService;
import com.estateguru.minibank.service.UserService;
import com.estateguru.minibank.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class RequestActionServiceImplTest {

    @Autowired
    private RequestActionService service;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        customerService.saveCustomer(getNewCustomerDto());
        currencyService.saveCurrency(new CurrencyDto("US Dollar", "USD"));
    }
    @Test
    @Transactional
    void sendRequest_succeed() {
        RequestActionDto rad = getNewRequestActionDto();
        RequestAction requestAction = service.sendRequest(rad);
        assertNotNull(requestAction);
        assertEquals(new BigDecimal(1000).longValue(),requestAction.getAmount().longValue());
        assertEquals(RequestStatus.PEND,requestAction.getStatus());
        assertEquals(TransactionType.Deposit,requestAction.getTransactionType());
    }

    @Test
    @Transactional
    void cancelRequest_succeed() {
        RequestActionDto rad = getNewRequestActionDto();
        RequestAction requestAction = service.sendRequest(rad);
        rad.setStatus(RequestStatus.PEND);
        rad.setCreateDate(requestAction.getCreateDate());
        RequestAction cancelRequestAction = service.cancelRequest(rad);
        assertEquals(RequestStatus.CANCEL,cancelRequestAction.getStatus());
    }
    @Test
    @Transactional
    void acceptRequest_succeed() {
        RequestActionDto rad = getNewRequestActionDto();
        RequestAction requestAction = service.sendRequest(rad);
        rad.setStatus(RequestStatus.PEND);
        rad.setCreateDate(requestAction.getCreateDate());
        RequestAction cancelRequestAction = service.acceptRequest(rad);
        assertEquals(RequestStatus.ACCEPT,cancelRequestAction.getStatus());
    }
    @Test
    @Transactional
    void rejectRequest_succeed() {
        RequestActionDto rad = getNewRequestActionDto();
        RequestAction requestAction = service.sendRequest(rad);
        rad.setStatus(RequestStatus.PEND);
        rad.setCreateDate(requestAction.getCreateDate());
        RequestAction cancelRequestAction = service.rejectRequest(rad);
        assertEquals(RequestStatus.REJECT,cancelRequestAction.getStatus());
    }
    private RequestActionDto getNewRequestActionDto() {
        CustomerDto customer = getNewCustomerDto();
        BankAccount souAcc = userService.createAccount(customer, "1", "USD", new BigDecimal(10));
        RequestActionDto rad = new RequestActionDto();
        rad.setAmount(new BigDecimal(1000));
        rad.setTransactionType(TransactionType.Deposit);
        rad.setSourceAccount(Utils.convertBankAccountToBankAccountDto(souAcc));
        return rad;
    }
    private CustomerDto getNewCustomerDto() {
        CustomerDto cu = new CustomerDto();
        cu.setName("Hadi");
        cu.setFamily("Soleimany");
        cu.setCode("4580088360");
        cu.setBirthDate(new Date());
        cu.setCreateDate(new Date());
        cu.setUpdateDate(new Date());

        return cu;
    }
}