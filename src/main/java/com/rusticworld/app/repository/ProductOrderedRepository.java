package com.rusticworld.app.repository;

import com.rusticworld.app.model.ProductOrderedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductOrderedRepository extends JpaRepository <ProductOrderedEntity,Long> {
    Optional<ProductOrderedEntity> findBySku(Long sku);
}
