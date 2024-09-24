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
public class ClientListPaginatedDAO {
    @Schema(description = "Index where the results begin", example = "1")
    private Integer startIndex;

    @Schema(description = "Last index where the results end", example = "5")
    private Integer endIndex;

    @Schema(description = "Number of results", example = "1")
    private Integer count;

    @Schema(description = "Current page number", example = "1")
    private Integer page;

    @Schema(description = "Total number of pages", example = "1")
    private Integer pages;

    @Schema(description = "Number of clients per page", example = "1")
    private Integer totalClientsPage;

    @ArraySchema(schema = @Schema(implementation = ClientDAO.class), arraySchema = @Schema(description = "List of clients obtained by filters"))
    private List<ClientDAO> clients;
}
