package com.mapera.inventory_system.domain.entity;

import com.mapera.inventory_system.domain.valueobject.MaximumStorage;

public class Stock {
    private final int id;
    private final Location location;
    private int quantity;
    private MaximumStorage maximumStorage;

    public Stock(int id, Location location, int quantity, int maximumStorage) {
        this.id = id;
        this.location = location;
        this.quantity = quantity;
        this.maximumStorage = new MaximumStorage(maximumStorage);
    }

    public Stock(int id, Location location, int quantity) {
        this.id = id;
        this.location = location;
        this.quantity = quantity;
        this.maximumStorage = new MaximumStorage();
    }

    public boolean hasAvailableStorage() {
        return !maximumStorageRegistered() ||
                this.quantity < getMaximumStorage();
    }

    public void increase(int quantity) throws IllegalArgumentException, IllegalStateException {
        if (quantity < 0) {
            throw new IllegalArgumentException("The quantity to increase cannot be negative.");
        }
        if (maximumStorageRegistered() &&
                this.quantity + quantity > getMaximumStorage()) {
            throw new IllegalStateException("Cannot increase quantity beyond the maximum storage limit.");
        }
        this.quantity += quantity;
    }

    public void decrease(int quantity) throws IllegalArgumentException {
        if (quantity < 0) {
            throw new IllegalArgumentException("The quantity to decrease cannot be negative.");
        }
        if (this.quantity - quantity < 0) {
            throw new IllegalArgumentException("Cannot decrease quantity below zero.");
        }
        this.quantity -= quantity;
    }

    public int getId() {
        return this.id;
    }

    public Location getLocation() {
        return this.location;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean maximumStorageRegistered() {
        return this.maximumStorage.isRegistered();
    }

    public int getMaximumStorage() {
        return this.maximumStorage.getValue();
    }

    public void setMaximumStorage(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("The quantity  cannot be negative.");
        }
        if (quantity == 0) {
            this.maximumStorage.setRegistered(false);
        }
        if (quantity > 0) {
            this.maximumStorage.setRegistered(true);
        }
        this.maximumStorage.setValue(quantity);
        this.maximumStorage.setRegistered(false);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", location='" + getLocation() + "'" +
                ", quantity='" + getQuantity() + "'" +
                ", maximumStorage='" + getMaximumStorage() + "'" +
                "}";
    }
}
