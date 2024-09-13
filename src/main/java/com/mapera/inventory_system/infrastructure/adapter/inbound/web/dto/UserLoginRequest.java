package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
    String email;
    String password;
}
