package org.addy.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String name;

    @NotNull
    @JsonProperty("unit_price")
    private BigDecimal unitPrice;

    @JsonProperty("discounted_price")
    private BigDecimal discountedPrice;

    private Collection<CategoryDto> categories;
}
