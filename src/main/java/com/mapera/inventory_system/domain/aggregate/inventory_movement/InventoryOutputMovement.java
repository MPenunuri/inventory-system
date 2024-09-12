package com.mapera.inventory_system.domain.aggregate.inventory_movement;

import java.time.LocalDateTime;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.domain.valueobject.OutputType;
import com.mapera.inventory_system.domain.valueobject.Sell;

public class InventoryOutputMovement extends OutputMovement {

    public InventoryOutputMovement(int id,
            InventoryProduct product, LocalDateTime time,
            OutputType subtype, String reason, int quantity,
            String comment, Location location, Sell sell) {
        super(id, product, time, subtype, reason, quantity, comment, location, sell);
    }

    public InventoryOutputMovement(int id, InventoryProduct product,
            LocalDateTime time, OutputType subtype, String reason, int quantity,
            Location location, Sell sell) {
        super(id, product, time, subtype, reason, quantity, location, sell);
    }

    public boolean execute() {
        InventoryProduct product = getProduct();
        Location location = getLocation();
        product.stockManager.decreaseStockInLocation(location.getId(), getQuantity());
        return true;
    }

}
