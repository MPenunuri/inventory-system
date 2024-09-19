package com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.movement;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterMovementRequest {
    @NotNull(message = "Product ID cannot be null")
    @Positive(message = "Product ID must be positive")
    private Long productId;
    @NotNull(message = "Date time cannot be null")
    private LocalDateTime dateTime;
    @NotNull(message = "Reason cannot be null")
    @Size(min = 1, max = 500, message = "Reason must be beetween 1 and 500 characters")
    private String reason;
    @NotNull(message = "Comment cannot be null")
    private String comment;
    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be positive")
    private int quantity;
    @Positive(message = "Supplier ID must be positive")
    Long supplierId;
    @Positive(message = "Location ID must be positive")
    private Long fromLocationId;
    @Positive(message = "Location ID must be positive")
    private Long toLocationId;
    @Size(min = 1, max = 50, message = "Transaction subtype must be beetween 1 and 50 characters")
    private String transactionSubtype;
    @Positive(message = "Transaction value must be positive")
    private double transactionValue;
    @Positive(message = "Currency ID must be positive")
    private Long transactionCurrencyId;
}
