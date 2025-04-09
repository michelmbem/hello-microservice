package org.addy.orderservice.dto.deserializer;

import org.addy.model.deserializer.EntityDeserializer;
import org.addy.orderservice.dto.PaymentMethodDto;

import java.util.UUID;

public class PaymentMethodDtoDeserializer extends EntityDeserializer<PaymentMethodDto, UUID> {

    public PaymentMethodDtoDeserializer() {
        super("id",
                node -> UUID.fromString(node.asText()),
                id -> PaymentMethodDto.builder().id(id).build());
    }
}
