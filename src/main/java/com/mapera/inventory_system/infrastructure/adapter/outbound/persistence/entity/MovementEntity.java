package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "movements")
public class MovementEntity {
    private Long user_id;
    @Id
    private Long id;
    private Long product_id;
    private LocalDateTime date_time;
    private String type;
    private String subtype;
    private String reason;
    private String comment;
    private int quantity;
    private Long supplier_id;
    private Long from_location_id;
    private Long to_location_id;
    private String transaction_type;
    private String transaction_subtype;
    private Double transaction_value;
    private Long transaction_currency_id;
}
