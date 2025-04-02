package org.addy.customerservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.addy.customerservice.converter.LocalDateTimeConverter;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.ValueConverter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Document(collection = "customers")
public class Customer {

    @Id
    @JsonProperty(access = READ_ONLY)
    private String id;

    @NotNull
    @Indexed(unique = true)
    private String name;

    @NotNull
    @Email
    @Indexed(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @Field("phone_number")
    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    private String city;

    private String state;

    @Field("postal_code")
    @JsonProperty("postal_code")
    private String postalCode;

    @Field("created_at")
    @JsonProperty(value = "created_at", access = READ_ONLY)
    @ValueConverter(LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    private boolean active;

    @Valid
    @Field("payment_methods")
    @JsonProperty("payment_methods")
    private List<PaymentMethod> paymentMethods;
}
