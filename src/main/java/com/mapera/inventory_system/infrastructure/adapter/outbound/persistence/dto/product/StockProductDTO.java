package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class StockProductDTO {
    @Id
    private Long id;
    private String name;
    private String productPresentation;
    private int minimumStock;
    private int totalStock;
}
