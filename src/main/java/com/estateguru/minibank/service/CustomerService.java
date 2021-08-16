package com.estateguru.minibank.service;

import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Optional<Customer> getCustomer(CustomerDto dto);
    Customer saveCustomer(CustomerDto dto);
    Customer saveCustomer(Customer customer);
    Customer getCustomerByCustomerCode(String customerCode);

    List<CustomerDto> getAllCustomer();
}
