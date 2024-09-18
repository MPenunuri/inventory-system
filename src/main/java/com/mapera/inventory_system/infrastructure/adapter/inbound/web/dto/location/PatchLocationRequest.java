package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.location;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatchLocationRequest {
    @NotNull(message = "ID cannot be null")
    @Positive(message = "ID must be positive")
    private Long id;
    @Size(min = 1, max = 255, message = "Name must be beetween 1 and 255 characters")
    private String name;
    @Size(min = 1, max = 500, message = "Name must be beetween 1 and 500 characters")
    private String address;
}
