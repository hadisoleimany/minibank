package com.estateguru.minibank.service;

import com.estateguru.minibank.dto.BankAccountDto;
import com.estateguru.minibank.dto.CreateAccountDto;
import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.dto.UserDto;
import com.estateguru.minibank.exception.BusinessException;
import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public interface UserService {
    CreateAccountDto initAndCreateBankAccount(CreateAccountDto accountDto);
    BankAccount createAccount(User user,CustomerDto customerDto, String accountNumber, String currencyCode,BigDecimal firstDeposit);
    BankAccount depositAccount(BankAccountDto accountDto,BigDecimal amount);
    BankAccount withdrawalAccount( BankAccountDto accountDto, BigDecimal amount);
    BankAccount transfer(BankAccountDto fromAccount,BankAccountDto toAccount,BigDecimal amount);

    UserDto signUp(UserDto userDto);
    String signIn(String userName, String password);
    void signOut(String userName, String password);
    UserDto getUserInfo(String userName);
    Long getUserIdByUserDto(String userName, String name, String code);

}
