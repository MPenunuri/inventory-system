package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.subcategory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterSubcategoryRequest {
    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;
}
