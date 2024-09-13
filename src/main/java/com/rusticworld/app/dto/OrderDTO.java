package com.rusticworld.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    @Schema(description = "id of the Order", example = "1L")
    private String code;
}
