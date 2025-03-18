package org.addy.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.addy.orderservice.enumeration.PaymentMethodType;

import java.util.UUID;

import static org.apache.commons.lang.StringUtils.right;

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
            case PAYPAL -> right(number, number.indexOf('@'));
            default -> right(number, 4);
        };
    }
}
