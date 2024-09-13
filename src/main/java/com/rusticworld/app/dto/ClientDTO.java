package com.rusticworld.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDTO {

    @Schema(description = "Name of the Client Max 100 characters", example = "Rounded Clay Pot")
    private String name;

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

    @Schema(description = "Tax ID of the Client Max 100 characters", example = "Rounded Clay Pot")
    private String taxId;
}
