
package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.MinimumStockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.StockEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location.LocationRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier.SupplierRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataR2dbcTest
public class FindProductsWithMinimumStock {

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private SubcategoryRepository subcategoryRepository;

        @Autowired
        private LocationRepository locationRepository;

        @Autowired
        private StockRepository stockRepository;

        @Autowired
        private SupplierRepository supplierRepository;

        @Autowired
        private ProductSupplierRepository productSupplierRepository;

        @BeforeEach
        void setUp() {
                Mono<Void> deleteProductSupplier = productSupplierRepository.deleteAll();
                Mono<Void> deleteSuppliers = supplierRepository.deleteAll();
                Mono<Void> deleteStocklist = stockRepository.deleteAll();
                Mono<Void> deleteLocations = locationRepository.deleteAll();
                Mono<Void> deleteProducts = productRepository.deleteAll();
                Mono<Void> deleteSubcategories = subcategoryRepository.deleteAll();
                Mono<Void> deleteCategories = categoryRepository.deleteAll();

                Mono<Void> setup = deleteProductSupplier
                                .then(deleteSuppliers)
                                .then(deleteStocklist)
                                .then(deleteLocations)
                                .then(deleteProducts)
                                .then(deleteSubcategories)
                                .then(deleteCategories);

                setup.block();
        }

        @Test
        public void test() {
                Samples samples = new Samples();
                ProductEntity[] products = new ProductEntity[3];
                products[0] = samples.product();
                products[0].setMinimumStock(40);
                products[1] = samples.product();
                products[1].setMinimumStock(10);
                products[2] = samples.product();
                products[2].setMinimumStock(50);
                CategoryEntity category = samples.category();
                SubcategoryEntity subcategory = samples.subcategory();
                LocationEntity location = new LocationEntity();
                location.setName("Location sample");
                StockEntity[] stockList = new StockEntity[3];
                stockList[0] = new StockEntity();
                stockList[0].setQuantity(10);
                stockList[1] = new StockEntity();
                stockList[1].setQuantity(20);
                stockList[2] = new StockEntity();
                stockList[2].setQuantity(5);

                Flux<ProductEntity> productFlux = Flux.fromArray(products);
                Flux<StockEntity> stockFlux = Flux.fromArray(stockList);

                Mono<Long> savedCategoryIdMono = categoryRepository.save(category)
                                .map(savedCategory -> savedCategory.getId());

                Mono<Long> savedSubcategoryIdMono = savedCategoryIdMono
                                .flatMap(categoryId -> {
                                        subcategory.setCategory_id(categoryId);
                                        return subcategoryRepository.save(subcategory);
                                })
                                .map(savedSubcategory -> savedSubcategory.getId());

                Mono<Void> saveProductsMono = savedSubcategoryIdMono
                                .flatMap(subcategoryId -> productFlux
                                                .doOnNext(product -> product.setSubcategory_id(subcategoryId))
                                                .flatMap(product -> productRepository.save(product)
                                                                .then(Mono.defer(() -> stockFlux
                                                                                .flatMap(stock -> locationRepository
                                                                                                .save(location)
                                                                                                .flatMap(savedLocation -> {
                                                                                                        stock.setProduct_id(
                                                                                                                        product.getId());
                                                                                                        stock.setLocation_id(
                                                                                                                        savedLocation.getId());
                                                                                                        return stockRepository
                                                                                                                        .save(stock);
                                                                                                }))
                                                                                .then())))
                                                .then());

                StepVerifier.create(saveProductsMono).verifyComplete();

                Flux<MinimumStockProductDTO> founded = productRepository.findProductsWithMinimumStock();

                StepVerifier.create(founded)
                                .expectNextCount(2)
                                .verifyComplete();
        }

}
