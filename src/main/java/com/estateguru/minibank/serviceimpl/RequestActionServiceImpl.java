package com.estateguru.minibank.serviceimpl;

import com.estateguru.minibank.dto.RequestActionDto;
import com.estateguru.minibank.exception.BusinessException;
import com.estateguru.minibank.model.RequestAction;
import com.estateguru.minibank.model.RequestStatus;
import com.estateguru.minibank.model.TransactionType;
import com.estateguru.minibank.repository.RequestActionRepository;
import com.estateguru.minibank.service.BankAccountService;
import com.estateguru.minibank.service.RequestActionService;
import com.estateguru.minibank.util.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RequestActionServiceImpl implements RequestActionService {
    private final RequestActionRepository repository;
    private final BankAccountService bankAccountService;

    public RequestActionServiceImpl(RequestActionRepository repository, BankAccountService bankAccountService) {
        this.repository = repository;
        this.bankAccountService = bankAccountService;
    }

    @Override
    public RequestAction sendRequest(RequestActionDto requestActionDto) {
        RequestAction ra = new RequestAction();
        ra.setAmount(requestActionDto.getAmount());
        ra.setCreateDate(new Date());
        ra.setUpdateDate(new Date());
        ra.setStatus(RequestStatus.PEND);
        ra.setTransactionType(requestActionDto.getTransactionType());
        ra.setSourceAccount(Utils.convertBankAccountDtoToBankAccount(requestActionDto.getSourceAccount(), bankAccountService));
        if (requestActionDto.getTransactionType().equals(TransactionType.Transfer)) {
            ra.setDestinationAccount(Utils.convertBankAccountDtoToBankAccount(requestActionDto.getDestinationAccount(), bankAccountService));
        }

        return repository.save(ra);
    }

    @Override
    public RequestAction cancelRequest(RequestActionDto rad) {
        RequestAction rqa = getRequestActionByRequestActionDto(rad);
        rqa.setStatus(RequestStatus.CANCEL);
        rqa.setUpdateDate(new Date());
        return repository.save(rqa);
    }

    private RequestAction getRequestActionByRequestActionDto(RequestActionDto rad) {
        Optional<RequestAction> rqa = repository.findRequestActionByAmountAndTransactionTypeAndStatusAndSourceAccountAndCreateDate(
                rad.getAmount(), rad.getTransactionType(), rad.getStatus(),
                Utils.convertBankAccountDtoToBankAccount(rad.getSourceAccount(), bankAccountService)
                , rad.getCreateDate());
        if (rqa.isEmpty()) {
            throw new BusinessException("Request Not Found");
        }
        return rqa.get();
    }

    @Override
    public RequestAction acceptRequest(RequestActionDto rad) {
        RequestAction rqa = getRequestActionByRequestActionDto(rad);
        rqa.setStatus(RequestStatus.ACCEPT);
        rqa.setUpdateDate(new Date());
        return repository.save(rqa);
    }

    @Override
        public RequestAction rejectRequest(RequestActionDto rad) {
        RequestAction rqa = getRequestActionByRequestActionDto(rad);
        rqa.setStatus(RequestStatus.REJECT);
        rqa.setUpdateDate(new Date());
        rqa.setDescription(rad.getDescription());
        return repository.save(rqa);
    }

    @Override
    public void rejectAllRequestByDate() {
        // impl
    }

    @Override
    public void rejectAllRequestByUser() {
    // impl
    }

    @Override
    public List<RequestAction> getAllRejectRequest(Date from, Date to) {
        return repository.findAllByStatusAndCreateDateAfterAndCreateDateBefore(RequestStatus.REJECT, from, to);
    }

    @Override
    public List<RequestAction> getAllPendingRequest(Date from, Date to) {
        return repository.findAllByStatusAndCreateDateAfterAndCreateDateBefore(RequestStatus.PEND, from, to);
    }

    @Override
    public List<RequestAction> getAllCancelRequest(Date from, Date to) {
        return repository.findAllByStatusAndCreateDateAfterAndCreateDateBefore(RequestStatus.CANCEL, from, to);
    }

    @Override
    public List<RequestAction> getAcceptRequest(Date fromDate, Date toDate) {
        return repository.findAllByStatusAndCreateDateAfterAndCreateDateBefore(RequestStatus.ACCEPT,
                fromDate, toDate);
    }

    public RequestActionDto convertRequestActionToRequestActionDto(RequestAction ra) {
        return new RequestActionDto(ra.getTransactionType(), ra.getStatus(),
                Utils.convertBankAccountToBankAccountDto(ra.getSourceAccount()),
                Utils.convertBankAccountToBankAccountDto(ra.getDestinationAccount())
                , ra.getAmount(), ra.getCreateDate(), ra.getUpdateDate(), ra.getDescription());
    }

    @Override
    public  List<RequestActionDto> getAllRequestByType(TransactionType type) {
        List<RequestAction> allRequest = repository.findAllByTransactionType(type);
        List<RequestActionDto> actionDtos=new ArrayList<>();

        allRequest.forEach(c->actionDtos.add(new RequestActionDto(c.getTransactionType(),
                c.getStatus(),
                Utils.convertBankAccountToBankAccountDto(c.getSourceAccount()),
                Utils.convertBankAccountToBankAccountDto(c.getDestinationAccount()),c.getAmount(),
                c.getCreateDate(),c.getUpdateDate(),c.getDescription())));
        return actionDtos;
    }
}
