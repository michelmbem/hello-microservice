package org.addy.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class ProductImageDto {

    private UUID id;

    @NotNull
    @URL
    private String url;

    @JsonProperty("is_default")
    private boolean isDefault;

    @NotNull
    private ProductDto product;
}
