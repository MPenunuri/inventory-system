package com.mapera.inventory_system.domain.valueobject;

public enum SellType {
    RETAIL,
    WHOLESALE,
    NO_SALE;

    public static SellType fromString(String value) {
        switch (value.toUpperCase()) {
            case "RETAIL":
                return RETAIL;
            case "WHOLESALE":
                return WHOLESALE;
            case "NO_SALE":
                return NO_SALE;
            default:
                throw new IllegalArgumentException("Unknown SellType: " + value);
        }
    }

}
