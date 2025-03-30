package org.addy.productservice.repository;

import org.addy.productservice.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
    List<ProductImage> findByProductId(UUID productId);
    @Query("FROM ProductImage i WHERE i.product.id = ?1 AND i.isDefault")
    Optional<ProductImage> findProductDefault(UUID productId);
}
