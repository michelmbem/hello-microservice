package org.addy.customerservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.customerservice.model.Customer;
import org.addy.customerservice.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    
    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Customer> findById(@PathVariable String id) {
        return customerService.findById(id).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("by-name/{name}")
    public ResponseEntity<Customer> findByName(@PathVariable String name) {
        return customerService.findByName(name).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("by-email/{email}")
    public ResponseEntity<Customer> findByEmail(@PathVariable String email) {
        return customerService.findByEmail(email).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("by-phone-number/{phoneNumber}")
    public ResponseEntity<Customer> findByPhoneNumber(@PathVariable String phoneNumber) {
        return customerService.findByPhoneNumber(phoneNumber).map(ResponseEntity::ok).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<Customer> persist(@RequestBody Customer customer) {
        customer = customerService.persist(customer);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customer.getId())
                .toUri();

        return ResponseEntity.created(location).body(customer);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Customer customer) {
        customerService.update(id, customer);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        customerService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
