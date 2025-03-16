package org.addy.orderservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.orderservice.dto.OrderDto;
import org.addy.orderservice.service.NotificationService;
import org.addy.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final NotificationService notificationService;

    @GetMapping
    public List<OrderDto> findAll() {
        return orderService.findAll();
    }

    @GetMapping("by-customer/{customerId}")
    public List<OrderDto> findByCustomerId(@PathVariable String customerId) {
        return orderService.findByCustomerId(customerId);
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable UUID id) {
        return orderService.findById(id).map(ResponseEntity::ok).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<OrderDto> persist(@Valid @RequestBody OrderDto orderDto) {
        orderDto = orderService.persist(orderDto);
        notificationService.orderReceived(orderDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(orderDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody OrderDto orderDto) {
        orderDto = orderService.update(id, orderDto);
        notificationService.orderUpdated(orderDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        orderService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
