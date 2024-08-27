package com.mapera.inventory_system.domain.valueobject;

import com.mapera.inventory_system.domain.common.MovementSubtype;

public enum EntryType implements MovementSubtype {
    ACQUISITION("Acquisition"),
    CUSTOMER_RETURN("Customer return"),
    INVENTORY_ADJUSTMENT("Inventory adjustment"),
    PRODUCTION("Production");

    private final String subtypeName;

    EntryType(String subtypeName) {
        this.subtypeName = subtypeName;
    }

    @Override
    public String getSubtype() {
        return this.subtypeName;
    }
}
