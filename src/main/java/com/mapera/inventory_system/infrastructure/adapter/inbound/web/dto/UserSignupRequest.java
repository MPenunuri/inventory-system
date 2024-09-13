package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto;

import lombok.Data;

@Data
public class UserSignupRequest {
    String username;
    String email;
    String password;
}
