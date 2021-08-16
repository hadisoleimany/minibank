package com.estateguru.minibank.controller;

import com.estateguru.minibank.dto.RequestActionDto;
import com.estateguru.minibank.model.TransactionType;
import com.estateguru.minibank.service.RequestActionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(RequestActionController.ROUTE)
public class RequestActionController {
    final static String ROUTE = "/request";
    private final RequestActionService service;

    public RequestActionController(RequestActionService service) {
        this.service = service;
    }

    @GetMapping("/getall")
    public ResponseEntity<Object> getAllRequestByType(@RequestParam("transactiontype") TransactionType type) {

        return new ResponseEntity<Object>(service.getAllRequestByType(type), HttpStatus.OK);

    }
}
