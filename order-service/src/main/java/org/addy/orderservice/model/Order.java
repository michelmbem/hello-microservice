package org.addy.orderservice.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.addy.orderservice.enumeration.OrderStatus;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    private UUID id;

    @CurrentTimestamp
    @Column(name = "created_on", insertable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "delivery_date", nullable = false)
    private LocalDateTime deliveryDate;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "payment_method_id")
    private UUID paymentMethodId;

    @Generated
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(insertable = false)
    private OrderStatus status;

    @Builder.Default
    @Valid
    @NotEmpty
    @OrderBy("rank ASC")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> items = new ArrayList<>();

    @PrePersist
    public void init() {
        if (id == null) id = UUID.randomUUID();
    }
}
