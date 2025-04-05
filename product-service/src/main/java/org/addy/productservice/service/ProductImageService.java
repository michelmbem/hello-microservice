package org.addy.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.productservice.dto.ProductImageDto;
import org.addy.productservice.mapper.ProductImageMapper;
import org.addy.productservice.model.ProductImage;
import org.addy.productservice.repository.ProductImageRepository;
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
public class ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductImageMapper productImageMapper;

    public List<ProductImageDto> findAll() {
        return productImageRepository.findAll().stream().map(productImageMapper::map).toList();
    }

    public List<ProductImageDto> findByProductId(UUID productId) {
        return productImageRepository.findByProductId(productId).stream().map(productImageMapper::map).toList();
    }

    public Optional<ProductImageDto> findById(UUID id) {
        return productImageRepository.findById(id).map(productImageMapper::map);
    }

    public Optional<ProductImageDto> findProductDefault(UUID productId) {
        return productImageRepository.findProductDefault(productId).map(productImageMapper::map);
    }

    public ProductImageDto persist(ProductImageDto imageDto) {
        checkUniquenessOfDefault(null, imageDto);

        ProductImage image = productImageMapper.map(imageDto);
        image = productImageRepository.save(image);

        return productImageMapper.map(image);
    }

    public void update(UUID id, ProductImageDto imageDto) {
        checkUniquenessOfDefault(id, imageDto);

        productImageRepository.findById(id).ifPresentOrElse(
                image -> {
                    productImageMapper.map(imageDto, image);
                    productImageRepository.save(image);
                },
                () -> {
                    throw new NoSuchElementException("ProductImage with id '" + id + "' not found");
                });
    }

    public void delete(UUID id) {
        productImageRepository.findById(id).ifPresentOrElse(
                productImageRepository::delete,
                () -> {
                    throw new NoSuchElementException("ProductImage with id '" + id + "' not found");
                });
    }

    private void checkUniquenessOfDefault(UUID id, ProductImageDto imageDto) {
        if (!imageDto.isDefault()) return;

        productImageRepository.findProductDefault(imageDto.getProduct().getId())
                .filter(image -> !image.getId().equals(id))
                .ifPresent(image -> {
                    throw new IllegalArgumentException("There is already a default image for product with id '" +
                            imageDto.getProduct().getId() + "'");
                });
    }
}
