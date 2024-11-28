package com.rusticworld.app.dao;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantDAO {
    private String variantName;
    private String variantColor;
    private byte[] variantImage;
}
