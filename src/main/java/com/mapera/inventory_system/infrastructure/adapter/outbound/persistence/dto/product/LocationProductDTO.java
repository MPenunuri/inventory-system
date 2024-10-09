package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product;

import lombok.Data;

@Data
public class LocationProductDTO {
    private Long productId;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private Long subcategoryId;
    private String subcategoryName;
    private String productPresentation;
    private int minimumStock;
    private double retailPrice;
    private double wholesalePrice;
    private Long priceCurrencyId;
    private String priceCurrency;
    private Long stockLocationId;
    private String stockLocationName;
    private String stockLocationAddress;
    private int stockLocationQuantity;
    private int stockLocationMaximumStorage;
}
