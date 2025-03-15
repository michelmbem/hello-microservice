package org.addy.orderservice.mapper.decorator;

import org.addy.orderservice.client.ProductService;
import org.addy.orderservice.dto.OrderItemDto;
import org.addy.orderservice.mapper.OrderItemMapper;
import org.addy.orderservice.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class OrderItemMapperDecorator implements OrderItemMapper {

    @Autowired
    @Qualifier("delegate")
    protected OrderItemMapper delegate;

    @Autowired
    protected ProductService productService;

    @Override
    public OrderItemDto map(OrderItem orderItem) {
        OrderItemDto orderItemDto = delegate.map(orderItem);

        productService.findById(orderItem.getProductId())
                .ifPresent(orderItemDto::setProduct);

        return orderItemDto;
    }
}
