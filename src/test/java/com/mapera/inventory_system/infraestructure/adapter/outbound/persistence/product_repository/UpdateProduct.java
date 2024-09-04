
package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency.CurrencyRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")

@DataR2dbcTest
public class UpdateProduct {

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
        AtomicReference<Long> nextSubcategoryIdRef = new AtomicReference<>();
        CategoryEntity category = samples.category();
        SubcategoryEntity prevSubcategory = samples.subcategory();
        SubcategoryEntity nextSubcategory = samples.subcategory();
        nextSubcategory.setName("Light soda");
        ProductEntity product = samples.product();

        AtomicReference<Long> prevCurrencyIdRef = new AtomicReference<>();
        CurrencyEntity currency1 = new CurrencyEntity();
        currency1.setName("USD");
        AtomicReference<Long> nextCurrencyIdRef = new AtomicReference<>();
        CurrencyEntity currency2 = new CurrencyEntity();
        currency2.setName("MXN");

        Mono<Void> savedPrevCurrency = currencyRepository.save(currency1)
                .doOnNext(c -> prevCurrencyIdRef.set(c.getId())).then();

        Mono<Void> savedNextCurrency = currencyRepository.save(currency2)
                .doOnNext(c -> nextCurrencyIdRef.set(c.getId())).then();

        Mono<Long> savedPrevSubcategoryId = categoryRepository.save(category)
                .flatMap(savedCategory -> {
                    nextSubcategory.setCategory_id(savedCategory.getId());
                    prevSubcategory.setCategory_id(savedCategory.getId());
                    return subcategoryRepository.save(nextSubcategory)
                            .flatMap(savedSubcategory -> {
                                nextSubcategoryIdRef.set(savedSubcategory.getId());
                                return subcategoryRepository.save(prevSubcategory)
                                        .map(savedPrevSubcategory -> savedPrevSubcategory.getId());
                            });

                });

        Mono<Long> savedProductId = savedPrevSubcategoryId.flatMap(
                subcategoryId -> {
                    product.setSubcategory_id(subcategoryId);
                    product.setPrice_currency_id(prevCurrencyIdRef.get());
                    return productRepository.save(product).map(prod -> {
                        return prod.getId();
                    });
                });

        Mono<ProductEntity> updatedProduct = savedProductId
                .flatMap(productId -> {
                    return productRepository.updateProductName(productId, "Fanta light")
                            .thenReturn(productId);
                })
                .flatMap(productId -> {
                    return productRepository.updateSubcategory(productId, nextSubcategoryIdRef.get())
                            .thenReturn(productId);
                })
                .flatMap(productId -> {
                    return productRepository.updateProductPresentation(productId, "Plastic bottle 600ml")
                            .thenReturn(productId);
                })
                .flatMap(productId -> {
                    return productRepository.updateMinimumStock(productId, 30).thenReturn(productId);
                })
                .flatMap(productId -> {
                    return productRepository.updateRetailPrice(productId, 1.25).thenReturn(productId);
                })
                .flatMap(productId -> {
                    return productRepository.updateWholesalePrice(productId, 1.00).thenReturn(productId);
                })
                .flatMap(productId -> {
                    return productRepository.updatePriceCurrency(productId, nextCurrencyIdRef.get());
                });

        Mono<ProductEntity> updated = savedPrevCurrency.then(savedNextCurrency).then(updatedProduct);

        StepVerifier.create(
                updated).expectNextMatches(
                        p -> p.getName().equals("Fanta light") &&
                                p.getSubcategory_id() == nextSubcategoryIdRef.get() &&
                                p.getProductPresentation().equals("Plastic bottle 600ml") &&
                                p.getMinimumStock() == 30 &&
                                p.getRetail_price() == 1.25 &&
                                p.getWholesale_price() == 1.00 &&
                                p.getPrice_currency_id() == nextCurrencyIdRef.get())
                .verifyComplete();

    }
}
