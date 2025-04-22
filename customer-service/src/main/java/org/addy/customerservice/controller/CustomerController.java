package org.addy.customerservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.customerservice.model.Customer;
import org.addy.customerservice.model.patch.CustomerPatch;
import org.addy.customerservice.service.CustomerService;
import org.addy.web.UriHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    
    @GetMapping
    public List<Customer> getAll() {
        return customerService.findAll();
    }

    @GetMapping("by-name-part/{namePart}")
    public List<Customer> getByNamePart(@PathVariable String namePart) {
        return customerService.findByNamePart(namePart);
    }

    @GetMapping("{id}")
    public ResponseEntity<Customer> getById(@PathVariable String id) {
        return customerService.findById(id).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("by-name/{name}")
    public ResponseEntity<Customer> getByName(@PathVariable String name) {
        return customerService.findByName(name).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("by-email/{email}")
    public ResponseEntity<Customer> getByEmail(@PathVariable String email) {
        return customerService.findByEmail(email).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("by-phone-number/{phoneNumber}")
    public ResponseEntity<Customer> getByPhoneNumber(@PathVariable String phoneNumber) {
        return customerService.findByPhoneNumber(phoneNumber).map(ResponseEntity::ok).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<Customer> post(@Valid @RequestBody Customer customer) {
        customer = customerService.persist(customer);
        URI location = UriHelper.relativeLocation("/{id}", customer.getId());

        return ResponseEntity.created(location).body(customer);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> put(@PathVariable String id, @Valid @RequestBody Customer customer) {
        customerService.update(id, customer);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> patch(@PathVariable String id, @Valid @RequestBody CustomerPatch customerPatch) {
        customerService.patch(id, customerPatch);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        customerService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
