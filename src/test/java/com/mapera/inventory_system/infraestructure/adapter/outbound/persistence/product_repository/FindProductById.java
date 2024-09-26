
package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.application.port.outbound.ProductPersistencePort;
import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.StockEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency.CurrencyRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location.LocationRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier.SupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")

@DataR2dbcTest
public class FindProductById {

        @Autowired
        UserRepository userRepository;

        @Autowired
        private ProductPersistencePort productRepository;

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private SubcategoryRepository subcategoryRepository;

        @Autowired
        private CurrencyRepository currencyRepository;

        @Autowired
        private StockRepository stockRepository;

        @Autowired
        private LocationRepository locationRepository;

        @Autowired
        private SupplierRepository supplierRepository;

        @Autowired
        private ProductSupplierRepository productSupplierRepository;

        @Autowired
        MovementRepository movementRepository;

        @Autowired
        ProductRepository productCrudRepository;

        @BeforeEach
        void setUp() {
                Mono<Void> deleteMovements = movementRepository.deleteAll();
                Mono<Void> deleteProductSupplier = productSupplierRepository.deleteAll();
                Mono<Void> deleteSuppliers = supplierRepository.deleteAll();
                Mono<Void> deleteStocklist = stockRepository.deleteAll();
                Mono<Void> deleteProducts = productCrudRepository.deleteAll();
                Mono<Void> deleteSubcategories = subcategoryRepository.deleteAll();
                Mono<Void> deleteCategories = categoryRepository.deleteAll();
                Mono<Void> deleteCurrencies = currencyRepository.deleteAll();
                Mono<Void> deleteLocations = locationRepository.deleteAll();
                Mono<Void> deleteUsers = userRepository.deleteAll();

                Mono<Void> setup = deleteProductSupplier
                                .then(deleteStocklist)
                                .then(deleteMovements)
                                .then(deleteSuppliers)
                                .then(deleteProducts)
                                .then(deleteSubcategories)
                                .then(deleteCategories)
                                .then(deleteCurrencies)
                                .then(deleteLocations)
                                .then(deleteUsers);

                setup.block();
        }

