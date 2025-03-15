package org.addy.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.addy.orderservice.enumeration.PaymentMethodType;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class PaymentMethodDto {

    @NotNull
    private UUID id;

    @NotNull
    private PaymentMethodType type;

    @NotNull
    private String number;
}
