package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "products")
public class ProductEntity {
    private Long user_id;
    @Id
    private Long id;
    private String name;
    private Long subcategory_id;
    private String product_presentation;
    private int minimum_stock;
    private double retail_price;
    private double wholesale_price;
    private Long price_currency_id;
}
