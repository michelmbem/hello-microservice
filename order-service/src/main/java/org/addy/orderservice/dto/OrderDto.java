package org.addy.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.addy.orderservice.enumeration.OrderStatus;

import java.math.BigDecimal;
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

    @JsonProperty("created_on")
    private LocalDateTime createdOn;

    @NotNull
    @JsonProperty("delivery_date")
    private LocalDateTime deliveryDate;

    @NotNull
    private CustomerDto customer;

    @JsonProperty("payment_method")
    private PaymentMethodDto paymentMethod;

    @NotNull
    private OrderStatus status;

    @Builder.Default
    @Valid
    @NotEmpty
    List<OrderItemDto> items = new ArrayList<>();

    @JsonProperty("total_price")
    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemDto item : items) {
            totalPrice = totalPrice.add(item.getTotalPrice());
        }

        return totalPrice;
    }
}
