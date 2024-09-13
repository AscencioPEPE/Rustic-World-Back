package com.rusticworld.app.dao;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductListPaginatedDAO {
    @Schema(description = "Index where the results begin", example = "1")
    private Integer starIndex;
    @Schema(description = "Last index where the results ends", example = "5")
    private Integer endIndex;
    @Schema(description = "Number of results", example = "1")
    private Integer count;
    @Schema(description = "Number of page", example = "1")
    private Integer page;
    @Schema(description = "Number of pages", example = "1")
    private Integer pages;
    @Schema(description = "Number of products per page", example = "1")
    private Integer totalProductsPage;
    @ArraySchema(schema = @Schema(implementation = ProductDAO.class), arraySchema = @Schema(description = "List of products obtained by filters"))
    private List<ProductDAO> products;
}
