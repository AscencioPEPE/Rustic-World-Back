package com.rusticworld.app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantDTO {
    private String variantName;
    private String variantColor;
    private byte[] variantImage;
}
