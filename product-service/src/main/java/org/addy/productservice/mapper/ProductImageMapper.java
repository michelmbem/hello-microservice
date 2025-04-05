package org.addy.productservice.mapper;

import org.addy.productservice.dto.ProductImageDto;
import org.addy.productservice.mapper.decorator.ProductImageMapperDecorator;
import org.addy.productservice.model.ProductImage;
import org.mapstruct.*;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE, uses = ProductMapper.class)
@DecoratedWith(ProductImageMapperDecorator.class)
public interface ProductImageMapper {

    ProductImageDto map(ProductImage image);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductImage map(ProductImageDto imageDto);

    @InheritConfiguration
    void map(ProductImageDto imageDto, @MappingTarget ProductImage image);
}
