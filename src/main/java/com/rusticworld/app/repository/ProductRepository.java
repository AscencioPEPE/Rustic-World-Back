package com.rusticworld.app.repository;

import com.rusticworld.app.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository <ProductEntity,Long>, JpaSpecificationExecutor<ProductEntity> {
    Optional<ProductEntity> findBySku(Long sku);
}
