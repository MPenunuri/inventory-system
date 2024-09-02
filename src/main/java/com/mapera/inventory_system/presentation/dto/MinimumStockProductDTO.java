package com.mapera.inventory_system.presentation.dto;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class MinimumStockProductDTO {
    @Id
    private Long id;
    private String name;
    private String productPresentation;
    private int minimumStock;
    private int totalStock;
}
