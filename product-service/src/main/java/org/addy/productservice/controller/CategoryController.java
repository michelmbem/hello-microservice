package org.addy.productservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.productservice.dto.CategoryDto;
import org.addy.productservice.dto.ProductDto;
import org.addy.productservice.service.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;
    
    @GetMapping
    public List<CategoryDto> getAll() {
        return categoryService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable UUID id) {
        return categoryService.findById(id).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("by-name/{name}")
    public ResponseEntity<CategoryDto> getByName(@PathVariable String name) {
        return categoryService.findByName(name).map(ResponseEntity::ok).orElseThrow();
    }

    @GetMapping("{categoryId}/products")
    public List<ProductDto> getProducts(@PathVariable UUID categoryId) {
        return productService.findByCategoriesId(categoryId);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> post(@Valid @RequestBody CategoryDto categoryDto) {
        categoryDto = categoryService.persist(categoryDto);
        URI location = UriHelper.relativeLocation("/{id}", categoryDto.getId());

        return ResponseEntity.created(location).body(categoryDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> put(@PathVariable UUID id, @Valid @RequestBody CategoryDto categoryDto) {
        categoryService.update(id, categoryDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
