package com.mapera.inventory_system.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.domain.valueobject.Cost;
import com.mapera.inventory_system.domain.valueobject.CostType;
import com.mapera.inventory_system.domain.valueobject.EntryType;
import com.mapera.inventory_system.domain.aggregate.inventory_movement.InventoryEntryMovement;

public class InventoryEntryMovementTest {

    @Test
    public void test() {
        InventoryProduct product = CreateInventoryProduct.createSampleProduct();
        Location location = product.stockManager.getStockInLocation(1).getLocation();
        CostType costType = CostType.PER_UNIT;
        Cost cost = new Cost(5.75, "USD", costType);
        InventoryEntryMovement movement = new InventoryEntryMovement(
                0, product, LocalDateTime.now(), EntryType.ACQUISITION, "Minimum provision",
                10, location, cost);

        assertEquals(true, movement.execute());
    }

}
