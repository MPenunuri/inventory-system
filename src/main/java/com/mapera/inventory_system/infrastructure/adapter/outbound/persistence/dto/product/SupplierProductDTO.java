package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product;

import lombok.Data;

@Data
public class SupplierProductDTO {
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
    private Long supplierId;
    private String supplierName;
}
