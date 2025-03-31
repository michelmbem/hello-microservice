package org.addy.orderservice.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.addy.orderservice.dto.PaymentMethodDto;

import java.io.IOException;
import java.util.UUID;

public class PaymentMethodDtoDeserializer extends JsonDeserializer<PaymentMethodDto> {

    @Override
    public PaymentMethodDto deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        UUID pmId;

        if (node.isValueNode()) {
            pmId = UUID.fromString(node.asText());
        } else if (node.isObject() && node.has("id")) {
            pmId = UUID.fromString(node.get("id").asText());
        } else {
            throw new IllegalArgumentException("Could not parse the given expression as a reference to a payment method");
        }

        return PaymentMethodDto.builder()
                .id(pmId)
                .build();
    }
}
