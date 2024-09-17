package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductUpdateRequest {
    @NotNull(message = "ID cannot be null")
    @Positive(message = "ID must be positive")
    private Long id;
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;
    @Positive(message = "Subcategory ID must be positive")
    private Long subcategoryId;
    @Size(min = 1, max = 255, message = "Product presentation or description must be between 1 and 255 characters")
    private String productPresentation;
    @Positive(message = "Minimum stock must be positive")
    private Integer minimumStock;
    @Positive(message = "Retail price must be positive")
    private Double retailPrice;
    @Positive(message = "Wholesale price must be positive")
    private Double wholesalePrice;
    @Positive(message = "Price currency ID must be positive")
    private Long priceCurrencyId;
}
