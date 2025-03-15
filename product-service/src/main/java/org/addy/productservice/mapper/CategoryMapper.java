package org.addy.productservice.mapper;

import org.addy.productservice.dto.CategoryDto;
import org.addy.productservice.model.Category;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface CategoryMapper {

    CategoryDto map(Category category);

    @Mapping(target = "id", ignore = true)
    Category map(CategoryDto categoryDto);

    @InheritConfiguration
    void map(CategoryDto categoryDto, @MappingTarget Category category);
}
