package com.mapera.inventory_system.domain.valueobject;

public class Sell {
    private final double value;
    private final String currency;
    private final SellType type;

    public Sell(double value, String currency, SellType type) {
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

    public SellType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "{" +
                " value='" + getValue() + "'" +
                ", currency='" + getCurrency() + "'" +
                ", type='" + getType() + "'" +
                "}";
    }

}
