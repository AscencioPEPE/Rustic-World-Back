package com.rusticworld.app.repository;

import com.rusticworld.app.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository <OrderEntity,Long> {
    Optional<OrderEntity> findByCode(String code);
}
