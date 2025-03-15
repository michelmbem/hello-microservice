package org.addy.productservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
public class Category {

    @Id
    private UUID id;

    @NotNull
    private String name;

    private String description;

    @ManyToMany(mappedBy = "categories")
    private Collection<Product> products;

    @PrePersist
    public void init() {
        if (id == null) id = UUID.randomUUID();
    }
}
