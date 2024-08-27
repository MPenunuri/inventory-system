package com.mapera.inventory_system.domain.valueobject;

import com.mapera.inventory_system.domain.common.MovementSubtype;

public enum OutputType implements MovementSubtype {
    SALES("Sales"),
    SUPPLIER_RETURN("Supplier Return"),
    INVENTORY_ADJUSTMENT("Inventory adjustment"),
    INTERNAL_CONSUMPTION("Internal Consumption");

    private final String subtypeName;

    OutputType(String subtypeName) {
        this.subtypeName = subtypeName;
    }

    @Override
    public String getSubtype() {
        return this.subtypeName;
    }
}
