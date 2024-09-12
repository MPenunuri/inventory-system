package com.mapera.inventory_system.domain.valueobject;

public enum CostType {
    PER_UNIT,
    OVERALL;

    public static CostType fromString(String value) {
        switch (value.toUpperCase()) {
            case "PER_UNIT":
                return PER_UNIT;
            case "OVERALL":
                return OVERALL;
            default:
                throw new IllegalArgumentException("Unknown CostType: " + value);
        }
    }
}
