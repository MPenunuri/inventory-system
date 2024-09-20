package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "categories")
public class CategoryEntity {
    private Long user_id;
    @Id
    private Long id;
    private String name;
}
