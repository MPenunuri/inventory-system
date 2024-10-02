package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.location;

import lombok.Data;

@Data
public class LocationDTO {
    private Long locationId;
    private String locationName;
    private String locationAddress;
    private int products;
    private int movements;

}
