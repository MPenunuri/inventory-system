package com.mapera.inventory_system.domain.aggregate.inventory_movement;

import java.time.LocalDateTime;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.common.MovementSubtype;

public class Movement {
    private final int id;
    private InventoryProduct product;
    private LocalDateTime time;
    private String type;
    private MovementSubtype subtype;
    private String reason;
    private int quantity;
    private String comment;

    public Movement(int id, InventoryProduct product,
            LocalDateTime time, String type,
            MovementSubtype subtype,
            String reason, int quantity, String comment) {
        this.id = id;
        this.product = product;
        this.time = time;
        this.type = type;
        this.subtype = subtype;
        this.reason = reason;
        this.quantity = quantity;
        this.comment = comment;
    }

    public Movement(int id, InventoryProduct product,
            LocalDateTime time, String type,
            MovementSubtype subtype,
            String reason, int quantity) {
        this(id, product, time, type, subtype, reason, quantity, null);
    }

    public int getId() {
        return this.id;
    }

    public InventoryProduct getProduct() {
        return this.product;
    }

    public void setProduct(InventoryProduct product) {
        this.product = product;
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MovementSubtype getSubtype() {
        return this.subtype;
    }

    public void setSubtype(MovementSubtype subtype) {
        this.subtype = subtype;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
