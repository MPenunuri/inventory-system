package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.currency;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterCurrencyRequest {
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 255, message = "Name must be beetween 1 and 255 characters")
    private String name;
}
