package com.mapera.inventory_system.domain.valueobject;

public class MaximumStorage {
    private int value;
    private boolean registered;

    public MaximumStorage() {
        this.value = 0;
        this.registered = false;
    }

    public MaximumStorage(int value) {
        this.value = value;
        this.registered = true;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
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
                " value='" + getValue() + "'" +
                ", registered='" + isRegistered() + "'" +
                "}";
    }

}
