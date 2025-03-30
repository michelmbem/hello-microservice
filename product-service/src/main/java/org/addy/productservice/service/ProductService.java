package org.addy.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.productservice.dto.ProductDto;
import org.addy.productservice.mapper.ProductMapper;
import org.addy.productservice.model.Product;
import org.addy.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream().map(productMapper::map).toList();
    }

    public List<ProductDto> findByCategoriesId(UUID categoryId) {
        return productRepository.findByCategoriesId(categoryId).stream().map(productMapper::map).toList();
    }

    public List<ProductDto> findByNamePart(String namePart) {
        return productRepository.findByNameContainingIgnoreCase(namePart).stream().map(productMapper::map).toList();
    }

    public Optional<ProductDto> findById(UUID id) {
        return productRepository.findById(id).map(productMapper::map);
    }

    public Optional<ProductDto> findByName(String name) {
        return productRepository.findByName(name).map(productMapper::map);
    }

    public ProductDto persist(ProductDto productDto) {
        Product product = productMapper.map(productDto);
        product = productRepository.save(product);
        
        return productMapper.map(product);
    }

    public void update(UUID id, ProductDto productDto) {
        productRepository.findById(id).ifPresentOrElse(
                product -> {
                    productMapper.map(productDto, product);
                    productRepository.save(product);
                },
                () -> {
                    throw new NoSuchElementException("Product with id '" + id + "' not found");
                });
    }

    public void delete(UUID id) {
        productRepository.findById(id).ifPresentOrElse(
                productRepository::delete,
                () -> {
                    throw new NoSuchElementException("Product with id '" + id + "' not found");
                });
    }
}
