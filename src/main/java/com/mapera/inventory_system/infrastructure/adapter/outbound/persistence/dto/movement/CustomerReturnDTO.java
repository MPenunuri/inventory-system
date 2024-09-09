package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CustomerReturnDTO {
    private Long movementId;
    private Long productId;
    private String productName;
    private String productPresentation;
    private LocalDateTime dateTime;
    private String type;
    private String subtype;
    private String reason;
    private String comment;
    private int quantity;
    private Long toLocationId;
    private String toLocationName;
    private String costType;
    private double cost;
    private String costCurrency;
}
