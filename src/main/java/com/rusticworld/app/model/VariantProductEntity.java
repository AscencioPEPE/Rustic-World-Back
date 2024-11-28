package com.rusticworld.app.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product-variant")
public class VariantProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "variant_name", nullable = false)
    private String variantName;

    @Column(name = "variant_color", nullable = false)
    private String variantColor;

    @Lob
    @Column(name = "variant_image", columnDefinition = "MEDIUMBLOB")
    private byte[] variantImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}
