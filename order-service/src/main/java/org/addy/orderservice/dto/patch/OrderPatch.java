package org.addy.orderservice.dto.patch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.addy.model.patch.PatchItem;
import org.addy.model.patch.PatchModel;
import org.addy.orderservice.model.Order;
import org.addy.orderservice.model.OrderItem;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record OrderPatch(
        @JsonProperty("delivery_date")
        LocalDateTime deliveryDate,
        @JsonProperty("customer_id")
        String customerId,
        @JsonProperty("payment_method_id")
        UUID paymentMethodId,
        List<PatchItem<OrderItemPatch, UUID>> items
) implements PatchModel<Order> {

    @Override
    public void applyTo(@NonNull Order entity) {
        if (deliveryDate != null) {
            entity.setDeliveryDate(deliveryDate);
        }

        if (customerId != null) {
            entity.setCustomerId(customerId);
        }

        if (paymentMethodId != null) {
            entity.setPaymentMethodId(paymentMethodId);
        }

        if (items != null) {
            if (entity.getItems() == null) {
                entity.setItems(new ArrayList<>());
            }

            Collection<OrderItem> originalItems = entity.getItems();
            Collection<OrderItem> addedItems = new ArrayList<>();
            Collection<OrderItem> removedItems = new ArrayList<>();

            items.forEach(patchItem -> {
                switch (patchItem.op()) {
                    case ADD -> addedItems.add(patchItem.payload().toEntity());
                    case UPDATE -> originalItems.stream()
                            .filter(item -> item.getId().equals(patchItem.id()))
                            .findFirst()
                            .ifPresent(patchItem.payload()::applyTo);
                    case REMOVE -> originalItems.stream()
                            .filter(item -> item.getId().equals(patchItem.id()))
                            .findFirst()
                            .ifPresent(removedItems::add);
                }
            });

            removedItems.forEach(item -> item.setOrder(null));
            originalItems.removeAll(removedItems);

            addedItems.forEach(item -> item.setOrder(entity));
            originalItems.addAll(addedItems);

            var rankGenerator = new AtomicInteger(1);
            originalItems.forEach(item -> item.setRank((short) rankGenerator.incrementAndGet()));
        }
    }

    @Override
    public @NonNull Order toEntity() {
        var order = new Order();
        applyTo(order);

        return order;
    }
}
