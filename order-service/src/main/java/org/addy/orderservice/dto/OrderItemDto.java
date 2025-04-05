package org.addy.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;
import lombok.*;
import org.addy.orderservice.dto.deserializer.ProductDtoDeserializer;

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
    @JsonDeserialize(using = ProductDtoDeserializer.class)
    private ProductDto product;

    @NotNull
    @PositiveOrZero
    @JsonProperty("unit_price")
    private BigDecimal unitPrice;

    @Positive
    private short quantity;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private float discount;

    @JsonProperty("total_price")
    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity * (1f - discount)));
    }
}
