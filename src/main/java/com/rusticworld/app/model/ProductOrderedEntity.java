package com.rusticworld.app.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_ordered")
public class ProductOrderedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sku", nullable = false)
    private Long sku;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "price_unitary", nullable = false)
    private Double priceUnitary;

    @Column(name = "price_wholesale", nullable = false)
    private Double priceWholesale;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "image_url")
    private String image;
}
