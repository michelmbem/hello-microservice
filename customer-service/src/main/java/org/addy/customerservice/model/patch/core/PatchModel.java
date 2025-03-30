package org.addy.customerservice.model.patch.core;

import org.springframework.lang.NonNull;

public interface PatchModel<E> {
    void applyTo(@NonNull E entity);
    @NonNull E toEntity();
}
