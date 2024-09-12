package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto;

import lombok.Data;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private Long subcategoryId;
    private String productPresentation;
    private Integer minimumStock;
    private Double retailPrice;
    private Double wholesalePrice;
    private Long priceCurrencyId;
}
