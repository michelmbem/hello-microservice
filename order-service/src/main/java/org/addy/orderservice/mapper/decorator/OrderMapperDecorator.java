package org.addy.orderservice.mapper.decorator;

import org.addy.orderservice.client.CustomerService;
import org.addy.orderservice.dto.CustomerDto;
import org.addy.orderservice.dto.OrderDto;
import org.addy.orderservice.dto.OrderItemDto;
import org.addy.orderservice.dto.PaymentMethodDto;
import org.addy.orderservice.enumeration.PaymentMethodType;
import org.addy.orderservice.mapper.OrderItemMapper;
import org.addy.orderservice.mapper.OrderMapper;
import org.addy.orderservice.model.Order;
import org.addy.orderservice.model.OrderItem;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.addy.orderservice.util.Constants.UNKNOWN_ATTRIBUTE_VALUE;

public abstract class OrderMapperDecorator implements OrderMapper {

    @Autowired
    @Qualifier("delegate")
    protected OrderMapper delegate;

    @Autowired
    protected OrderItemMapper itemMapper;

    @Autowired
    protected CustomerService customerService;

    @Override
    public OrderDto map(Order order) {
        OrderDto orderDto = delegate.map(order);

        customerService.findById(order.getCustomerId()).ifPresentOrElse(
                customerDto  -> {
                    orderDto.setCustomer(customerDto);

                    Stream<PaymentMethodDto> paymentMethodDtoStream = customerDto.getPaymentMethods() != null
                            ? customerDto.getPaymentMethods().stream()
                            : Stream.empty();

                    orderDto.setPaymentMethod(paymentMethodDtoStream
                            .filter(pm -> pm.getId().equals(order.getPaymentMethodId()))
                            .findFirst()
                            .orElse(unknownPaymentMethod(order.getPaymentMethodId())));
                },
                () -> {
                    orderDto.setCustomer(unknownCustomer(order.getCustomerId()));
                    orderDto.setPaymentMethod(unknownPaymentMethod(order.getPaymentMethodId()));
                });

        return orderDto;
    }

    @Override
    public Order map(OrderDto orderDto) {
        Order order = delegate.map(orderDto);
        syncItems(orderDto, order);

        return order;
    }

    @Override
    public void map(OrderDto orderDto, @MappingTarget Order order) {
        delegate.map(orderDto, order);
        syncItems(orderDto, order);
    }

    private static CustomerDto unknownCustomer(String customerId) {
        if (StringUtils.hasText(customerId)) {
            return CustomerDto.builder()
                    .id(customerId)
                    .name("<unknown customer>")
                    .email(UNKNOWN_ATTRIBUTE_VALUE)
                    .phoneNumber(UNKNOWN_ATTRIBUTE_VALUE)
                    .address(UNKNOWN_ATTRIBUTE_VALUE)
                    .city(UNKNOWN_ATTRIBUTE_VALUE)
                    .state(UNKNOWN_ATTRIBUTE_VALUE)
                    .postalCode(UNKNOWN_ATTRIBUTE_VALUE)
                    .build();
        }

        return null;
    }

    private static PaymentMethodDto unknownPaymentMethod(UUID paymentMethodId) {
        if (paymentMethodId != null) {
            return PaymentMethodDto.builder()
                    .id(paymentMethodId)
                    .type(PaymentMethodType.UNKNOWN)
                    .number(UNKNOWN_ATTRIBUTE_VALUE)
                    .build();
        }

        return null;
    }

    private void syncItems(OrderDto orderDto, Order order) {
        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
        }

        List<OrderItemDto> givenItems = orderDto.getItems() != null ? orderDto.getItems() : List.of();
        List<OrderItem> originalItems = order.getItems();
        List<OrderItem> addedItems = new ArrayList<>();

        givenItems.forEach(givenItem -> originalItems.stream()
                .filter(originalItem -> originalItem.getId().equals(givenItem.getId()))
                .findFirst()
                .ifPresentOrElse(
                        originalItem -> itemMapper.map(givenItem, originalItem),
                        () -> {
                            OrderItem newItem = itemMapper.map(givenItem);
                            newItem.setOrder(order);
                            addedItems.add(newItem);
                        }
                ));

        List<OrderItem> deletedItems = originalItems.stream()
                .filter(originalItem -> givenItems.stream()
                        .noneMatch(givenItem -> originalItem.getId().equals(givenItem.getId())))
                .peek(originalItem -> originalItem.setOrder(null))
                .toList();

        originalItems.removeAll(deletedItems);
        originalItems.addAll(addedItems);

        var rankGenerator = new AtomicInteger(1);
        originalItems.forEach(item -> item.setRank((short) rankGenerator.incrementAndGet()));
    }
}
