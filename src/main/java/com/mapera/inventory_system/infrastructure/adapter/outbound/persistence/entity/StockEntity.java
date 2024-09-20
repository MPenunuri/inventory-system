package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "stock_list")
public class StockEntity {
    private Long user_id;
    @Id
    private Long id;
    private Long location_id;
    private Long product_id;
    private int quantity;
    private int maximumStorage;
}