        @Test
        public void test() {

                Samples samples = new Samples();

                UserEntity userEntity = samples.user();
                AtomicReference<Long> userIdRef = new AtomicReference<>();

                CategoryEntity category = samples.category();
                AtomicReference<Long> subcategoryIdRef = new AtomicReference<>();
                SubcategoryEntity subcategory = samples.subcategory();

                AtomicReference<Long> productIdRef = new AtomicReference<>();
                ProductEntity product = samples.product();

                AtomicReference<Long> currencyIdRef = new AtomicReference<>();
                CurrencyEntity currency = new CurrencyEntity();
                currency.setName("USD");

                AtomicReference<Long> location1IdRef = new AtomicReference<>();
                LocationEntity location1 = new LocationEntity();
                location1.setName("Location 1");
                AtomicReference<Long> location2IdRef = new AtomicReference<>();
                LocationEntity location2 = new LocationEntity();
                location2.setName("Location 2");
                StockEntity stock1 = new StockEntity();
                stock1.setQuantity(20);
                stock1.setMaximumStorage(20);
                StockEntity stock2 = new StockEntity();
                stock2.setQuantity(15);

                AtomicReference<Long> supplier1IdRef = new AtomicReference<>();
                SupplierEntity supplier1 = new SupplierEntity();
                supplier1.setName("Supplier 1");
                AtomicReference<Long> supplier2IdRef = new AtomicReference<>();
                SupplierEntity supplier2 = new SupplierEntity();
                supplier2.setName("Supplier 2");

                Mono<Void> savedUser = userRepository.save(userEntity).doOnNext(u -> {
                        userIdRef.set(u.getId());
                        category.setUser_id(u.getId());
                        subcategory.setUser_id(u.getId());
                        product.setUser_id(u.getId());
                        currency.setUser_id(u.getId());
                        location1.setUser_id(u.getId());
                        location2.setUser_id(u.getId());
                        stock1.setUser_id(u.getId());
                        stock2.setUser_id(u.getId());
                        supplier1.setUser_id(u.getId());
                        supplier2.setUser_id(u.getId());
                }).then();

                StepVerifier.create(savedUser).verifyComplete();

                Mono<Void> savedLocations = locationRepository.save(location1)
                                .doOnNext(loc1 -> location1IdRef.set(loc1.getId()))
                                .then(locationRepository.save(location2)
                                                .doOnNext(loc2 -> location2IdRef.set(loc2.getId())))
                                .then();

                StepVerifier.create(savedLocations).verifyComplete();

                Mono<Void> savedSuppliers = supplierRepository.save(supplier1)
                                .doOnNext(s -> supplier1IdRef.set(s.getId()))
                                .then(supplierRepository.save(supplier2)
                                                .doOnNext(s -> supplier2IdRef.set(s.getId())))
                                .then();

                StepVerifier.create(savedSuppliers).verifyComplete();

                Mono<Void> savedCurrency = currencyRepository.save(currency)
                                .doOnNext(c -> currencyIdRef.set(c.getId())).then();

                StepVerifier.create(savedCurrency).verifyComplete();

                Mono<Void> savedSubcategoryId = categoryRepository.save(category)
                                .flatMap(savedCategory -> {
                                        subcategory.setCategory_id(savedCategory.getId());
                                        return subcategoryRepository.save(subcategory).doOnNext(
                                                        s -> subcategoryIdRef.set(s.getId()));
                                }).then();

                StepVerifier.create(savedSubcategoryId).verifyComplete();

                Mono<Void> savedProduct = productRepository
                                .registerProduct(userIdRef.get(), product.getName())
                                .doOnNext(p -> productIdRef.set(p.getId()))
                                .then();

                StepVerifier.create(savedProduct).verifyComplete();

                Mono<InventoryProduct> foundProduct = productRepository.findProductById(
                                userIdRef.get(), productIdRef.get());

                StepVerifier.create(foundProduct).expectNextMatches(p -> p.getName().equals(
                                product.getName())).verifyComplete();

                Mono<Void> updatedProduct = productRepository
                                .updateSubcategory(userIdRef.get(), productIdRef.get(), subcategoryIdRef.get())
                                .then(productRepository.updateProductPresentation(
                                                userIdRef.get(),
                                                productIdRef.get(),
                                                product.getProduct_presentation()))
                                .then(productRepository.updateMinimumStock(
                                                userIdRef.get(),
                                                productIdRef.get(),
                                                product.getMinimum_stock()))
                                .then(productRepository.updateRetailPrice(
                                                userIdRef.get(),
                                                productIdRef.get(),
                                                product.getRetail_price()))
                                .then(productRepository.updateWholesalePrice(
                                                userIdRef.get(),
                                                productIdRef.get(),
                                                product.getWholesale_price()))
                                .then(productRepository.updatePriceCurrency(
                                                userIdRef.get(),
                                                productIdRef.get(),
                                                currencyIdRef.get()))
                                .then();

                StepVerifier.create(updatedProduct).verifyComplete();

                Mono<Void> savedSuppliersRelations = productSupplierRepository
                                .addProductSupplierRelation(
                                                userIdRef.get(),
                                                productIdRef.get(),
                                                supplier1IdRef.get())
                                .then(productSupplierRepository
                                                .addProductSupplierRelation(
                                                                userIdRef.get(),
                                                                productIdRef.get(),
                                                                supplier2IdRef.get()))
                                .then();

                StepVerifier.create(savedSuppliersRelations).verifyComplete();

                Mono<Boolean> savedStock1 = stockRepository.addProductStockInLocation(
                                userIdRef.get(),
                                location1IdRef.get(), productIdRef.get(), 20, 20);

                Mono<Boolean> savedStock2 = stockRepository.addProductStockInLocation(
                                userIdRef.get(),
                                location2IdRef.get(), productIdRef.get(), 15, null);

                StepVerifier.create(savedStock1).expectNext(true).verifyComplete();
                StepVerifier.create(savedStock2).expectNext(true).verifyComplete();

                StepVerifier.create(foundProduct)
                                .expectNextMatches(p -> p.getSubcategory().getCategory().getName().equals(
                                                category.getName()) &&
                                                p.getSubcategory().getName().equals(subcategory.getName()) &&
                                                p.getProductPresentation().equals(product.getProduct_presentation()) &&
                                                p.getMinimumStock().getStock() == product.getMinimum_stock() &&
                                                p.getSellingPrice().getRetail() == product.getRetail_price() &&
                                                p.getSellingPrice().getWholesale() == product.getWholesale_price() &&
                                                p.getSellingPrice().getCurrency().equals(currency.getName()) &&
                                                p.supplierManager.getSuppliers() == 2 &&
                                                p.stockManager.getTotalStock() == 35)
                                .verifyComplete();

        }

}
