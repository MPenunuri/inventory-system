package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.currency;

import lombok.Data;

@Data
public class CurrencyDTO {
    private Long currencyId;
    private String currencyName;
    private int products;
    private int movements;
}
