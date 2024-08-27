package com.mapera.inventory_system.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import com.mapera.inventory_system.domain.aggregate.inventory_movement.InventoryTransferMovement;
import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.domain.valueobject.TransferType;

public class InventoryTransferMovementTest {

    @Test
    public void test() {
        InventoryProduct product = CreateInventoryProduct.createSampleProduct();
        Location from = product.stockManager.getStockInLocation(0).getLocation();
        Location to = product.stockManager.getStockInLocation(1).getLocation();
        InventoryTransferMovement movement = new InventoryTransferMovement(0, product,
                ZonedDateTime.now(), TransferType.NONE,
                "Maximise the use of space", 10, from, to);
        assertEquals(true, movement.execute());
    }
}
