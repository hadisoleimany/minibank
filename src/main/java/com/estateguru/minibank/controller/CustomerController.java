package com.estateguru.minibank.controller;

import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.dto.UserDto;
import com.estateguru.minibank.model.Customer;
import com.estateguru.minibank.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CustomerController.ROUTE)
public class CustomerController {
    final static String ROUTE = "/customer";
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping("/addcustomer")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> createAccount(@RequestBody CustomerDto customerDto) {
        Customer customer = service.saveCustomer(customerDto);
        customerDto.setCreateDate(customer.getCreateDate());
        customerDto.setUpdateDate(customer.getUpdateDate());
        return new ResponseEntity<Object>(customerDto, HttpStatus.CREATED);
    }
    @GetMapping("/getall")
    public ResponseEntity<List<CustomerDto>> getAllCustomer(){
        return new ResponseEntity<>(service.getAllCustomer(), HttpStatus.OK);
    }
}
