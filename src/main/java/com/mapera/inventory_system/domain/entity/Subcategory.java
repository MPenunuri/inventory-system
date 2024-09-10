package com.mapera.inventory_system.domain.entity;

public class Subcategory {
    private final Long id;
    private final Category category;
    private String name;

    public Subcategory(Long id, Category category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public Category getCategory() {
        return this.category;
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
                ", category='" + getCategory() + "'" +
                ", name='" + getName() + "'" +
                "}";
    }

}
