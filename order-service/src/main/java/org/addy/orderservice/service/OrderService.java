package org.addy.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.orderservice.dto.OrderDto;
import org.addy.orderservice.dto.patch.OrderPatch;
import org.addy.orderservice.mapper.OrderMapper;
import org.addy.orderservice.model.Order;
import org.addy.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final NotificationService notificationService;

    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream().map(orderMapper::map).toList();
    }

    public List<OrderDto> findByCustomerId(String customerId) {
        return orderRepository.findByCustomerId(customerId).stream().map(orderMapper::map).toList();
    }

    public Optional<OrderDto> findById(UUID id) {
        return orderRepository.findById(id).map(orderMapper::map);
    }

    public OrderDto persist(OrderDto orderDto) {
        Order order = orderMapper.map(orderDto);
        OrderDto saved = orderMapper.map(orderRepository.saveAndFlush(order));
        notificationService.orderReceived(saved);

        return saved;
    }

    public void update(UUID id, OrderDto orderDto) {
        orderRepository.findById(id).ifPresentOrElse(
                order -> {
                    orderMapper.map(orderDto, order);
                    OrderDto saved = orderMapper.map(orderRepository.saveAndFlush(order));
                    notificationService.orderUpdated(saved);
                },
                () -> {
                    throw new NoSuchElementException("Order with id '" + id + "' not found");
                });
    }

    public void patch(UUID id, OrderPatch orderPatch) {
        orderRepository.findById(id).ifPresentOrElse(
                order -> {
                    orderPatch.applyTo(order);
                    OrderDto saved = orderMapper.map(orderRepository.saveAndFlush(order));
                    notificationService.orderUpdated(saved);
                },
                () -> {
                    throw new NoSuchElementException("Order with id '" + id + "' not found");
                });
    }

    public void delete(UUID id) {
        orderRepository.findById(id).ifPresentOrElse(
                orderRepository::delete,
                () -> {
                    throw new NoSuchElementException("Order with id '" + id + "' not found");
                });
    }
}
