package com.estateguru.minibank.serviceimpl;

import com.estateguru.minibank.exception.BusinessException;
import com.estateguru.minibank.exception.CustomerException;
import com.estateguru.minibank.model.User;
import com.estateguru.minibank.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Test
    void currencyIsValidForUser() {
        User user = new User();
        user.setId(2L);
        assertThrows(BusinessException.class, () -> {
            userService.CurrencyIsValidForUser("JPY",user);
        });
    }
}