package org.addy.productservice.repository;

import org.addy.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByName(String name);
    List<Product> findByCategoriesId(UUID categoryId);
}
