package org.addy.orderservice.mapper;

import org.addy.orderservice.dto.OrderDto;
import org.addy.orderservice.mapper.decorator.OrderMapperDecorator;
import org.addy.orderservice.model.Order;
import org.mapstruct.*;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = IGNORE,
        uses = OrderItemMapper.class)
@DecoratedWith(OrderMapperDecorator.class)
public interface OrderMapper {

    OrderDto map(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "paymentMethodId", source = "paymentMethod.id")
    Order map(OrderDto orderDto);

    @InheritConfiguration
    void map(OrderDto orderDto, @MappingTarget Order order);
}
