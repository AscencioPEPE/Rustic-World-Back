package com.rusticworld.app.service;

import com.rusticworld.app.dao.ProductDAO;
import com.rusticworld.app.dao.ProductListPaginatedDAO;
import com.rusticworld.app.dao.VariantDAO;
import com.rusticworld.app.dto.ProductDTO;
import com.rusticworld.app.exception.ProductException;
import com.rusticworld.app.model.ProductEntity;
import com.rusticworld.app.model.VariantProductEntity;
import com.rusticworld.app.repository.ProductRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;

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
                    .body("Product with SKU " + productDTO.getSku() + " already exists.");
        }
        try {
            ProductEntity product = fromDTO(productDTO);
            if (productDTO.getVariants() != null && !productDTO.getVariants().isEmpty()) {
                List<VariantProductEntity> variants = productDTO.getVariants().stream()
                        .map(variantDTO -> {
                            try {
                                return VariantProductEntity.builder()
                                        .variantName(variantDTO.getVariantName())
                                        .variantColor(variantDTO.getVariantColor())
                                        .variantImage(variantDTO.getVariantImage() != null ? variantDTO.getVariantImage(): null)
                                        .product(product)
                                        .build();
                            } catch (Exception e) {
                                throw new RuntimeException("Error processing variant image: " + e.getMessage(), e);
                            }
                        }).toList();
                product.setVariants(variants);
            }
            ProductEntity savedProduct = productRepository.save(product);
            return ResponseEntity.ok(toDAO(savedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving product: " + e.getMessage());
        }
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
        productRepository.delete(productOpt.get());
        return ResponseEntity.ok().body(StringUtils.PRODUCT_DELETED);
    }

    @Transactional
    public ResponseEntity<Object> update(Long skuExisting, ProductDTO productDTO) {
        Optional<ProductEntity> productOpt = productRepository.findBySku(skuExisting);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StringUtils.PRODUCT_NOT_FOUND + productDTO.getSku());
        }

        ProductEntity productEntity = productOpt.get();
        productEntity.setName(productDTO.getName());
        productEntity.setSku(productDTO.getSku());
        productEntity.setCategory(productDTO.getCategory());
        productEntity.setDescription(productDTO.getDescription());
        productEntity.setSize(productDTO.getSize());
        productEntity.setWeight(productDTO.getWeight());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setPriceUnitary(productDTO.getPriceUnitary());
        productEntity.setPriceWholesale(productDTO.getPriceWholesale());
        productEntity.setQuantity(productDTO.getQuantity());
        if (productDTO.getImage() != null && !productDTO.getImage().isEmpty()) {
            productEntity.setImage(convertMultipartFileToBytes(productDTO.getImage()));
        }
        productRepository.save(productEntity);

        return ResponseEntity.ok().body(toDAO(productEntity));
    }


    private ProductEntity fromDTO(ProductDTO product) {
        return ProductEntity.builder()
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
                .image(convertMultipartFileToBytes(product.getImage()))
                .build();
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
                .image(product.getImage()) // Puedes convertir a URL si es necesario
                .variants(product.getVariants() != null
                        ? product.getVariants().stream()
                        .map(variant -> VariantDAO.builder()
                                .variantName(variant.getVariantName())
                                .variantColor(variant.getVariantColor())
                                .variantImage(variant.getVariantImage())
                                .build())
                        .toList()
                        : List.of()) // Devuelve una lista vacía si es null
                .build();
    }


    private byte[] convertMultipartFileToBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new ProductException("Error converting file to byte array", e);
        }
    }

}
