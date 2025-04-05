package org.addy.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class ProductDto {

    private UUID id;

    @NotEmpty
    private String name;

    @NotNull
    @PositiveOrZero
    @JsonProperty("unit_price")
    private BigDecimal unitPrice;

    @PositiveOrZero
    @JsonProperty("discounted_price")
    private BigDecimal discountedPrice;

    private Collection<CategoryDto> categories;
}
