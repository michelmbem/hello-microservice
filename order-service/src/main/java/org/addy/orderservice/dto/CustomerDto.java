package org.addy.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class CustomerDto {

    private String id;

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

    private String city;

    private String state;

    @JsonProperty("postal_code")
    private String postalCode;

    @Valid
    @JsonProperty("payment_methods")
    private List<PaymentMethodDto> paymentMethods;
}
