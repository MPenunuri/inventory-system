package com.mapera.inventory_system.domain.aggregate.inventory_movement;

import java.time.LocalDateTime;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.domain.valueobject.TransferType;

public class TransferMovement extends Movement {
    private Location from;
    private Location to;

    public TransferMovement(int id, InventoryProduct product,
            LocalDateTime time, TransferType subtype, String reason, int quantity,
            String comment, Location from, Location to) {
        super(id, product, time, "transfer", subtype, reason, quantity, comment);
        this.from = from;
        this.to = to;
    }

    public TransferMovement(int id, InventoryProduct product,
            LocalDateTime time,
            TransferType subtype, String reason, int quantity,
            Location from, Location to) {
        super(id, product, time, "transfer", subtype, reason, quantity, null);
        this.from = from;
        this.to = to;
    }

    public Location getFrom() {
        return this.from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return this.to;
    }

    public void setTo(Location to) {
        this.to = to;
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
                ", to='" + getFrom() + "'" +
                ", from='" + getTo() + "'" +
                "}";
    }

}
