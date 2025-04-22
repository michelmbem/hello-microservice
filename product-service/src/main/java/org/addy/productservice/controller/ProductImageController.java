package org.addy.productservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.productservice.dto.ProductImageDto;
import org.addy.productservice.service.ProductImageService;
import org.addy.web.UriHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/product-images")
public class ProductImageController {
    private final ProductImageService productImageService;
    
    @GetMapping
    public List<ProductImageDto> getAll() {
        return productImageService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductImageDto> getById(@PathVariable UUID id) {
        return productImageService.findById(id).map(ResponseEntity::ok).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<ProductImageDto> post(@Valid @RequestBody ProductImageDto imageDto) {
        imageDto = productImageService.persist(imageDto);
        URI location = UriHelper.relativeLocation("/{id}", imageDto.getId());

        return ResponseEntity.created(location).body(imageDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> put(@PathVariable UUID id, @Valid @RequestBody ProductImageDto imageDto) {
        productImageService.update(id, imageDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productImageService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
