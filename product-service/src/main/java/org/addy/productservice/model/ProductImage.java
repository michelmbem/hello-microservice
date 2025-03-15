package org.addy.productservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "product_image")
public class ProductImage {

    @Id
    private UUID id;

    @NotNull
    @URL
    private String url;

    @Column(name = "is_default")
    private boolean isDefault;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @PrePersist
    public void init() {
        if (id == null) id = UUID.randomUUID();
    }
}
