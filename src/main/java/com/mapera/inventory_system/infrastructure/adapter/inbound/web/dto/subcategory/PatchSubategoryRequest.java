package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.subcategory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatchSubategoryRequest {
    @NotNull(message = "ID cannot be null")
    @Positive(message = "ID must be posititve")
    private Long id;
    @Positive(message = "Category ID must be posititve")
    private Long categoryId;
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

}
