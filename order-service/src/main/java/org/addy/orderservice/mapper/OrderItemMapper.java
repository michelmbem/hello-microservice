package org.addy.orderservice.mapper;

import org.addy.orderservice.dto.OrderItemDto;
import org.addy.orderservice.mapper.decorator.OrderItemMapperDecorator;
import org.addy.orderservice.model.OrderItem;
import org.mapstruct.*;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
@DecoratedWith(OrderItemMapperDecorator.class)
public interface OrderItemMapper {

    OrderItemDto map(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", source = "product.id")
    OrderItem map(OrderItemDto orderItemDto);

    @InheritConfiguration
    void map(OrderItemDto orderItemDto, @MappingTarget OrderItem orderItem);
}
