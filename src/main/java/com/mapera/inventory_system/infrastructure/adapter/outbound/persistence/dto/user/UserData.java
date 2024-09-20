package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.user;

import lombok.Data;

@Data
public class UserData {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Integer registeredCategories;
    private Integer registeredSubcategories;
    private Integer registeredProducts;
    private Integer registeredMovements;
    private Integer registeredLocations;
    private Integer registeredStocks;
    private Integer registeredSuppliers;
    private Integer registeredProductSupplierRelations;
    private Integer registeredCurrencies;
}
