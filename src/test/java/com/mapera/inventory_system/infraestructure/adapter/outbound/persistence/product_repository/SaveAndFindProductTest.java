
package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataR2dbcTest
public class SaveAndFindProductTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Test
    public void test() {
        Samples samples = new Samples();
        AtomicReference<Long> subcategoryIdRef = new AtomicReference<>();
        CategoryEntity category = samples.category();
        SubcategoryEntity subcategory = samples.subcategory();
        ProductEntity product = samples.product();

        Mono<ProductEntity> savedProductMono = categoryRepository.save(category)
                .flatMap(savedCategory -> {
                    subcategory.setCategory_id(savedCategory.getId());
                    return subcategoryRepository.save(subcategory);
                })
                .flatMap(savedSubcategory -> {
                    subcategoryIdRef.set(savedSubcategory.getId());
                    product.setSubcategory_id(savedSubcategory.getId());
                    return productRepository.save(product);
                });

        Mono<ProductEntity> foundProductMono = savedProductMono
                .flatMap(savedProduct -> productRepository.findById(savedProduct.getId()));

        StepVerifier.create(foundProductMono)
                .expectNextMatches(foundProduct -> foundProduct.getName().equals("Coca cola")
                        &&
                        foundProduct.getSubcategory_id() == subcategoryIdRef.get() &&
                        foundProduct.getMinimumStock() == 20 &&
                        foundProduct.getRetail_price() == 1 &&
                        foundProduct.getWholesale_price() == 0.75 &&
                        foundProduct.getProductPresentation().equals("Glass container 600 ml"))
                .verifyComplete();
    }
}
