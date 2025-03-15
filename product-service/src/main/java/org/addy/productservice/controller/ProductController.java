package org.addy.productservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.productservice.dto.ProductDto;
import org.addy.productservice.dto.ProductImageDto;
import org.addy.productservice.service.ProductImageService;
import org.addy.productservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductImageService productImageService;
    
    @GetMapping
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable UUID id) {
        return productService.findById(id).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("by-name/{name}")
    public ResponseEntity<ProductDto> findByName(@PathVariable String name) {
        return productService.findByName(name).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("{productId}/images")
    public List<ProductImageDto> findImages(@PathVariable UUID productId) {
        return productImageService.findByProductId(productId);
    }

    @GetMapping("{productId}/images/default")
    public ResponseEntity<ProductImageDto> findDefaultImage(@PathVariable UUID productId) {
        return productImageService.findProductDefault(productId).map(ResponseEntity::ok).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<ProductDto> persist(@Valid @RequestBody ProductDto productDto) {
        productDto = productService.persist(productDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(productDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody ProductDto productDto) {
        productService.update(id, productDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
