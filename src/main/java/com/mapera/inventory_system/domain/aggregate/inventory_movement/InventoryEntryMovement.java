package com.mapera.inventory_system.domain.aggregate.inventory_movement;

import java.time.ZonedDateTime;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.valueobject.Cost;
import com.mapera.inventory_system.domain.valueobject.EntryType;
import com.mapera.inventory_system.domain.entity.Location;

public class InventoryEntryMovement extends EntryMovement {

    public InventoryEntryMovement(int id, InventoryProduct product,
            ZonedDateTime time, EntryType subtype, String reason, int quantity, String comment,
            Location location, Cost cost) {
        super(id, product, time, subtype, reason, quantity, comment, location, cost);
    }

    public InventoryEntryMovement(int id, InventoryProduct product,
            ZonedDateTime time, EntryType subtype, String reason, int quantity, Location location,
            Cost cost) {
        super(id, product, time, subtype, reason, quantity, location, cost);
    }

    public boolean execute() {
        InventoryProduct product = getProduct();
        Location location = getLocation();
        product.stockManager.increseStockInLocation(location.getId(), getQuantity());
        return true;
    }

}
