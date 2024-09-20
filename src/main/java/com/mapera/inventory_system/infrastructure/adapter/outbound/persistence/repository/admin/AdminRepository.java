package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.application.port.outbound.AdminPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.user.UserData;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency.CurrencyCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location.LocationCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier.SupplierCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class AdminRepository implements AdminPersistencePort {

    @Autowired
    UserCrudRepository userCrudRepository;

    @Autowired
    CategoryCrudRepository categoryCrudRepository;

    @Autowired
    SubcategoryCrudRepository subcategoryCrudRepository;

    @Autowired
    ProductCrudRepository productCrudRepository;

    @Autowired
    SupplierCrudRepository supplierCrudRepository;

    @Autowired
    ProductSupplierCrudRepository productSupplierCrudRepository;

    @Autowired
    LocationCrudRepository locationCrudRepository;

    @Autowired
    CurrencyCrudRepository currencyCrudRepository;

    @Autowired
    MovementCrudRepository movementCrudRepository;

    @Autowired
    StockCrudRepository stockCrudRepository;

    @Override
    public Flux<UserEntity> getUsers() {
        return userCrudRepository.findAll();
    }

    @Override
    public Mono<UserData> getUserInfo(Long userId) {
        return userCrudRepository.getUserInfo(userId);
    }

    @Override
    public Mono<Void> deleteUserAndAllTheirInformation(Long userId) {
        return movementCrudRepository.deleteByUserId(userId)
                .then(productSupplierCrudRepository.deleteByUserId(userId))
                .then(stockCrudRepository.deleteByUserId(userId))
                .then(productCrudRepository.deleteByUserId(userId))
                .then(subcategoryCrudRepository.deleteByUserId(userId))
                .then(categoryCrudRepository.deleteByUserId(userId))
                .then(currencyCrudRepository.deleteByUserId(userId))
                .then(locationCrudRepository.deleteByUserId(userId))
                .then(supplierCrudRepository.deleteByUserId(userId))
                .then(userCrudRepository.deleteById(userId))
                .then();
    }

}
