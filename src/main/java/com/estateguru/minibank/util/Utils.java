package com.estateguru.minibank.util;

import com.estateguru.minibank.dto.BankAccountDto;
import com.estateguru.minibank.model.BankAccount;
import com.estateguru.minibank.service.BankAccountService;

public class Utils {

    public static BankAccountDto convertBankAccountToBankAccountDto(BankAccount account) {
        return new BankAccountDto(account.getAccountNumber(), account.getCurrentBalance()
                , account.getLastUpdate(), account.getCreateDate());
    }

    public static BankAccount convertBankAccountDtoToBankAccount(BankAccountDto accountDto, BankAccountService service) {
        return service.getBankAccountByAccountNumber(accountDto.getAccountNumber());
    }
}
