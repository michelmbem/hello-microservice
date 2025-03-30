package org.addy.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.productservice.dto.CategoryDto;
import org.addy.productservice.mapper.CategoryMapper;
import org.addy.productservice.model.Category;
import org.addy.productservice.repository.CategoryRepository;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::map).toList();
    }

    public Optional<CategoryDto> findById(UUID id) {
        return categoryRepository.findById(id).map(categoryMapper::map);
    }

    public Optional<CategoryDto> findByName(String name) {
        return categoryRepository.findByName(name).map(categoryMapper::map);
    }

    public CategoryDto persist(CategoryDto categoryDto) {
        Category category = categoryMapper.map(categoryDto);
        category = categoryRepository.save(category);

        return categoryMapper.map(category);
    }

    public void update(UUID id, CategoryDto categoryDto) {
        categoryRepository.findById(id).ifPresentOrElse(
                category -> {
                    categoryMapper.map(categoryDto, category);
                    categoryRepository.save(category);
                },
                () -> {
                    throw new NoSuchElementException("Category with id '" + id + "' not found");
                });
    }

    public void delete(UUID id) {
        categoryRepository.findById(id).ifPresentOrElse(
                categoryRepository::delete,
                () -> {
                    throw new NoSuchElementException("Category with id '" + id + "' not found");
                });
    }
}
