package com.estateguru.minibank.serviceimpl;

import com.estateguru.minibank.dto.CustomerDto;
import com.estateguru.minibank.model.Customer;
import com.estateguru.minibank.repository.CustomerRepository;
import com.estateguru.minibank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
        return repository.save(convertCustomerDtoToCustomer(dto));
    }

    @Override
    public Customer saveCustomer(Customer dto) {
        return null;
    }
    private Customer convertCustomerDtoToCustomer(CustomerDto dto){
        Customer cu=new Customer();
        cu.setCode(dto.getCode());
        cu.setFamily(dto.getFamily());
        cu.setName(dto.getName());
        cu.setBirthDate(dto.getBirthDate());
        cu.setCreateDate(dto.getCreateDate());
        cu.setUpdateDate(dto.getUpdateDate());
        return cu;
    }
}
