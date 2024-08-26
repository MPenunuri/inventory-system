package com.mapera.inventory_system.domain.valueobject;

public final class Cost {
    private final double value;
    private final String currency;
    private final CostType type;

    public Cost(double value, String currency, CostType type) {
        this.value = value;
        this.currency = currency;
        this.type = type;
    }

    public double getValue() {
        return this.value;
    }

    public String getCurrency() {
        return this.currency;
    }

    public CostType getType() {
        return this.type;
    }

    public String toString() {
        return "{" +
                " value='" + getValue() + "'" +
                ", currency='" + getCurrency() + "'" +
                ", type='" + getType() + "'" +
                "}";
    }

}
