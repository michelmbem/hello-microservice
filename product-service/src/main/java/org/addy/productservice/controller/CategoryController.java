package org.addy.productservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.productservice.dto.CategoryDto;
import org.addy.productservice.dto.ProductDto;
import org.addy.productservice.service.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;
    
    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable UUID id) {
        return categoryService.findById(id).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("by-name/{name}")
    public ResponseEntity<CategoryDto> findByName(@PathVariable String name) {
        return categoryService.findByName(name).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("{categoryId}/products")
    public List<ProductDto> findProducts(@PathVariable UUID categoryId) {
        return productService.findByCategoriesId(categoryId);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> persist(@Valid @RequestBody CategoryDto categoryDto) {
        categoryDto = categoryService.persist(categoryDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(categoryDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody CategoryDto categoryDto) {
        categoryService.update(id, categoryDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
