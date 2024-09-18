package com.mapera.inventory_system.domain.aggregate.inventory_product;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mapera.inventory_system.domain.entity.Stock;
import com.mapera.inventory_system.domain.entity.Subcategory;
import com.mapera.inventory_system.domain.entity.Supplier;
import com.mapera.inventory_system.domain.valueobject.MinimumStock;
import com.mapera.inventory_system.domain.valueobject.SellingPrice;

public class InventoryProduct extends Product {
        @JsonIgnore
        public StockManager stockManager;
        @JsonIgnore
        public SupplierManager supplierManager;

        public InventoryProduct(
                        Long id, String name, Subcategory subcategory,
                        List<Stock> stockList, String productPresentation,
                        List<Supplier> suppliers, MinimumStock minimumStock,
                        SellingPrice sellingPrice) {
                super(id, name, subcategory, stockList,
                                productPresentation, suppliers,
                                minimumStock, sellingPrice);
                this.stockManager = new StockManager(stockList);
                this.supplierManager = new SupplierManager(suppliers);
        }

        public InventoryProduct(String name) {
                this(null, name, null, null,
                                null, null,
                                null, null);
                this.setStockList(new ArrayList<Stock>());
                this.stockManager = new StockManager(this.getStockList());
                this.setSuppliers(new ArrayList<Supplier>());
                this.supplierManager = new SupplierManager(this.getSuppliers());
        }
}
