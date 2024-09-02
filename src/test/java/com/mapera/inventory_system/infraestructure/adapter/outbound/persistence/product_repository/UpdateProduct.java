
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
public class UpdateProduct {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Test
    public void test() {
        Samples samples = new Samples();
        AtomicReference<Long> nexSubcategoryIdRef = new AtomicReference<>();
        CategoryEntity category = samples.category();
        SubcategoryEntity prevSubcategory = samples.subcategory();
        SubcategoryEntity nextSubcategory = samples.subcategory();
        nextSubcategory.setName("Light soda");
        ProductEntity product = samples.product();

        Mono<Long> savedPrevSubcategoryId = categoryRepository.save(category)
                .flatMap(savedCategory -> {
                    nextSubcategory.setCategory_id(savedCategory.getId());
                    prevSubcategory.setCategory_id(savedCategory.getId());
                    return subcategoryRepository.save(nextSubcategory)
                            .flatMap(savedSubcategory -> {
                                nexSubcategoryIdRef.set(savedSubcategory.getId());
                                return subcategoryRepository.save(prevSubcategory)
                                        .map(savedPrevSubcategory -> savedPrevSubcategory.getId());
                            });

                });

        Mono<Long> savedProductId = savedPrevSubcategoryId.flatMap(
                subcategoryId -> {
                    product.setSubcategory_id(subcategoryId);
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
                    return productRepository.updateSubcategory(productId, nexSubcategoryIdRef.get())
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
                    return productRepository.updatePriceCurrency(productId, "CAD");
                });

        StepVerifier.create(updatedProduct).expectNextMatches(
                p -> p.getName().equals("Fanta light") &&
                        p.getSubcategory_id() == nexSubcategoryIdRef.get() &&
                        p.getProductPresentation().equals("Plastic bottle 600ml") &&
                        p.getMinimumStock() == 30 &&
                        p.getRetail_price() == 1.25 &&
                        p.getWholesale_price() == 1.00 &&
                        p.getPrice_currency().equals("CAD"))
                .verifyComplete();

    }
}
