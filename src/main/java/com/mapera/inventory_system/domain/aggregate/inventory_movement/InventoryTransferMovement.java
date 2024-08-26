package com.mapera.inventory_system.domain.aggregate.inventory_movement;

import java.time.ZonedDateTime;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.entity.Location;

public class InventoryTransferMovement extends TransferMovement {

    public InventoryTransferMovement(int id, InventoryProduct product,
            ZonedDateTime time, String reason, int quantity,
            String comment, Location from, Location to) {
        super(id, product, time, reason, quantity, comment, from, to);
    }

    public InventoryTransferMovement(int id, InventoryProduct product,
            ZonedDateTime time, String reason, int quantity,
            Location from, Location to) {
        super(id, product, time, reason, quantity, from, to);
    }

    public boolean execute() {
        InventoryProduct product = getProduct();
        Location from = getFrom();
        Location to = getTo();
        product.stockManager.decreaseStockInLocation(from.getId(), getQuantity());
        product.stockManager.increseStockInLocation(to.getId(), getQuantity());
        return true;
    }

}
