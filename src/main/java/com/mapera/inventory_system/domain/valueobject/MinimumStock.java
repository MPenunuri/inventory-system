package com.mapera.inventory_system.domain.valueobject;

public class MinimumStock {
    private int stock;
    private boolean registered;

    public MinimumStock() {
        this.stock = 0;
        this.registered = false;
    }

    public MinimumStock(int stock) {
        this.stock = stock;
        this.registered = true;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isRegistered() {
        return this.registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "{" +
                " stock='" + getStock() + "'" +
                ", registered='" + isRegistered() + "'" +
                "}";
    }
}
