package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.supplier;

import lombok.Data;

@Data
public class SupplierDTO {
    private Long supplierId;
    private String supplierName;
    private int products;
    private int movements;
}
