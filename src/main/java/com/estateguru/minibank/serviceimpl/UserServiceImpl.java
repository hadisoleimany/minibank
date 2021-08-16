package com.estateguru.minibank.serviceimpl;

import com.estateguru.minibank.aop.AccessibleUser;
import com.estateguru.minibank.dto.BankAccountDto;
import com.estateguru.minibank.dto.CreateAccountDto;
import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.dto.UserDto;
import com.estateguru.minibank.exception.BankAccountException;
import com.estateguru.minibank.exception.BusinessException;
import com.estateguru.minibank.exception.CustomerException;
import com.estateguru.minibank.model.*;
import com.estateguru.minibank.repository.UserRepository;
import com.estateguru.minibank.service.*;
import com.estateguru.minibank.util.Utils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserServiceImpl implements UserService {

    private final CustomerService customerService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final CurrencyService currencyService;
    private final BCryptPasswordEncoder encoder;
    private final UserRepository rp;
    private final LimitCurrencyService limitCurrencyService;

    public UserServiceImpl(CustomerService customerService, BankAccountService bankAccountService, TransactionService transactionService, CurrencyService currencyService, BCryptPasswordEncoder encoder, UserRepository rp, LimitCurrencyService limitCurrencyService) {
        this.customerService = customerService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.currencyService = currencyService;
        this.encoder = encoder;
        this.rp = rp;
        this.limitCurrencyService = limitCurrencyService;
    }

    @Override
    public CreateAccountDto initAndCreateBankAccount(CreateAccountDto accountDto) {
        SecurityContext context = SecurityContextHolder.getContext();
        User user = getUserByUserName(accountDto.getUser().getUserName());
        if (!context.getAuthentication().getPrincipal().equals(user.getUserName())) {
            throw new BusinessException("Wrong User !!!!!!");
        }
        createAccount(user, accountDto.getCustomer(), accountDto.getAccountNumber(), accountDto.getCurrencyCode()
                , accountDto.getFirstDeposit());
        return accountDto;

    }


    @Override
    public BankAccount createAccount(User user, CustomerDto customerDto, String accountNumber, String currencyCode, BigDecimal firstDeposit) {

        //check accountNumber
        validationAccountNumberCreateAccount(accountNumber);
        //check Currency Exist
        Currency currency = validationCurrency(currencyCode);
        //check Currency For Create Account
        if (user.getRole().equals(RoleType.USER)) {
            CurrencyIsValidForUser(currencyCode, new User());
        }
        //check Customer Exist
        Customer customer = existCustomer(customerDto);
        //check Duplicate Currency
        validationCustomerCurrency(customer, currency);

        BankAccount account = new BankAccount();
        account.setCustomer(customer);
        account.setAccountNumber(accountNumber);
        account.setCurrency(currency);
        account.setCurrentBalance(BigDecimal.ZERO);
        account.setCreatedBy(user);
        bankAccountService.save(account);
        return depositAccount(Utils.convertBankAccountToBankAccountDto(account), firstDeposit);
    }

    @AccessibleUser
    @Override
    @Transactional
    public synchronized BankAccount depositAccount(BankAccountDto accountDto, BigDecimal amount) {
        //check Amount
        checkAmount(amount);
        //check Exist Account
        BankAccount account = existBankAccount(accountDto);
        account.setCurrentBalance(account.getCurrentBalance().add(amount));
        bankAccountService.update(account);
        return depositTransaction(account, amount);
    }

    @AccessibleUser
    @Override
    @Transactional
    public synchronized BankAccount withdrawalAccount(BankAccountDto accountDto, BigDecimal amount) {
        //check Amount
        checkAmount(amount);
        //check Exist Account
        BankAccount account = existBankAccount(accountDto);
        //check Amount In Account
        checkAmountInAccount(account, amount);
        account.setCurrentBalance(account.getCurrentBalance().subtract(amount));
        bankAccountService.update(account);
        return withdrawalTransaction(account, amount);
    }

    @AccessibleUser
    @Override
    @Transactional
    public synchronized BankAccount transfer(BankAccountDto fromAccount, BankAccountDto toAccount, BigDecimal amount) {
        //check Amount
        checkAmount(amount);
        //check Exist Source Account
        BankAccount sourceAccount = existBankAccount(fromAccount);
        //check Exist Destination Account
        existBankAccount(fromAccount);
        //check Amount In Account
        checkAmountInAccount(sourceAccount, amount);
        //withdrawalAccount
        withdrawalAccount(fromAccount, amount);
        return depositAccount(toAccount, amount);

    }

    @Override
    public UserDto signUp(UserDto userDto) {
        if (rp.findByUserName(userDto.getUserName()).isPresent()) {
            throw new BusinessException("User Already Exist");
        }
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        User user = getUserFromUserDto(userDto);
        User save = rp.save(user);
        save.setPassword(null);
        return new UserDto(save);
    }

    @Override
    public String signIn(String userName, String password) {
        return signInOrOut(userName, password, true);
    }

    @Override
    public void signOut(String userName, String password) {
        signInOrOut(userName, password, false);
    }

    @Override
    public UserDto getUserInfo(String userName) {
        User user = getUserByUserName(userName);
        user.setPassword(null);
        return new UserDto(user);
    }

    private User getUserByUserName(String userName) {
        return rp.findByUserName(userName).orElseThrow(() -> new BusinessException("User Does Not Exist"));
    }

    @Override
    public Long getUserIdByUserDto(String userName, String name, String code) {
        Long user = rp.getUserIdByUserNameAndNameAndCode(userName, name, code);
        if (user == null) {
            throw new BusinessException("User Does Not Exist");
        }
        return user;
    }

    private void checkPermission(RoleType roleType) {
        if (roleType.equals(RoleType.USER)) {
            throw new BusinessException("You Don't Have Permission");
        }
    }

    private Currency validationCurrency(String currencyCode) {
        return currencyService.findCurrencyByCode(currencyCode);
    }

    public void CurrencyIsValidForUser(String currencyCode, User user) {
        if (!limitCurrencyService.accessUserForCurrency(user,validationCurrency(currencyCode))) {
            throw new BusinessException("You Don't Have Permission To Create Account With Currency : " + currencyCode);
        }
    }

    private void validationCustomerCurrency(Customer customer, Currency currency) {
        bankAccountService.existAccountWithCustomerWithCurrency(customer, currency);
    }

    private void validationAccountNumberCreateAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new BankAccountException("Account Number is Null");
        }
        if (bankAccountService.existAccountNumber(accountNumber)) {
            throw new BankAccountException("AccountNumber Already Exist", accountNumber);
        }
    }

    private BankAccount depositTransaction(BankAccount account, BigDecimal amount) {
        BankTransaction tr = new BankTransaction();
        tr.setTransactionType(TransactionType.Deposit);
        tr.setTransactionAmount(amount);
        tr.setAccount(account);
        transactionService.saveTransaction(tr);
        return account;
    }

    private BankAccount withdrawalTransaction(BankAccount account, BigDecimal amount) {
        BankTransaction tr = new BankTransaction();
        tr.setTransactionType(TransactionType.Withdrawal);
        tr.setTransactionAmount(amount);
        tr.setAccount(account);
        transactionService.saveTransaction(tr);
        return account;
    }

    private void checkAmountInAccount(BankAccount account, BigDecimal amount) {
        if (account.getCurrentBalance().compareTo(amount) < 0) {
            throw new BusinessException("The Account Does Not Have Enough Amount");
        }
    }

    private Customer existCustomer(CustomerDto customer) {
        if (customer == null) {
            throw new CustomerException("Customer Is Null");
        }
        return customerService.getCustomer(customer).
                orElseThrow(() -> new CustomerException("Customer Not Found", customer.getCode()));
    }

    private BankAccount existBankAccount(BankAccountDto bankAccountDto) {
        return bankAccountService.getBankAccount(bankAccountDto).
                orElseThrow(() -> new BankAccountException("Account Not Found", bankAccountDto.getAccountNumber()));
    }

    private void checkAccountNumber(String accountNumber) {
        bankAccountService.getBankAccountByAccountNumber(accountNumber);
    }

    private void checkAmount(BigDecimal amount) {
        if (amount == null ||
                amount.equals(BigDecimal.ZERO) ||
                (amount.compareTo(BigDecimal.ZERO) < 0)) {
            throw new BusinessException("You Should Enter The Correct amount");
        }
    }

    private String signInOrOut(String userName, String password, boolean loggedIn) {
        User user = getUserByUserName(userName);
        if (!encoder.matches(password, user.getPassword())) {
            throw new BusinessException("UserName Or Password Is Wrong");
        }
        user.setLoggedIn(loggedIn);
        rp.save(user);
        return getJWTToken(userName, user.getRole());
    }

    private String getJWTToken(String username, RoleType roleType) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_" + roleType.name());

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 6000000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }

    private User getUserFromUserDto(UserDto userDto) {
        User user = new User();
        user.setCode(userDto.getCode());
        user.setUserName(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        user.setName(userDto.getName());
        user.setRole(userDto.getRole());
        return user;
    }

}
