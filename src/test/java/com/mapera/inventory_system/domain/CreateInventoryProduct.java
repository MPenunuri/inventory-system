package com.mapera.inventory_system.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.entity.Category;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.domain.entity.Stock;
import com.mapera.inventory_system.domain.entity.Subcategory;
import com.mapera.inventory_system.domain.entity.Supplier;

public class CreateInventoryProduct {

    public static InventoryProduct createSampleProduct() {
        InventoryProduct product = new InventoryProduct(1, "Coca Cola");
        Category category = new Category(0, "Drink");
        Subcategory subcategory = new Subcategory(0, category, "Soda");
        product.setSubcategory(subcategory);
        Location location1 = new Location(0, "Central warehouse", "Liberty 183");
        Location location2 = new Location(1, "Alternative warehouse", "San Diego 3B");
        Stock stock1 = new Stock(0, location1, 40, 40);
        Stock stock2 = new Stock(1, location2, 10);
        product.stockManager.addStock(stock1);
        product.stockManager.addStock(stock2);
        product.setProductPresentation("Glass container 600 ml");
        Supplier supplier1 = new Supplier(0, "International business S.A.");
        Supplier supplier2 = new Supplier(1, "Another company");
        product.supplierManager.addSupplier(supplier1);
        product.supplierManager.addSupplier(supplier2);
        product.setMinimumStock(100);
        product.setSellingPrice(1.23, 1.00, "USD");

        return product;
    }

    @Test
    public void test() {
        InventoryProduct product = createSampleProduct();
        assertEquals("Coca Cola", product.getName());
        assertEquals("Drink", product.getSubcategory().getCategory().getName());
        assertEquals("Soda", product.getSubcategory().getName());
        assertEquals("Central warehouse", product.stockManager.getStockInLocation(0).getLocation().getName());
        assertEquals(40, product.stockManager.getStockInLocation(0).getMaximumStorage());
        assertEquals(false, product.stockManager.getStockInLocation(0).hasAvailableStorage());
        assertEquals("San Diego 3B", product.stockManager.getStockInLocation(1).getLocation().getAddress());
        assertEquals(false, product.stockManager.getStockInLocation(1).maximumStorageRegistered());
        assertEquals(true, product.stockManager.getStockInLocation(1).hasAvailableStorage());
        assertEquals(50, product.stockManager.getTotalStock());
        assertEquals("Glass container 600 ml", product.getProductPresentation());
        assertEquals("International business S.A.", product.supplierManager.getSupplier(0).getName());
        assertEquals("Another company", product.supplierManager.getSupplier(1).getName());
        assertEquals(2, product.supplierManager.getSuppliers());
        assertEquals(100, product.getMinimumStock().getStock());
        assertEquals(1.23, product.getSellingPrice().getRetail());
        assertEquals(1.00, product.getSellingPrice().getWholesale());

        product.stockManager.removeStock(0);
        assertEquals(10, product.stockManager.getTotalStock());
        product.supplierManager.removeSupplier(0);
        assertEquals(1, product.supplierManager.getSuppliers());
    }

}
