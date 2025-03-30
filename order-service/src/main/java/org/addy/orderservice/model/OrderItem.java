package org.addy.orderservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @PositiveOrZero
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Positive
    private short quantity;

    @PositiveOrZero
    private float discount;

    @Positive
    private short rank;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @PrePersist
    public void init() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
