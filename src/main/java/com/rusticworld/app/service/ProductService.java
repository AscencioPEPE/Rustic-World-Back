package com.rusticworld.app.service;

import com.rusticworld.app.dao.ProductDAO;
import com.rusticworld.app.dao.ProductListPaginatedDAO;
import com.rusticworld.app.dao.VariantDAO;
import com.rusticworld.app.dto.ProductDTO;
import com.rusticworld.app.model.ProductEntity;
import com.rusticworld.app.model.VariantProductEntity;
import com.rusticworld.app.repository.ProductRepository;
import com.rusticworld.app.repository.VariantProductRepository;
import com.rusticworld.app.utils.ProductSpecification;
import com.rusticworld.app.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;
    private VariantProductRepository variantProductRepository;

    private static final String PRODUCT_MISTAKE = "Product with SKU ";

    public ResponseEntity<Object> getAll(List<String> categories, String priceOrder, Integer limit, Integer page, String namePrefix) {
        Specification<ProductEntity> spec = Specification.where(null);
        if (categories != null && !categories.isEmpty()) {
            spec = spec.and(ProductSpecification.hasCategories(categories));
        }
        if (namePrefix != null && !namePrefix.isEmpty()) {
            spec = spec.and(ProductSpecification.nameStartsWith(namePrefix));
        }
        Sort sort;
        if ("desc".equalsIgnoreCase(priceOrder)) {
            sort = Sort.by(Sort.Order.desc("price"));
        } else {
            sort = Sort.by(Sort.Order.asc("price"));
        }
        sort = sort.and(Sort.by(Sort.Order.asc("sku")));
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
        Page<ProductEntity> productPage = productRepository.findAll(spec, pageRequest);
        List<ProductDAO> listProductDAO = productPage.getContent().stream()
                .map(this::toDAO).toList();
        ProductListPaginatedDAO response = ProductListPaginatedDAO.builder()
                .starIndex((int) productPage.getPageable().getOffset())
                .endIndex((productPage.getNumberOfElements() > 0)
                        ? (int) (productPage.getPageable().getOffset() + productPage.getNumberOfElements() - 1)
                        : (int) productPage.getPageable().getOffset())
                .count((int) productPage.getTotalElements())
                .page(productPage.getNumber())
                .pages(productPage.getTotalPages())
                .totalProductsPage(productPage.getNumberOfElements())
                .products(listProductDAO)
                .build();
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Object> save(ProductDTO productDTO) {
        Optional<ProductEntity> productOpt = productRepository.findBySku(productDTO.getSku());
        if (productOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(PRODUCT_MISTAKE + productDTO.getSku() + " already exists.");
        }
        return ResponseEntity.ok(toDAO(productRepository.save(fromDTO(productDTO))));
    }

    public ResponseEntity<Object> get(Long sku) {
        Optional<ProductEntity> productOpt = productRepository.findBySku(sku);
        return productOpt.<ResponseEntity<Object>>map(product -> ResponseEntity.ok().body(toDAO(product))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(StringUtils.PRODUCT_NOT_FOUND + sku));
    }

    public ResponseEntity<Object> delete(Long sku) {
        Optional<ProductEntity> productOpt = productRepository.findBySku(sku);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StringUtils.PRODUCT_NOT_FOUND + sku);
        }
        ProductEntity product = productOpt.get();
        productRepository.delete(product);
        return ResponseEntity.ok().body(StringUtils.PRODUCT_DELETED);
    }

    @Transactional
    public ResponseEntity<Object> update(Long skuExisting, ProductDTO productDTO) {
        Optional<ProductEntity> productOpt = productRepository.findBySku(skuExisting);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(PRODUCT_MISTAKE + skuExisting + " not found.");
        }
        ProductEntity existingProduct = productOpt.get();
        if (!skuExisting.equals(productDTO.getSku())) {
            Optional<ProductEntity> conflictingProduct = productRepository.findBySku(productDTO.getSku());
            if (conflictingProduct.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(PRODUCT_MISTAKE + productDTO.getSku() + " already exists.");
            }
        }
        existingProduct.setName(productDTO.getName());
        existingProduct.setSku(productDTO.getSku());
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setSize(productDTO.getSize());
        existingProduct.setWeight(productDTO.getWeight());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setPriceUnitary(productDTO.getPriceUnitary());
        existingProduct.setPriceWholesale(productDTO.getPriceWholesale());
        existingProduct.setQuantity(productDTO.getQuantity());
        if (productDTO.getImage() != null) {
            existingProduct.setImage(productDTO.getImage());
        }
        if (productDTO.getVariants() != null) {
            List<VariantProductEntity> newVariants = productDTO.getVariants().stream()
                    .map(variantDTO -> VariantProductEntity.builder()
                            .variantName(variantDTO.getVariantName())
                            .variantColor(variantDTO.getVariantColor())
                            .variantImage(variantDTO.getVariantImage())
                            .build())
                    .toList();
            existingProduct.getVariants().clear();
            existingProduct.getVariants().addAll(newVariants);
        }
        ProductEntity updatedProduct = productRepository.save(existingProduct);
        return ResponseEntity.ok(toDAO(updatedProduct));
    }

    private ProductEntity fromDTO(ProductDTO productDTO) {
        ProductEntity product = ProductEntity.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .category(productDTO.getCategory())
                .description(productDTO.getDescription())
                .size(productDTO.getSize())
                .weight(productDTO.getWeight())
                .price(productDTO.getPrice())
                .priceUnitary(productDTO.getPriceUnitary())
                .priceWholesale(productDTO.getPriceWholesale())
                .quantity(productDTO.getQuantity())
                .image(productDTO.getImage())
                .build();
        product.setVariants(productDTO.getVariants() != null
                ? productDTO.getVariants().stream()
                .map(variantDTO -> VariantProductEntity.builder()
                        .variantName(variantDTO.getVariantName())
                        .variantColor(variantDTO.getVariantColor())
                        .variantImage(variantDTO.getVariantImage())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new))
                : new ArrayList<>());
        return product;
    }


    private ProductDAO toDAO(ProductEntity product) {
        return ProductDAO.builder()
                .name(product.getName())
                .sku(product.getSku())
                .category(product.getCategory())
                .description(product.getDescription())
                .size(product.getSize())
                .weight(product.getWeight())
                .price(product.getPrice())
                .priceUnitary(product.getPriceUnitary())
                .priceWholesale(product.getPriceWholesale())
                .quantity(product.getQuantity())
                .image(product.getImage())
                .variants(product.getVariants() != null
                        ? product.getVariants().stream()
                        .map(variant -> VariantDAO.builder()
                                .variantName(variant.getVariantName())
                                .variantColor(variant.getVariantColor())
                                .variantImage(variant.getVariantImage())
                                .build())
                        .collect(Collectors.toCollection(ArrayList::new))
                        : new ArrayList<>())
                .build();
    }

}
