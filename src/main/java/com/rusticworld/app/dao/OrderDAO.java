package com.rusticworld.app.dao;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDAO {

    @Schema(description = "id of the Order", example = "1L")
    private String code;

    @Schema(description = "id of the Order", example = "1L")
    private String status;

    @Schema(description = "id of the Order", example = "1L")
    private Double total;

    @Schema(description = "id of the Order", example = "1L")
    private LocalDate registerDate;

    @Schema(description = "id of the Order", example = "1L")
    private LocalDate paymentDate;

    @Schema(description = "id of the Order", example = "1L")
    private LocalDate updateDate;

    @Schema(description = "Client details associated order")
    private ClientDAO client;

    @ArraySchema(schema = @Schema(implementation = ProductDAO.class), arraySchema = @Schema(description = "List of product of the order"))
    private List<ProductDAO> products;
}
