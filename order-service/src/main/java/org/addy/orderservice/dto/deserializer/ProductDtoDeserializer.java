package org.addy.orderservice.dto.deserializer;

import org.addy.model.deserializer.EntityDeserializer;
import org.addy.orderservice.dto.ProductDto;

import java.util.UUID;

public class ProductDtoDeserializer extends EntityDeserializer<ProductDto, UUID> {

    public ProductDtoDeserializer() {
        super("id",
                node -> UUID.fromString(node.asText()),
                id -> ProductDto.builder().id(id).build());
    }
}
