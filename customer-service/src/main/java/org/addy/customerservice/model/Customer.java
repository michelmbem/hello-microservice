package org.addy.customerservice.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    @NotNull
    @Indexed(unique = true)
    private String name;

    @NotNull
    @Email
    @Indexed(unique = true)
    private String email;

    private String password;

    @Field("phone_number")
    private String phoneNumber;

    private String address;

    private String city;

    private String state;

    @Field("postal_code")
    private String postalCode;

    @Valid
    @Field("payment_methods")
    private List<PaymentMethod> paymentMethods;
}
