package com.mapera.inventory_system.domain.entity;

import com.mapera.inventory_system.domain.common.Identifiable;

public class Supplier implements Identifiable {
    private final Long id;
    private String name;

    public Supplier(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                "}";
    }

}
