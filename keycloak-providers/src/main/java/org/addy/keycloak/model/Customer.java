package org.addy.keycloak.model;

import lombok.*;
import org.addy.keycloak.util.Documents;
import org.bson.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Customer {
    private String id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private Long createdAt;
    private boolean active;

    public static Customer from(Document document) {
        return Customer.builder()
                .id(document.getObjectId("_id").toHexString())
                .name(document.getString("name"))
                .email(document.getString("email"))
                .password(document.getString("password"))
                .phoneNumber(document.getString("phone_number"))
                .address(document.getString("address"))
                .city(document.getString("city"))
                .state(document.getString("state"))
                .postalCode(document.getString("postal_code"))
                .createdAt(Documents.getLong(document, "created_at"))
                .active(Documents.getBoolean(document,"active", false))
                .build();
    }

    public Document toDocument() {
        return new Document()
                .append("name", name)
                .append("email", email)
                .append("password", password)
                .append("phone_number", phoneNumber)
                .append("address", address)
                .append("city", city)
                .append("state", state)
                .append("postal_code", postalCode)
                .append("created_at", createdAt)
                .append("active", active)
                .append("_class", getClass().getName());
    }
}
