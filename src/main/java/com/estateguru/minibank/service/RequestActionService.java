package com.estateguru.minibank.service;

import com.estateguru.minibank.dto.RequestActionDto;
import com.estateguru.minibank.model.RequestAction;

import java.util.Date;
import java.util.List;

public interface RequestActionService {
    RequestAction sendRequest(RequestActionDto requestActionDto);

    RequestAction cancelRequest(RequestActionDto requestActionDto);

    RequestAction acceptRequest(RequestActionDto requestActionDto);

    RequestAction rejectRequest(RequestActionDto requestActionDto);

    void rejectAllRequestByDate();

    void rejectAllRequestByUser();

    List<RequestAction> getAllRejectRequest(Date from,Date to);

    List<RequestAction> getAllCancelRequest(Date from,Date to);

    List<RequestAction> getAllPendingRequest(Date from,Date to);

    List<RequestAction> getAcceptRequest(Date fromDate, Date toDate);

    RequestActionDto convertRequestActionToRequestActionDto(RequestAction ra);
}
