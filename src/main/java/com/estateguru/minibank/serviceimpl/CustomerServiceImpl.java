package com.estateguru.minibank.serviceimpl;

import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.exception.CustomerException;
import com.estateguru.minibank.model.Customer;
import com.estateguru.minibank.repository.CustomerRepository;
import com.estateguru.minibank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.estateguru.minibank.exception.ErrorCode.CUSTOMER_NOT_FOUND;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Customer> getCustomer(CustomerDto dto) {
        return repository.findCustomerByCode(dto.getCode());
    }

    @Override
    public Customer saveCustomer(CustomerDto dto) {
        Customer customer = convertCustomerDtoToCustomer(dto);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());

        return repository.save(customer);
    }

    @Override
    public Customer saveCustomer(Customer dto) {
        return null;
    }

    @Override
    public Customer getCustomerByCustomerCode(String customerCode) {
        return repository.findCustomerByCode(customerCode).
                orElseThrow(() -> new CustomerException(CUSTOMER_NOT_FOUND, customerCode));
    }

    @Override
    public List<CustomerDto> getAllCustomer() {

        List<CustomerDto> cuDto = new ArrayList<>();
        repository.findAll().forEach(c -> cuDto.add(
                new CustomerDto(c.getName(),c.getFamily(),c.getCode(),
                        c.getBirthDate(),c.getCreateDate(),c.getUpdateDate()
                )));
        return cuDto;

    }

    private Customer convertCustomerDtoToCustomer(CustomerDto dto) {
        Customer cu = new Customer();
        cu.setCode(dto.getCode());
        cu.setFamily(dto.getFamily());
        cu.setName(dto.getName());
        cu.setBirthDate(dto.getBirthDate());
        cu.setCreateDate(dto.getCreateDate());
        cu.setUpdateDate(dto.getUpdateDate());
        return cu;
    }
}
