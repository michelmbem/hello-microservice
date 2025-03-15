package org.addy.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.addy.orderservice.enumeration.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class OrderDto {

    private UUID id;

    private LocalDateTime createdOn;

    @NotNull
    private CustomerDto customer;

    private PaymentMethodDto paymentMethod;

    @NotNull
    private OrderStatus status;

    @Builder.Default
    @Valid
    @NotEmpty
    List<OrderItemDto> items = new ArrayList<>();
}
