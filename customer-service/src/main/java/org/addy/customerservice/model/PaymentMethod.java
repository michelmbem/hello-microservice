package org.addy.customerservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.addy.customerservice.enumeration.PaymentMethodType;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class PaymentMethod {

    @JsonProperty(access = READ_ONLY)
    private UUID id;

    @NotNull
    private PaymentMethodType type;

    @NotEmpty
    private String number;
}
