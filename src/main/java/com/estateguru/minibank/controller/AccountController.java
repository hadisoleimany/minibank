package com.estateguru.minibank.controller;

import com.estateguru.minibank.dto.BankAccountDto;
import com.estateguru.minibank.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AccountController.ROUTE)
public class AccountController {
    final static String ROUTE = "/acc";


    private final BankAccountService service;

    public AccountController(BankAccountService service) {
        this.service = service;
    }

    @GetMapping("/getbalance")
    public ResponseEntity<BankAccountDto> getBalance(@RequestParam("accountnumber") String accountNumber) {
        return new ResponseEntity<BankAccountDto>(service.getBalance(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/getallaccountbycustomer")
    public ResponseEntity<List<BankAccountDto>> getAllAccountByCustomer(@RequestParam("customercode") String customerCode) {
        return new ResponseEntity<>(service.getAllAccountByCustomer(customerCode), HttpStatus.OK);
    }
}
