package org.addy.productservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class CategoryDto {

    private UUID id;

    @NotNull
    private String name;

    private String description;
}
