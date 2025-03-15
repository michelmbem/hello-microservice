package org.addy.orderservice.client;

import org.addy.orderservice.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "customer-service", path = "/customers")
public interface CustomerService {

    @GetMapping
    List<CustomerDto> findAll();

    @GetMapping("{id}")
    Optional<CustomerDto> findById(@PathVariable String id);

    @GetMapping("by-name/{name}")
    Optional<CustomerDto> findByName(@PathVariable String name);

    @GetMapping("by-email/{email}")
    Optional<CustomerDto> findByEmail(@PathVariable String email);

    @GetMapping("by-phone-number/{phoneNumber}")
    Optional<CustomerDto> findByPhoneNumber(@PathVariable String phoneNumber);

    @PostMapping
    Optional<CustomerDto> persist(@RequestBody CustomerDto customerDto);

    @PutMapping("{id}")
    void update(@PathVariable String id, @RequestBody CustomerDto customerDto);

    @DeleteMapping("{id}")
    void delete(@PathVariable String id);
}
