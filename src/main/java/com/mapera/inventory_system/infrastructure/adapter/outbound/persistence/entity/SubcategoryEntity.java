package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "subcategories")
public class SubcategoryEntity {
    @Id
    private Long id;
    private Long category_id;
    private String name;
}
