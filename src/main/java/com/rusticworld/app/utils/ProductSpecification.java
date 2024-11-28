package com.rusticworld.app.utils;

import com.rusticworld.app.model.ProductEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {
    private ProductSpecification(){}
    public static Specification<ProductEntity> hasCategories(List<String> categories) {
        return (root, query, builder) -> {
            if (categories == null || categories.isEmpty()) {
                return builder.conjunction();
            }

            Predicate[] predicates = categories.stream()
                    .map(category -> builder.like(root.get("category"), "%" + category + "%"))
                    .toArray(Predicate[]::new);
            return builder.and(predicates);
        };
    }

    public static Specification<ProductEntity> nameStartsWith(String prefix) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), prefix + "%");
    }
}