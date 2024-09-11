package com.mapera.inventory_system.domain.aggregate.inventory_product;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.mapera.inventory_system.domain.entity.Supplier;
import com.mapera.inventory_system.infrastructure.util.FindInList;

public class SupplierManager {
    private final List<Supplier> suppliers;

    public SupplierManager(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public int getSuppliers() {
        int number = 0;
        Iterator<Supplier> iterator = this.suppliers.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            number++;
        }
        return number;
    }

    public void addSupplier(Supplier supplier) {
        Optional<Supplier> found = FindInList.findById(this.suppliers, supplier.getId());
        if (found.isPresent()) {
            throw new IllegalArgumentException(
                    "Cannot add a new supplier to supplier list if supplier list contains a supplier with the same id");
        }
        this.suppliers.add(supplier);
    }

    public Supplier getSupplier(long id) {
        Supplier supplier = FindInList.getById(this.suppliers, id, "Supplier");
        return supplier;
    }

    public Optional<Supplier> findSupplier(long id) {
        Optional<Supplier> supplier = FindInList.findById(this.suppliers, id);
        return supplier;
    }

    public boolean removeSupplier(long id) {
        boolean removed = FindInList.findByIdAndRemove(this.suppliers, id);
        return removed;
    }
}
