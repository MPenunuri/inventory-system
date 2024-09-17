package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.ProductApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.product.ProductUpdateRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure/product/")
@Validated
public class ProductPatchController {

        @Autowired
        private ProductApplicationService productApplicationService;

        @PatchMapping("/name")
        public Mono<ProductEntity> updateProductName(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return productApplicationService.updateProductName(
                                updateRequest.getId(), updateRequest.getName());
        }

        @PatchMapping("/subcategory")
        public Mono<ProductEntity> updateSubcategory(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return productApplicationService.updateSubcategory(
                                updateRequest.getId(), updateRequest.getSubcategoryId());
        }

        @PatchMapping("/presentation")
        public Mono<ProductEntity> updateProductPresentation(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return productApplicationService.updateProductPresentation(
                                updateRequest.getId(), updateRequest.getProductPresentation());
        }

        @PatchMapping("/minimumStock")
        public Mono<ProductEntity> updateMinimumStock(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return productApplicationService.updateMinimumStock(
                                updateRequest.getId(), updateRequest.getMinimumStock());
        }

        @PatchMapping("/retailprice")
        public Mono<ProductEntity> updateRetailPrice(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return productApplicationService.updateRetailPrice(
                                updateRequest.getId(), updateRequest.getRetailPrice());
        }

        @PatchMapping("/wholesale")
        public Mono<ProductEntity> updateWholesalePrice(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return productApplicationService.updateWholesalePrice(
                                updateRequest.getId(), updateRequest.getWholesalePrice());
        }

        @PatchMapping("/currency")
        public Mono<ProductEntity> updatePriceCurrency(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return productApplicationService.updatePriceCurrency(
                                updateRequest.getId(), updateRequest.getPriceCurrencyId());
        }

}
