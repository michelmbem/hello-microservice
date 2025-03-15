package org.addy.productservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
public class Product {

    @Id
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "discounted_price")
    private BigDecimal discountedPrice;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Collection<Category> categories;

    @OneToMany(mappedBy = "product")
    private Collection<ProductImage> images;

    @PrePersist
    public void init() {
        if (id == null) id = UUID.randomUUID();
    }
}
