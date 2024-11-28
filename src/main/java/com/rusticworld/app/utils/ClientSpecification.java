package com.rusticworld.app.utils;

import com.rusticworld.app.model.ClientEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ClientSpecification {
    private ClientSpecification() {
    }

    public static Specification<ClientEntity> nameStartsWith(String prefix) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), prefix + "%");
    }

    public static Specification<ClientEntity> nameClientStartsWith(String prefix) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("nameClient"), prefix + "%");
    }


    public static Specification<ClientEntity> hasClientType(List<String> clientType) {
        return (root, query, builder) -> {
            if (clientType == null || clientType.isEmpty()) {
                return builder.conjunction();
            }
            return root.get("clientType").in(clientType);
        };
    }
}
