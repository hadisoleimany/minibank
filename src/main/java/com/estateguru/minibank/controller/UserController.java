package com.estateguru.minibank.controller;

import com.estateguru.minibank.dto.*;
import com.estateguru.minibank.model.TransactionType;
import com.estateguru.minibank.service.BankAccountService;
import com.estateguru.minibank.service.RequestActionService;
import com.estateguru.minibank.service.UserService;
import com.estateguru.minibank.util.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(UserController.ROUTE)
public class UserController {
    final static String ROUTE = "/user";

    private final UserService service;
    private final BankAccountService bankAccountService;
    private final RequestActionService requestActionService;


    public UserController(UserService service, BankAccountService bankAccountService, RequestActionService requestActionService) {
        this.service = service;
        this.bankAccountService = bankAccountService;
        this.requestActionService = requestActionService;
    }

    @PostMapping("/signup")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDto signUp(@RequestBody UserDto userDto) {
        return service.signUp(userDto);
    }

    @PostMapping("/signin")
    public String signIn(@RequestBody LoginDto loginDto) {
        return service.signIn(loginDto.getUserName(), loginDto.getPassword());
    }

    @PostMapping("/signout")
    public void signOut(@RequestBody LoginDto loginDto) {
        service.signOut(loginDto.getUserName(), loginDto.getPassword());
    }

    @GetMapping("/getuser")
    public ResponseEntity<UserDto> getUserInfo(@RequestParam("username") String userName) {
        return new ResponseEntity<>(service.getUserInfo(userName), HttpStatus.OK);
    }

    @PostMapping("/createaccount")
    public ResponseEntity<Object> createAccount(@RequestBody CreateAccountDto createAccountDto) {
        return new ResponseEntity<Object>(service.initAndCreateBankAccount(createAccountDto), HttpStatus.OK);
    }

    @PostMapping("/depositaccount")
    public ResponseEntity<BankAccountDto> depositAccount(@RequestBody ActionAccountDto actionAccountDto) {
        BankAccountDto bankAccountDto = Utils.convertBankAccountToBankAccountDto(bankAccountService.getBankAccountByAccountNumber(actionAccountDto.getAccountNumber()));
        return new ResponseEntity<>(Utils.convertBankAccountToBankAccountDto(service.depositAccount(bankAccountDto, actionAccountDto.getAmount())), HttpStatus.OK);
    }

    @PostMapping("/withdrawalaccount")
    public ResponseEntity<BankAccountDto> withdrawalAccount(@RequestBody ActionAccountDto actionAccountDto) {
        BankAccountDto bankAccountDto = Utils.convertBankAccountToBankAccountDto(bankAccountService.getBankAccountByAccountNumber(actionAccountDto.getAccountNumber()));
        return new ResponseEntity<>(Utils.convertBankAccountToBankAccountDto(service.withdrawalAccount(bankAccountDto, actionAccountDto.getAmount())), HttpStatus.OK);
    }

    @PostMapping("/transferaccount")
    public ResponseEntity<BankAccountDto> transferAccount(@RequestBody ActionAccountDto actionAccountDto) {
        BankAccountDto bankAccountDto = Utils.convertBankAccountToBankAccountDto(bankAccountService.getBankAccountByAccountNumber(actionAccountDto.getAccountNumber()));
        BankAccountDto targetBankAccountDto = Utils.convertBankAccountToBankAccountDto(bankAccountService.getBankAccountByAccountNumber(actionAccountDto.getTargetAccountNumber()));
        return new ResponseEntity<>(Utils.convertBankAccountToBankAccountDto(service.transfer(bankAccountDto, targetBankAccountDto, actionAccountDto.getAmount())), HttpStatus.OK);
    }

    @PostMapping("/depositrequest")
    public ResponseEntity<Object> depositRequest(@RequestBody ActionAccountDto actionAccountDto) {
        RequestActionDto rqa = new RequestActionDto();
        rqa.setAmount(actionAccountDto.getAmount());
        BankAccountDto bankAccountDto = Utils.convertBankAccountToBankAccountDto(bankAccountService.getBankAccountByAccountNumber(actionAccountDto.getAccountNumber()));
        rqa.setSourceAccount(bankAccountDto);
        rqa.setTransactionType(TransactionType.Deposit);
        RequestActionDto requestActionDto = requestActionService.convertRequestActionToRequestActionDto(requestActionService.sendRequest(rqa));
        return new ResponseEntity<>(requestActionDto, HttpStatus.OK);
    }
    @PostMapping("/withdrawalrequest")
    public ResponseEntity<Object> withdrawalRequest(@RequestBody ActionAccountDto actionAccountDto) {
        RequestActionDto rqa = new RequestActionDto();
        rqa.setAmount(actionAccountDto.getAmount());
        BankAccountDto bankAccountDto = Utils.convertBankAccountToBankAccountDto(bankAccountService.getBankAccountByAccountNumber(actionAccountDto.getAccountNumber()));
        rqa.setSourceAccount(bankAccountDto);
        rqa.setTransactionType(TransactionType.Withdrawal);
        RequestActionDto requestActionDto = requestActionService.convertRequestActionToRequestActionDto(requestActionService.sendRequest(rqa));
        return new ResponseEntity<>(requestActionDto, HttpStatus.OK);
    }
    @PostMapping("/transferrequest")
    public ResponseEntity<Object> transferRequest(@RequestBody ActionAccountDto actionAccountDto) {
        RequestActionDto rqa = new RequestActionDto();
        rqa.setAmount(actionAccountDto.getAmount());
        BankAccountDto bankAccountDto = Utils.convertBankAccountToBankAccountDto(bankAccountService.getBankAccountByAccountNumber(actionAccountDto.getAccountNumber()));
        rqa.setSourceAccount(bankAccountDto);
        rqa.setDestinationAccount(Utils.convertBankAccountToBankAccountDto(bankAccountService.getBankAccountByAccountNumber(actionAccountDto.getTargetAccountNumber())));
        rqa.setTransactionType(TransactionType.Transfer);
        RequestActionDto requestActionDto = requestActionService.convertRequestActionToRequestActionDto(requestActionService.sendRequest(rqa));
        return new ResponseEntity<>(requestActionDto, HttpStatus.OK);
    }
}
