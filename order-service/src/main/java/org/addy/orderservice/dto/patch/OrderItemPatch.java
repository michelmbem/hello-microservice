package org.addy.orderservice.dto.patch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.addy.model.patch.PatchModel;
import org.addy.orderservice.model.OrderItem;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record OrderItemPatch(
        @JsonProperty("product_id")
        UUID productId,
        @JsonProperty("unit_price")
        BigDecimal unitPrice,
        Short quantity,
        Float discount
) implements PatchModel<OrderItem> {

    @Override
    public void applyTo(@NonNull OrderItem entity) {
        if (productId != null) {
            entity.setProductId(productId);
        }

        if (unitPrice != null) {
            entity.setUnitPrice(unitPrice);
        }

        if (quantity != null) {
            entity.setQuantity(quantity);
        }

        if (discount != null) {
            entity.setDiscount(discount);
        }
    }

    @Override
    public @NonNull OrderItem toEntity() {
        var orderItem = new OrderItem();
        applyTo(orderItem);

        return orderItem;
    }
}
