package com.mapera.inventory_system.domain.aggregate.inventory_movement;

import java.time.ZonedDateTime;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.domain.valueobject.Cost;
import com.mapera.inventory_system.domain.valueobject.EntryType;

public class EntryMovement extends Movement {
    private Location Location;
    private Cost cost;

    public EntryMovement(int id, InventoryProduct product,
            ZonedDateTime time, EntryType subtype, String reason, int quantity,
            String comment, Location location, Cost cost) {
        super(id, product, time, "entry", subtype, reason, quantity, comment);
        this.Location = location;
        this.cost = cost;
    }

    public EntryMovement(int id, InventoryProduct product,
            ZonedDateTime time, EntryType subtype, String reason, int quantity, Location location, Cost cost) {
        super(id, product, time, "entry", subtype, reason, quantity, null);
        this.Location = location;
        this.cost = cost;
    }

    public Location getLocation() {
        return this.Location;
    }

    public void setLocation(Location Location) {
        this.Location = Location;
    }

    public Cost getCost() {
        return this.cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
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
                ", cost='" + getCost() + "'" +
                "}";
    }

}
