package org.addy.orderservice.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.addy.orderservice.dto.ProductDto;

import java.io.IOException;
import java.util.UUID;

public class ProductDtoDeserializer extends JsonDeserializer<ProductDto> {

    @Override
    public ProductDto deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        UUID productId;

        if (node.isValueNode()) {
            productId = UUID.fromString(node.asText());
        } else if (node.isObject() && node.has("id")) {
            productId = UUID.fromString(node.get("id").asText());
        } else {
            throw new IllegalArgumentException("Could not parse the given expression as a reference to a product");
        }

        return ProductDto.builder()
                .id(productId)
                .build();
    }
}
