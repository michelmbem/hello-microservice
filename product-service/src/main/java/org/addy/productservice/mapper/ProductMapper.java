package org.addy.productservice.mapper;

import org.addy.productservice.dto.ProductDto;
import org.addy.productservice.mapper.decorator.ProductMapperDecorator;
import org.addy.productservice.model.Product;
import org.mapstruct.*;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = IGNORE,
        uses = CategoryMapper.class)
@DecoratedWith(ProductMapperDecorator.class)
public interface ProductMapper {

    ProductDto map(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Product map(ProductDto productDto);

    @InheritConfiguration
    void map(ProductDto productDto, @MappingTarget Product product);
}
