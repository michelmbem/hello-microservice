package org.addy.orderservice.dto.patch.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import org.addy.orderservice.constraint.ValidItemPatch;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@ValidItemPatch
@JsonInclude(NON_NULL)
public record PatchItem<E, K>(
        @NotNull PatchOperation op,
        K id,
        E payload
) {
}
