package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SupplierReturnDTO {
    private Long movementId;
    private Long productId;
    private String productName;
    private String productPresentation;
    private LocalDateTime dateTime;
    private String type;
    private String subtype;
    private String reason;
    private String comment;
    private Long supplierId;
    private String supplierName;
    private int quantity;
    private Long fromLocationId;
    private String fromLocationName;
    private String refundType;
    private double refund;
    private String refundCurrency;
}
