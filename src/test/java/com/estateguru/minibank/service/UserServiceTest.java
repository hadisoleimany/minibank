package com.estateguru.minibank.service;

import com.estateguru.minibank.dto.BankAccountDto;
import com.estateguru.minibank.dto.CurrencyDto;
import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.dto.UserDto;
import com.estateguru.minibank.exception.BankAccountException;
import com.estateguru.minibank.exception.BusinessException;
import com.estateguru.minibank.exception.CustomerException;
import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.model.Currency;
import com.estateguru.minibank.model.RoleType;
import com.estateguru.minibank.model.User;
import com.estateguru.minibank.repository.UserRepository;
import com.estateguru.minibank.util.Utils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService service;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        customerService.saveCustomer(getNewCustomerDto());
        Currency usd = currencyService.findCurrencyByCode("usd");
        if (usd == null) {
            currencyService.saveCurrency(new CurrencyDto("US Dollar", "USD"));
        }
        Optional<User> byUserName = userRepository.findByUserName("hadi");
        if (byUserName.isEmpty()) {
            service.signUp(new UserDto("hadi", "100", "hadi", "5699", RoleType.ADMIN));
        }
//        service.signUp(new UserDto("ali", "50", "ali", "123", RoleType.USER));
    }

    @BeforeAll
    static void beforeAll() {

    }

    @Test
    @Transactional
    void createAccount_succeed() {
        CustomerDto customer = getNewCustomerDto();

        Optional<User> byUserName = userRepository.findByUserName("hadi");
        BankAccount account = service.createAccount(byUserName.get(), customer, "1", "USD", new BigDecimal(10));
        assertNotNull(account);
        assertEquals(new BigDecimal(10), account.getCurrentBalance());
    }

    @Test
    @Transactional
    void createAccount_expectCustomerException() {
        CustomerDto customer = getNewCustomerDto();
        Optional<User> user = userRepository.findByUserName("hadi");
        customer.setCode("1111");
        assertThrows(CustomerException.class, () -> {
            service.createAccount(user.get(),
                    customer, "1", "USD", new BigDecimal(10));
        });
    }

    @Test
    @Transactional
    void createAccount_expectCustomerException_null() {
        Optional<User> user = userRepository.findByUserName("hadi");
        assertThrows(CustomerException.class, () -> {
            service.createAccount(user.get(), null, "1", "USD", new BigDecimal(10));
        }, "Customer Is Null");
    }

    @Test
    @Transactional
    void createAccount_expectBusinessException_wrong_amount_deposit() {
        CustomerDto customer = getNewCustomerDto();
        Optional<User> user = userRepository.findByUserName("hadi");
        assertThrows(BusinessException.class, () -> {
            service.createAccount(user.get(), customer, "1", "USD", new BigDecimal(-10));
        });
    }

    @Test
    @Transactional
    void createAccount_expectBusinessException_wrong_amount_deposit_zero() {
        CustomerDto customer = getNewCustomerDto();
        Optional<User> user = userRepository.findByUserName("hadi");
        assertThrows(BusinessException.class, () -> {
            service.createAccount(user.get(), customer, "1", "USD", new BigDecimal(0));
        });
    }

    @Test
    @Transactional
    void createAccount_expectBusinessException_wrong_amount_deposit_null() {
        CustomerDto customer = getNewCustomerDto();
        Optional<User> user = userRepository.findByUserName("hadi");
        assertThrows(BusinessException.class, () -> {
            service.createAccount(user.get(), customer, "1", "USD", null);
        }, "You Should Enter The Correct amount");
    }

    @Test
    @Transactional
    void createAccount_expectBusinessException_wrong_currency() {
        CustomerDto customer = getNewCustomerDto();
        Optional<User> user = userRepository.findByUserName("hadi");
        assertThrows(BusinessException.class, () -> {
            service.createAccount(user.get(), customer, "1", "UD", new BigDecimal(10));
        }, "Currency Does Not Exist with Code : " + "UD");
    }

    @Test
    @Transactional
    void createAccount_expectBankAccountException_accountNumber_exist() {
        CustomerDto customer = getNewCustomerDto();
        Optional<User> user = userRepository.findByUserName("hadi");
        service.createAccount(user.get(), customer, "1", "USD", new BigDecimal(10));
        assertThrows(BankAccountException.class, () -> {
            service.createAccount(user.get(), customer, "1", "USD", new BigDecimal(10));
        }, "AccountNumber Already Exist");
    }

    @Test
    @Transactional
    void createAccount_expectBankAccountException_accountNumber_null() {
        CustomerDto customer = getNewCustomerDto();
        Optional<User> user = userRepository.findByUserName("hadi");
        assertThrows(BankAccountException.class, () -> {
            service.createAccount(user.get(), customer, null, "USD", new BigDecimal(10));
        }, "Account Number is Null");
    }

    @Test
    @Transactional
    void deposit() {
        CustomerDto customer = getNewCustomerDto();
        Optional<User> user = userRepository.findByUserName("hadi");
        BankAccount account = service.createAccount(user.get(), customer, "1", "USD", new BigDecimal(10));
        BankAccountDto bankAccountDto = Utils.convertBankAccountToBankAccountDto(account);
        BankAccount account1 = service.depositAccount(bankAccountDto, new BigDecimal(10));
        assertEquals(new BigDecimal(20).longValue(), account1.getCurrentBalance().longValue());
    }

    @Test
    @Transactional
    void withdrawal_succeed() {

        CustomerDto customer = getNewCustomerDto();
        Optional<User> user = userRepository.findByUserName("hadi");
        BankAccount account = service.createAccount(user.get(), customer, "1", "USD", new BigDecimal(30));
        BankAccountDto bankAccountDto = Utils.convertBankAccountToBankAccountDto(account);
        BankAccount account1 = service.withdrawalAccount(bankAccountDto, new BigDecimal(10));
        assertEquals(new BigDecimal(20).longValue(), account1.getCurrentBalance().longValue());
    }

    @Test
    @Transactional
    void withdrawal_expect_BusinessException_more_than_amount() {

        CustomerDto customer = getNewCustomerDto();
        Optional<User> user = userRepository.findByUserName("hadi");
        BankAccount account = service.createAccount(user.get(), customer, "1", "USD", new BigDecimal(30));
        BankAccountDto bankAccountDto = Utils.convertBankAccountToBankAccountDto(account);
        assertThrows(BusinessException.class, () -> {
            service.withdrawalAccount(bankAccountDto, new BigDecimal(40));
        }, "The Account Does Not Have Enough Amount");

    }

    @Test
    @Transactional
    void transfer_succeed() {
        CustomerDto customer = getNewCustomerDto();
        Optional<User> user = userRepository.findByUserName("hadi");

        BankAccount baseAccount = service.createAccount(user.get(), customer, "1", "USD", new BigDecimal(100));
        BankAccountDto sourceAccount = Utils.convertBankAccountToBankAccountDto(baseAccount);
        BankAccountDto destinationAccount = Utils.convertBankAccountToBankAccountDto(
                service.createAccount(user.get(), customer, "2", "USD", new BigDecimal(30)));
        BankAccount transfer = service.transfer(sourceAccount, destinationAccount, new BigDecimal(50));

        assertEquals(new BigDecimal(80).longValue(), transfer.getCurrentBalance().longValue());

        assertEquals(new BigDecimal(50).longValue(), bankAccountService.getBankAccountById(baseAccount.getId())
                .getCurrentBalance().longValue());

    }

    @Test
    @Transactional
    void transfer_expect_BusinessException_more_than_amount() {
        CustomerDto customer = getNewCustomerDto();
        Optional<User> user = userRepository.findByUserName("hadi");
        BankAccount baseAccount = service.createAccount(user.get(), customer, "1", "USD", new BigDecimal(100));
        BankAccountDto sourceAccount = Utils.convertBankAccountToBankAccountDto(baseAccount);
        BankAccountDto destinationAccount = Utils.convertBankAccountToBankAccountDto(
                service.createAccount(user.get(), customer, "2", "USD", new BigDecimal(30)));


        assertThrows(BusinessException.class, () -> {
            service.transfer(sourceAccount, destinationAccount, new BigDecimal(150));
        }, "The Account Does Not Have Enough Amount");

    }

    //    @Test
//void testi(){
//        System.out.println(new BigDecimal(10).compareTo(new BigDecimal(3)) );
//        System.out.println(new BigDecimal(10).compareTo(new BigDecimal(10)) );
//        System.out.println(new BigDecimal(10).compareTo(new BigDecimal(11)) );
//
//
//    }
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