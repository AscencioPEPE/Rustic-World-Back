package com.rusticworld.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOrderedDTO {

    @Schema(description = "Name of the product Max 100 characters", example = "Rounded Clay Pot")
    private String name;

    @Schema(description = "sku of the product", example = "Combine name with color")
    private Long sku;

    @Schema(description = "category of the product", example = "Combine name with color")
    private String category;

    @Schema(description = "description of the product (optional)", example = "With any aggregations etc...")
    private String description;

    @Schema(description = "Price of the product in dollar with cents", example = "1500.00")
    private Double price;

    @Schema(description = "Unitary price of the product in dollar with cents", example = "1500.00")
    private Double priceUnitary;

    @Schema(description = "Wholesale Price of the product in dollar with cents", example = "1500.00")
    private Double priceWholesale;

    @Schema(description = "Quantity of the product (optional)", example = "1500.00")
    private Integer quantity;

    @Schema(description = "Url of the product saved locally", example = "C:\\Users\\pepej\\Desktop\\Artetermos.com-master")
    private String image;
}
