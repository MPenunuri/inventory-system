package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductSupplierRelationRequest {
    @NotNull(message = "Supplier ID cannot be null")
    @Positive(message = "Supplier ID must be positive")
    private Long supplierId;
    @NotNull(message = "Product ID cannot be null")
    @Positive(message = "Product ID must be positive")
    private Long productId;
}
