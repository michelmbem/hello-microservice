package org.addy.productservice.mapper.decorator;

import lombok.extern.slf4j.Slf4j;
import org.addy.productservice.dto.ProductDto;
import org.addy.productservice.mapper.ProductMapper;
import org.addy.productservice.model.Category;
import org.addy.productservice.model.Product;
import org.addy.productservice.repository.CategoryRepository;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public abstract class ProductMapperDecorator implements ProductMapper {

    @Autowired
    @Qualifier("delegate")
    protected ProductMapper delegate;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product map(ProductDto productDto) {
        Product product = delegate.map(productDto);
        mapCategories(productDto, product);

        return product;
    }

    @Override
    public void map(ProductDto productDto, @MappingTarget Product product) {
        delegate.map(productDto, product);
        mapCategories(productDto, product);
    }

    private void mapCategories(ProductDto productDto, Product product) {
        if (productDto.getCategories() != null) {
            Collection<Category> categories = productDto.getCategories()
                    .stream()
                    .map(categoryDto -> categoryRepository.findById(categoryDto.getId()).orElseThrow())
                    .collect(Collectors.toSet());

            product.setCategories(categories);
        }
    }
}
