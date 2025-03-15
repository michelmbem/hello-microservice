package org.addy.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class OrderItemDto {

    private UUID id;

    @NotNull
    private ProductDto product;

    @NotNull
    @PositiveOrZero
    private BigDecimal unitPrice;

    @Positive
    private short quantity;

    @PositiveOrZero
    private float discount;
}
