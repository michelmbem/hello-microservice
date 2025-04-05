package org.addy.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.addy.orderservice.dto.deserializer.CustomerDtoDeserializer;
import org.addy.orderservice.dto.deserializer.PaymentMethodDtoDeserializer;
import org.addy.orderservice.enumeration.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class OrderDto {

    private UUID id;

    @JsonProperty(value = "created_on", access = READ_ONLY)
    private LocalDateTime createdOn;

    @NotNull
    @FutureOrPresent
    @JsonProperty("delivery_date")
    private LocalDateTime deliveryDate;

    @NotNull
    @JsonDeserialize(using = CustomerDtoDeserializer.class)
    private CustomerDto customer;

    @JsonProperty("payment_method")
    @JsonDeserialize(using = PaymentMethodDtoDeserializer.class)
    private PaymentMethodDto paymentMethod;

    @JsonProperty(access = READ_ONLY)
    private OrderStatus status;

    @Valid
    @NotEmpty
    List<OrderItemDto> items;

    @JsonProperty("total_price")
    public BigDecimal getTotalPrice() {
        if (items != null) {
            return items.stream()
                    .map(OrderItemDto::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return BigDecimal.ZERO;
    }
}
