package org.addy.customerservice.model.patch;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.addy.customerservice.model.PaymentMethod;
import org.addy.customerservice.model.PaymentMethodType;
import org.addy.model.patch.PatchModel;
import org.springframework.lang.NonNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record PaymentMethodPatch(
        PaymentMethodType type,
        String number
) implements PatchModel<PaymentMethod> {

    @Override
    public void applyTo(@NonNull PaymentMethod entity) {
        if (type != null) {
            entity.setType(type);
        }

        if (number != null) {
            entity.setNumber(number);
        }
    }

    @Override
    public @NonNull PaymentMethod toEntity() {
        var pm = new PaymentMethod();
        applyTo(pm);

        return pm;
    }
}
