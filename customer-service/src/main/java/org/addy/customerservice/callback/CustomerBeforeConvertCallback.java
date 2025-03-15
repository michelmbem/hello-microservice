package org.addy.customerservice.callback;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.addy.customerservice.model.Customer;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CustomerBeforeConvertCallback implements BeforeConvertCallback<Customer> {

    @Override
    public @NonNull Customer onBeforeConvert(Customer customer, @NonNull String collection) {
        customer.getPaymentMethods().forEach(pm -> {
            if (pm.getId() == null)
                pm.setId(UUID.randomUUID());
        });

        return customer;
    }
}
