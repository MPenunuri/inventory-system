package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product;

import lombok.Data;

@Data
public class NoSupplierProductDTO {
    private Long productId;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private Long subcategoryId;
    private String subcategoryName;
    private String productPresentation;
}
