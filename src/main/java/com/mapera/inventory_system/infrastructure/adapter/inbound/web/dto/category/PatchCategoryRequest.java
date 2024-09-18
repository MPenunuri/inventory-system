package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatchCategoryRequest {
    @NotNull(message = "ID cannot be null")
    @Positive(message = "ID cannot be negative")
    private Long id;
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 255, message = "Invalid name size. Must be between 1 and 255 characters")
    private String name;
}
