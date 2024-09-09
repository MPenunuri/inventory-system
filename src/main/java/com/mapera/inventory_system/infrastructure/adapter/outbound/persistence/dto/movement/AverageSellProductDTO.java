package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement;

import lombok.Data;

@Data
public class AverageSellProductDTO {
    private Long productId;
    private String productName;
    private Long productCategoryId;
    private String productCategory;
    private Long productSubcategoryId;
    private String productSubcategory;
    private String productPresentation;
    private String sellType;
    private double averageSellValue;
    private String sellCurrency;
    private int sells;
}
