package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "stock_list")
public class StockEntity {
    @Id
    private final Long id;
    private final Long location_id;
    private final Long product_id;
    private int quantity;
    private int maximumStorage;
}
