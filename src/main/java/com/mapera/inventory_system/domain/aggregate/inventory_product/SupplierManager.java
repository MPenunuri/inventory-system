package com.mapera.inventory_system.domain.aggregate.inventory_product;

import java.util.List;

import com.mapera.inventory_system.domain.entity.Supplier;
import com.mapera.inventory_system.infrastructure.util.FindInList;

public class SupplierManager {
    private final List<Supplier> suppliers;

    public SupplierManager(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public void addSupplier(int id, String name) {
        Supplier supplier = new Supplier(id, name);
        this.suppliers.add(supplier);
    }

    public Supplier getSupplier(int id) {
        Supplier supplier = FindInList.findById(this.suppliers, id, "Supplier");
        return supplier;
    }

    public boolean removeSupplier(int id) {
        boolean removed = FindInList.findByIdAndRemove(this.suppliers, id);
        return removed;
    }
}
