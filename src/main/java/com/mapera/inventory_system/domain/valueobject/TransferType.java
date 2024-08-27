package com.mapera.inventory_system.domain.valueobject;

import com.mapera.inventory_system.domain.common.MovementSubtype;

public enum TransferType implements MovementSubtype {
    NONE("None");

    private final String subtypeName;

    TransferType(String subtypeName) {
        this.subtypeName = subtypeName;
    }

    @Override
    public String getSubtype() {
        return this.subtypeName;
    }

}
