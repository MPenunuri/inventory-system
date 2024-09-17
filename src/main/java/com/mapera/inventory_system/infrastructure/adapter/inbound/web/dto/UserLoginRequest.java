package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotNull(message = "email cannot be null")
    @Email(message = "Email should be valid")
    String email;
    @NotNull(message = "password cannot be null")
    @Size(min = 1, max = 255, message = "Password must be between 1 and 255 characters")
    String password;
}
