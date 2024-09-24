package com.rusticworld.app.dao;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class ClientDAO {

    @Schema(description = "Name of the Client Max 100 characters", example = "Rounded Clay Pot")
    private String name;

    @Schema(description = "Name of the Client Max 100 characters", example = "Rounded Clay Pot")
    private String nameClient;

    @Schema(description = "Email of the Client Max 100 characters", example = "Rounded Clay Pot")
    private String email;

    @Schema(description = "Phone of the Client Max 100 characters", example = "Rounded Clay Pot")
    private String phone;

    @Schema(description = "Address of the Client Max 100 characters", example = "Rounded Clay Pot")
    private String address;

    @Schema(description = "Address of the Client Max 100 characters", example = "Rounded Clay Pot")
    private String zipCode;

    @Schema(description = "Address of the Client Max 100 characters", example = "Rounded Clay Pot")
    private String state;

    @Schema(description = "Address of the Client Max 100 characters", example = "Rounded Clay Pot")
    private String city;

    @Schema(description = "Address of the Client Max 100 characters", example = "Rounded Clay Pot")
    private String clientType;

    @Schema(description = "Tax ID of the Client Max 100 characters", example = "Rounded Clay Pot")
    private String taxId;

    @ArraySchema(schema = @Schema(implementation = OrderDAO.class), arraySchema = @Schema(description = "List of order of the client"))
    private List<OrderDAO> orders;
}
