package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SupplierPatchRequest {
    @NotNull(message = "ID cannot be null")
    @Positive(message = "ID must be positive")
    private Long id;
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

}
