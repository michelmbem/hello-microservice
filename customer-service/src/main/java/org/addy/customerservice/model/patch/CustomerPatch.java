package org.addy.customerservice.model.patch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.addy.customerservice.model.Customer;
import org.addy.customerservice.model.PaymentMethod;
import org.addy.model.patch.PatchItem;
import org.addy.model.patch.PatchModel;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record CustomerPatch(
        String name,
        String email,
        @JsonProperty("phone_number")
        String phoneNumber,
        String address,
        String city,
        String state,
        @JsonProperty("postal_code")
        String postalCode,
        @Valid @JsonProperty("payment_methods")
        List<PatchItem<PaymentMethodPatch, UUID>> paymentMethods
) implements PatchModel<Customer> {

    @Override
    public void applyTo(@NonNull Customer entity) {
        if (name != null) {
            entity.setName(name);
        }

        if (email != null) {
            entity.setEmail(email);
        }

        if (phoneNumber != null) {
            entity.setPhoneNumber(phoneNumber);
        }

        if (address != null) {
            entity.setAddress(address);
        }

        if (city != null) {
            entity.setCity(city);
        }

        if (state != null) {
            entity.setState(state);
        }

        if (postalCode != null) {
            entity.setPostalCode(postalCode);
        }

        if (paymentMethods != null) {
            if (entity.getPaymentMethods() == null) {
                entity.setPaymentMethods(new ArrayList<>());
            }

            Collection<PaymentMethod> originalItems = entity.getPaymentMethods();
            Collection<PaymentMethod> addedItems = new ArrayList<>();
            Collection<PaymentMethod> removedItems = new ArrayList<>();

            paymentMethods.forEach(patchItem -> {
                switch (patchItem.op()) {
                    case ADD -> addedItems.add(patchItem.payload().toEntity());
                    case UPDATE -> originalItems.stream()
                            .filter(item -> item.getId().equals(patchItem.id()))
                            .findFirst()
                            .ifPresent(patchItem.payload()::applyTo);
                    case REMOVE -> originalItems.stream()
                            .filter(item -> item.getId().equals(patchItem.id()))
                            .findFirst()
                            .ifPresent(removedItems::add);
                }
            });

            originalItems.removeAll(removedItems);
            originalItems.addAll(addedItems);
        }
    }

    @Override
    public @NonNull Customer toEntity() {
        var customer = new Customer();
        applyTo(customer);

        return customer;
    }
}
