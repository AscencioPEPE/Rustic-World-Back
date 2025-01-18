package com.rusticworld.app.repository;

import com.rusticworld.app.model.VariantProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantProductRepository extends JpaRepository<VariantProductEntity,Long> {
}
