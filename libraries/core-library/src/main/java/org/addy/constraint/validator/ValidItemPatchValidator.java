package org.addy.constraint.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.addy.constraint.ValidItemPatch;
import org.addy.model.patch.PatchItem;

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
