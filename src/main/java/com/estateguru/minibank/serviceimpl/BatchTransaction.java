package com.estateguru.minibank.serviceimpl;

import com.estateguru.minibank.dto.RequestActionDto;
import com.estateguru.minibank.model.RequestAction;
import com.estateguru.minibank.service.RequestActionService;
import com.estateguru.minibank.service.UserService;
import com.estateguru.minibank.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BatchTransaction {
    private final RequestActionService requestActionService;
    private final UserService userService;

    public BatchTransaction(RequestActionService requestActionService, UserService userService) {
        this.requestActionService = requestActionService;
        this.userService = userService;
    }

    @Async
    @Scheduled(cron = "${instructionSchedularTime}")
    public void doTransaction() {

        List<RequestAction> acceptRequest = requestActionService.getAcceptRequest(new Date(), new Date());
        log.info("Start For Check Ready Transaction");
        for (RequestAction action : acceptRequest) {
            try {
                log.info("Transaction is : "+action.getTransactionType() );
                switch (action.getTransactionType()) {
                    case Deposit:
                        userService.depositAccount(
                                Utils.convertBankAccountToBankAccountDto(action.getSourceAccount()),
                                action.getAmount());
                        break;
                    case Withdrawal:
                        userService.withdrawalAccount(
                                Utils.convertBankAccountToBankAccountDto(action.getSourceAccount()),
                                action.getAmount());
                        break;
                    case Transfer:
                        userService.transfer(
                                Utils.convertBankAccountToBankAccountDto(action.getSourceAccount()),
                                Utils.convertBankAccountToBankAccountDto(action.getDestinationAccount()),
                                action.getAmount());
                        break;
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                RequestActionDto requestActionDto = requestActionService.convertRequestActionToRequestActionDto(action);
                requestActionDto.setDescription(e.getMessage());
                requestActionService.rejectRequest(requestActionDto);
            }

        }
    }
}
