package com.mapera.inventory_system.domain.valueobject;

public class SellingPrice {
    private final double retail;
    private final double wholesale;

    public SellingPrice(double retail, double wholesale) {
        this.retail = retail;
        this.wholesale = wholesale;
    }

    public double getRetail() {
        return this.retail;
    }

    public double getWholesale() {
        return this.wholesale;
    }

    public String toString() {
        return "{" +
                " retail='" + getRetail() + "'" +
                ", wholesale='" + getWholesale() + "'" +
                "}";
    }

}
