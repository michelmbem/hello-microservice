package org.addy.orderservice.dto;

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

    private String password;

    private String phoneNumber;

    private String address;

    private String city;

    private String state;

    private String postalCode;

    @Valid
    private List<PaymentMethodDto> paymentMethods;
}
