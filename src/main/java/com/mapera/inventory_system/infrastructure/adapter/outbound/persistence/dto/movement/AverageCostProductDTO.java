package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement;

import lombok.Data;

@Data
public class AverageCostProductDTO {
    private Long productId;
    private String productName;
    private Long productCategoryId;
    private String productCategory;
    private Long productSubcategoryId;
    private String productSubcategory;
    private String productPresentation;
    private String costType;
    private float averageCostValue;
    private String costCurrency;
}
