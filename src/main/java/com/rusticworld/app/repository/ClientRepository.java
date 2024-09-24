package com.rusticworld.app.repository;

import com.rusticworld.app.model.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository <ClientEntity,Long>, JpaSpecificationExecutor<ClientEntity> {
    ClientEntity findByName(String name);
}
