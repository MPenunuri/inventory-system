package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.category;

import lombok.Data;

@Data
public class CategoriesAndSubcategoriesDTO {
    private Long categoryId;
    private String categoryName;
    private Long subcategoryId;
    private String subcategoryName;
    private int products;
}
