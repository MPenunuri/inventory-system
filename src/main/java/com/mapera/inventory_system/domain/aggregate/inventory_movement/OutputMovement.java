package com.mapera.inventory_system.domain.aggregate.inventory_movement;

import java.time.ZonedDateTime;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.domain.valueobject.Sell;

public class OutputMovement extends Movement {
    private Location location;
    private Sell sell;

    public OutputMovement(int id, InventoryProduct product,
            ZonedDateTime time, String reason, int quantity,
            String comment, Location location, Sell sell) {
        super(id, product, time, "output", null, reason, quantity, comment);
        this.subtype = sell.getType().toString();
        this.location = location;
        this.sell = sell;
    }

    public OutputMovement(int id, InventoryProduct product,
            ZonedDateTime time, String reason, int quantity,
            Location location, Sell sell) {
        super(id, product, time, "output", null, reason, quantity, null);
        this.subtype = sell.getType().toString();
        this.location = location;
        this.sell = sell;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Sell getSell() {
        return this.sell;
    }

    public void setSell(Sell sell) {
        this.sell = sell;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", product='" + getProduct() + "'" +
                ", time='" + getTime() + "'" +
                ", type='" + getType() + "'" +
                ", subtype='" + getSubtype() + "'" +
                ", reason='" + getReason() + "'" +
                ", quantity='" + getQuantity() + "'" +
                ", comment='" + getComment() + "'" +
                ", location='" + getLocation() + "'" +
                ", sell='" + getSell() + "'" +
                "}";
    }
}
