package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "product_supplier")
public class ProductSupplierEntity {
    @Id
    private Long id;
    private Long productId;
    private Long supplierId;
}
