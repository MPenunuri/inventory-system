package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "subcategories")
public class SubcategoryEntity {
    @Id
    private final Long id;
    private final Long category;
    private String name;
}
