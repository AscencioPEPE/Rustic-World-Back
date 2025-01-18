package com.rusticworld.app.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_variant")
public class VariantProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "variant_name")
    private String variantName;

    @Column(name = "variant_color")
    private String variantColor;

    @Lob
    @Column(name = "variant_image", columnDefinition = "MEDIUMBLOB")
    private byte[] variantImage;
}
