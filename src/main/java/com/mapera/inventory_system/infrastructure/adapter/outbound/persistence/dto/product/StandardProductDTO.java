package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product;

import lombok.Data;

@Data
public class StandardProductDTO {
    private Long userId;
    private Long productId;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private Long subcategoryId;
    private String subcategoryName;
    private String productPresentation;
    private int minimumStock;
    private int totalStock;
    private double retailPrice;
    private double wholesalePrice;
    private String priceCurrency;
}