package org.addy.orderservice.mapper.decorator;

import org.addy.orderservice.client.ProductService;
import org.addy.orderservice.dto.OrderItemDto;
import org.addy.orderservice.dto.ProductDto;
import org.addy.orderservice.mapper.OrderItemMapper;
import org.addy.orderservice.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;

import static org.addy.orderservice.util.Constants.*;

public abstract class OrderItemMapperDecorator implements OrderItemMapper {

    @Autowired
    @Qualifier("delegate")
    protected OrderItemMapper delegate;

    @Autowired
    protected ProductService productService;

    @Override
    public OrderItemDto map(OrderItem orderItem) {
        OrderItemDto orderItemDto = delegate.map(orderItem);

        productService.findById(orderItem.getProductId()).ifPresentOrElse(
                orderItemDto::setProduct,
                () -> orderItemDto.setProduct(unknownProduct(orderItem)));

        return orderItemDto;
    }

    private static ProductDto unknownProduct(OrderItem orderItem) {
        return ProductDto.builder()
                .id(orderItem.getProductId())
                .name(UNKNOWN_ATTRIBUTE_VALUE)
                .unitPrice(orderItem.getUnitPrice())
                .discountedPrice(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(1f - orderItem.getDiscount())))
                .build();
    }
}
