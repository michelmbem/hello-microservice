package org.addy.customerservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class PaymentMethod {

    @NotNull
    private UUID id;

    @NotNull
    private PaymentMethodType type;

    @NotNull
    private String number;
}
