package com.mapera.inventory_system.domain.valueobject;

public class SellingPrice {
    private final double retail;
    private final double wholesale;
    private final String currency;

    public SellingPrice(double retail, double wholesale, String currency) {
        this.retail = retail;
        this.wholesale = wholesale;
        this.currency = currency;
    }

    public double getRetail() {
        return this.retail;
    }

    public double getWholesale() {
        return this.wholesale;
    }

    public String getCurrency() {
        return this.currency;
    }

    @Override
    public String toString() {
        return "{" +
                " retail='" + getRetail() + "'" +
                ", wholesale='" + getWholesale() + "'" +
                ", currency='" + getCurrency() + "'" +
                "}";
    }

}
