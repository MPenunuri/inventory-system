package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "products")
public class ProductEntity {
    @Id
    private Long id;
    private String name;
    private Long subcategory_id;
    private String productPresentation;
    private int minimumStock;
    private double retail_price;
    private double wholesale_price;
    private String price_currency;
}
