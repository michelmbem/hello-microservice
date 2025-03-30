package org.addy.orderservice.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.addy.orderservice.dto.CustomerDto;

import java.io.IOException;

public class CustomerDtoDeserializer extends JsonDeserializer<CustomerDto> {

    @Override
    public CustomerDto deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        String customerId;

        if (node.isValueNode()) {
            customerId = node.asText();
        } else if (node.isObject() && node.has("id")) {
            customerId = node.get("id").asText();
        } else {
            throw new IllegalArgumentException("Could not parse the given expression as a customer reference");
        }

        return CustomerDto.builder()
                .id(customerId)
                .build();
    }
}
