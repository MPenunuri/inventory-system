package com.mapera.inventory_system.domain.repository;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;

public interface IProductRepository extends IRepository<InventoryProduct> {
    InventoryProduct[] findProductsByCategoryId(int id);

    InventoryProduct[] findProductsBySubcategoryId(int id);

    InventoryProduct[] indProductsBySupplierId(int id);

    InventoryProduct[] findProductsByLocationId(int id);

    InventoryProduct[] findProductsBySellingPrice(String currency, String type, double min, double max);

    InventoryProduct[] findProductsWithMinimumStock();
}
