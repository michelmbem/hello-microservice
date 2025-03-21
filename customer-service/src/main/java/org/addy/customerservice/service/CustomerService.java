package org.addy.customerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.customerservice.model.Customer;
import org.addy.customerservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(String id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByName(String name) {
        return customerRepository.findByName(name);
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }

    public Customer persist(Customer customer) {
        return customerRepository.save(customer);
    }

    public void update(String id, Customer customer) {
        customerRepository.findById(id).ifPresentOrElse(original -> {
                    original.setName(customer.getName());
                    original.setEmail(customer.getEmail());
                    original.setPhoneNumber(customer.getPhoneNumber());
                    original.setAddress(customer.getAddress());
                    original.setCity(customer.getCity());
                    original.setState(customer.getState());
                    original.setPostalCode(customer.getPostalCode());
                    original.setPaymentMethods(customer.getPaymentMethods());
                    customerRepository.save(original);
                },
                () -> {
                    throw new NoSuchElementException("Customer with id '" + id + "' not found");
                });
    }

    public void delete(String id) {
        customerRepository.findById(id).ifPresentOrElse(customerRepository::delete,
                () -> {
                    throw new NoSuchElementException("Customer with id '" + id + "' not found");
                });
    }
}
