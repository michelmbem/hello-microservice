package org.addy.productservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.productservice.dto.ProductDto;
import org.addy.productservice.dto.ProductImageDto;
import org.addy.productservice.service.ProductImageService;
import org.addy.productservice.service.ProductService;
import org.addy.web.UriHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<ProductDto> getAll() {
        return productService.findAll();
    }

    @GetMapping("by-name-part/{namePart}")
    public List<ProductDto> getByNamePart(@PathVariable String namePart) {
        return productService.findByNamePart(namePart);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable UUID id) {
        return productService.findById(id).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("by-name/{name}")
    public ResponseEntity<ProductDto> getByName(@PathVariable String name) {
        return productService.findByName(name).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("{productId}/images")
    public List<ProductImageDto> getImages(@PathVariable UUID productId) {
        return productImageService.findByProductId(productId);
    }

    @GetMapping("{productId}/images/default")
    public ResponseEntity<ProductImageDto> getDefaultImage(@PathVariable UUID productId) {
        return productImageService.findProductDefault(productId).map(ResponseEntity::ok).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<ProductDto> post(@Valid @RequestBody ProductDto productDto) {
        productDto = productService.persist(productDto);
        URI location = UriHelper.relativeLocation("/{id}", productDto.getId());

        return ResponseEntity.created(location).body(productDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> put(@PathVariable UUID id, @Valid @RequestBody ProductDto productDto) {
        productService.update(id, productDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
