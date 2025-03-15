package org.addy.orderservice.client;

import org.addy.orderservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "product-service", path = "/products")
public interface ProductService {

    @GetMapping
    List<ProductDto> findAll();

    @GetMapping("{id}")
    Optional<ProductDto> findById(@PathVariable UUID id);

    @GetMapping("by-name/{name}")
    Optional<ProductDto> findByName(@PathVariable String name);

    @PostMapping
    Optional<ProductDto> persist(@RequestBody ProductDto productDto);

    @PutMapping("{id}")
    void update(@PathVariable UUID id, @RequestBody ProductDto productDto);

    @DeleteMapping("{id}")
    void delete(@PathVariable UUID id);
}
