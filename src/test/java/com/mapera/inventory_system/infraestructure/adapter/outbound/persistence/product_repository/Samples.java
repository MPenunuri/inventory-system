package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;

public class Samples {
    public UserEntity user() {
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setEmail("example@domain.com");
        user.setPassword("123456");
        user.setRoles("USER");
        return user;
    }

    public CategoryEntity category() {
        CategoryEntity category = new CategoryEntity();
        category.setName("Drinks");
        return category;
    }

    public SubcategoryEntity subcategory() {
        SubcategoryEntity subcategory = new SubcategoryEntity();
        subcategory.setName("Soda");
        return subcategory;
    }

    public ProductEntity product() {
        ProductEntity product = new ProductEntity();
        product.setName("Coca cola");
        product.setProductPresentation("Glass container 600 ml");
        product.setMinimumStock(20);
        product.setRetail_price(1);
        product.setWholesale_price(.75);
        return product;
    }
}
