package org.addy.customerservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class PaymentMethod {

    @NotNull
    @JsonProperty(access = READ_ONLY)
    private UUID id;

    @NotNull
    private PaymentMethodType type;

    @NotNull
    private String number;
}
