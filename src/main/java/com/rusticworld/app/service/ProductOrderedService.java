package com.rusticworld.app.service;

import com.rusticworld.app.dto.ProductOrderedDTO;
import com.rusticworld.app.model.ProductOrderedEntity;
import com.rusticworld.app.repository.ProductOrderedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductOrderedService {
    private ProductOrderedRepository productOrderedRepository;

    public ProductOrderedEntity save(ProductOrderedDTO productDTO) {
        return productOrderedRepository.save(fromDTO(productDTO));
    }

    public ProductOrderedEntity get(Long sku) {
        return productOrderedRepository.findBySku(sku)
                .orElse(null);
    }

    public void delete(Long sku) {
        productOrderedRepository.findBySku(sku)
                .ifPresent(product -> productOrderedRepository.delete(product));
    }

    private ProductOrderedEntity fromDTO(ProductOrderedDTO product){
        return ProductOrderedEntity.builder()
                .name(product.getName())
                .sku(product.getSku())
                .category(product.getCategory())
                .description(product.getDescription())
                .price(product.getPrice())
                .priceUnitary(product.getPriceUnitary())
                .priceWholesale(product.getPriceWholesale())
                .quantity(product.getQuantity())
                .image(product.getImage())
                .build();
    }
}
