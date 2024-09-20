package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StandardProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency.CurrencyRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataR2dbcTest
public class FindProductsBySellingPrice {

        @Autowired
        UserRepository userRepository;

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private SubcategoryRepository subcategoryRepository;

        @Autowired
        private CurrencyRepository currencyRepository;

        @Test
        public void test() {
                Samples samples = new Samples();

                UserEntity userEntity = samples.user();

                CategoryEntity category = samples.category();
                AtomicReference<Long> subcategoryId = new AtomicReference<>();
                SubcategoryEntity subcategory = samples.subcategory();

                AtomicReference<Long> currencyId = new AtomicReference<>();
                CurrencyEntity currency = new CurrencyEntity();
                currency.setName("USD");

                ProductEntity[] products = new ProductEntity[4];
                products[0] = samples.product();
                products[0].setRetail_price(.5);
                products[0].setWholesale_price(.25);
                products[1] = samples.product();
                products[1].setRetail_price(1);
                products[1].setWholesale_price(.5);
                products[2] = samples.product();
                products[2].setRetail_price(1.5);
                products[2].setWholesale_price(.75);
                products[3] = samples.product();
                products[3].setWholesale_price(1);
                products[3].setRetail_price(2);

                Mono<Void> savedUser = userRepository.save(userEntity).doOnNext(u -> {
                        category.setUser_id(u.getId());
                        subcategory.setUser_id(u.getId());
                        currency.setUser_id(u.getId());
                        products[0].setUser_id(u.getId());
                        products[1].setUser_id(u.getId());
                        products[2].setUser_id(u.getId());
                        products[3].setUser_id(u.getId());
                }).then();

                StepVerifier.create(savedUser).verifyComplete();

                Flux<ProductEntity> productFlux = Flux.fromArray(products);

                Mono<Void> savedCurrencyId = currencyRepository.save(currency)
                                .doOnNext(c -> currencyId.set(c.getId())).then();

                Mono<Void> savedSubcategoryId = categoryRepository.save(category)
                                .flatMap(savedCategory -> {
                                        subcategory.setCategory_id(savedCategory.getId());
                                        return subcategoryRepository.save(subcategory)
                                                        .doOnNext(s -> subcategoryId.set(s.getId())).then();
                                });

                Mono<Void> savedProducts = productFlux.flatMap(product -> {
                        product.setSubcategory_id(subcategoryId.get());
                        product.setPrice_currency_id(currencyId.get());
                        return productRepository.save(product).then();
                }).then();

                Mono<Void> saveItems = savedCurrencyId.then(savedSubcategoryId).then(savedProducts);

                StepVerifier.create(saveItems).verifyComplete();

                Flux<StandardProductDTO> retailFound = productRepository.findProductsBySellingRetailPrice(
                                category.getUser_id(),
                                currencyId.get(), 1.0, 1.5);

                StepVerifier.create(retailFound)
                                .expectNextCount(2)
                                .verifyComplete();

                Flux<StandardProductDTO> wholesaleFound = productRepository.findProductsBySellingWholesalePrice(
                                category.getUser_id(),
                                currencyId.get(), .5, .75);

                StepVerifier.create(wholesaleFound)
                                .expectNextCount(2)
                                .verifyComplete();
        }
}
