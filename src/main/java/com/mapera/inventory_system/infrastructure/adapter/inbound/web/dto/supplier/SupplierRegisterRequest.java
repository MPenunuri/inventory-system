package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class SupplierRegisterRequest {
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;
}
