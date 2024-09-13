package com.rusticworld.app.utils;

import com.rusticworld.app.model.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {
    private ProductSpecification(){}
    public static Specification<ProductEntity> hasCategories(List<String> categories) {
        return (root, query, builder) -> {
            if (categories == null || categories.isEmpty()) {
                return builder.conjunction();
            }
            return root.get("category").in(categories);
        };
    }
}