package com.mapera.inventory_system.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.mapera.inventory_system.domain.aggregate.inventory_movement.InventoryOutputMovement;
import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.domain.valueobject.OutputType;
import com.mapera.inventory_system.domain.valueobject.Sell;
import com.mapera.inventory_system.domain.valueobject.SellType;

public class InventoryOutputMovementTest {

    @Test
    public void test() {
        InventoryProduct product = CreateInventoryProduct.createSampleProduct();
        Location location = product.stockManager.getStockInLocation(0).getLocation();
        Sell sell = new Sell(product.getSellingPrice().getRetail(),
                product.getSellingPrice().getCurrency(),
                SellType.RETAIL);
        InventoryOutputMovement movement = new InventoryOutputMovement(
                0, product, LocalDateTime.now(), OutputType.SALES, "Common sale",
                10, location, sell);

        assertEquals(true, movement.execute());
    }
}
