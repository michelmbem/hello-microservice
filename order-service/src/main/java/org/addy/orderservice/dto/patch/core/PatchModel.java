package org.addy.orderservice.dto.patch.core;

import org.springframework.lang.NonNull;

public interface PatchModel<E> {
    void applyTo(@NonNull E entity);
    @NonNull E toEntity();
}
