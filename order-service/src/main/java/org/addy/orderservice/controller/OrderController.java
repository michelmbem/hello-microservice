package org.addy.orderservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.addy.orderservice.dto.OrderDto;
import org.addy.orderservice.dto.patch.OrderPatch;
import org.addy.orderservice.service.OrderService;
import org.addy.web.UriHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getAll() {
        return orderService.findAll();
    }

    @GetMapping("by-customer/{customerId}")
    public List<OrderDto> getByCustomer(@PathVariable String customerId) {
        return orderService.findByCustomerId(customerId);
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable UUID id) {
        return orderService.findById(id).map(ResponseEntity::ok).orElseThrow();
    }

    @PostMapping
    @PreAuthorize("hasRole('customer')")
    public ResponseEntity<OrderDto> post(@Valid @RequestBody OrderDto orderDto) {
        orderDto = orderService.persist(orderDto);
        URI location = UriHelper.relativeLocation("/{id}", orderDto.getId());

        return ResponseEntity.created(location).body(orderDto);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('order-manager')")
    public ResponseEntity<Void> put(@PathVariable UUID id, @Valid @RequestBody OrderDto orderDto) {
        orderService.update(id, orderDto);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('order-manager')")
    public ResponseEntity<Void> patch(@PathVariable UUID id, @Valid @RequestBody OrderPatch orderPatch) {
        orderService.patch(id, orderPatch);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('order-manager')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        orderService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
