package org.addy.customerservice.constraint.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.addy.customerservice.constraint.ValidItemPatch;
import org.addy.customerservice.model.patch.core.PatchItem;

public final class ValidItemPatchValidator implements
        ConstraintValidator<ValidItemPatch, PatchItem<?, ?>> {

    @Override
    public boolean isValid(PatchItem value, ConstraintValidatorContext context) {
        return switch (value.op()) {
            case ADD -> value.payload() != null;
            case REMOVE -> value.id() != null;
            case UPDATE -> value.id() != null && value.payload() != null;
        };
    }
}
