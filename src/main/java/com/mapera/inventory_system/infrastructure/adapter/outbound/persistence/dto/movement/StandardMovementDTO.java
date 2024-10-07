package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class StandardMovementDTO {
    private Long movementId;
    private Long productId;
    private String productName;
    private String productPresentation;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateTime;
    private String type;
    private String subtype;
    private String reason;
    private String comment;
    private int quantity;
}
