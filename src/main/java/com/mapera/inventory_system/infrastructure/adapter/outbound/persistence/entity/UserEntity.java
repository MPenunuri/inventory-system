package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "users")
public class UserEntity {
    @Id
    private Long id;
    private String username;
    private String email;
    private String password;
    private String roles;
}
