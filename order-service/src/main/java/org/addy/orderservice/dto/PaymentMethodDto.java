package org.addy.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.addy.orderservice.enumeration.PaymentMethodType;
import org.apache.commons.lang.StringUtils;

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

    @JsonIgnore
    public String getNumberEnd() {
        return switch (type) {
            case CREDIT_CARD, DEBIT_CARD -> StringUtils.right(number, 4);
            case PAYPAL -> number.substring(number.indexOf('@'));
            default -> number;
        };
    }
}
