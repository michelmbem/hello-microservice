package org.addy.orderservice.mapper.decorator;

import org.addy.orderservice.client.CustomerService;
import org.addy.orderservice.dto.OrderDto;
import org.addy.orderservice.dto.OrderItemDto;
import org.addy.orderservice.mapper.OrderItemMapper;
import org.addy.orderservice.mapper.OrderMapper;
import org.addy.orderservice.model.Order;
import org.addy.orderservice.model.OrderItem;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        customerService.findById(order.getCustomerId())
                .ifPresent(customerDto  -> {
                    orderDto.setCustomer(customerDto);
                    orderDto.setPaymentMethod(customerDto.getPaymentMethods().stream()
                            .filter(pm -> Objects.equals(pm.getId(), order.getPaymentMethodId()))
                            .findFirst()
                            .orElse(null));
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

    private void syncItems(OrderDto orderDto, Order order) {
        List<OrderItemDto> givenItems = orderDto.getItems();
        List<OrderItem> originalItems = order.getItems();
        List<OrderItem> newItems = new ArrayList<>();
        List<OrderItem> deletedItems = new ArrayList<>();

        for (OrderItemDto givenItem : givenItems) {
            OrderItem originalItem = originalItems.stream()
                    .filter(item -> Objects.equals(givenItem.getId(), item.getId()))
                    .findFirst()
                    .orElse(null);

            if (originalItem != null) {
                itemMapper.map(givenItem, originalItem);
            } else {
                OrderItem newItem = itemMapper.map(givenItem);
                newItem.setOrder(order);
                newItems.add(newItem);
            }
        }

        for (OrderItem originalItem : originalItems) {
            if (givenItems.stream().noneMatch(givenItem ->
                    Objects.equals(givenItem.getId(), originalItem.getId()))) {
                originalItem.setOrder(null);
                deletedItems.add(originalItem);
            }
        }

        originalItems.removeAll(deletedItems);
        originalItems.addAll(newItems);

        short rank = 1;
        for (OrderItem originalItem : originalItems) {
            originalItem.setRank(rank++);
        }
    }
}
