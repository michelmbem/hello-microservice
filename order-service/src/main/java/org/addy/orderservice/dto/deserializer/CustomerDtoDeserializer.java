package org.addy.orderservice.dto.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import org.addy.model.deserializer.EntityDeserializer;
import org.addy.orderservice.dto.CustomerDto;

public class CustomerDtoDeserializer extends EntityDeserializer<CustomerDto, String> {

    public CustomerDtoDeserializer() {
        super("id", JsonNode::asText, id -> CustomerDto.builder().id(id).build());
    }
}
