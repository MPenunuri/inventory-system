package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.security.AuthenticationService;
import com.mapera.inventory_system.application.service.ProductApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.product.ProductUpdateRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure/product")
@Validated
public class ProductPatchController {

        @Autowired
        private ProductApplicationService productApplicationService;

        @Autowired
        private AuthenticationService authService;

        @PatchMapping("/name")
        public Mono<ProductEntity> updateProductName(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return authService.getUserIdFromToken().flatMap(userId -> {
                        return productApplicationService.updateProductName(
                                        userId, updateRequest.getId(), updateRequest.getName());
                });
        }

        @PatchMapping("/subcategory")
        public Mono<ProductEntity> updateSubcategory(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return authService.getUserIdFromToken().flatMap(userId -> {
                        return productApplicationService.updateSubcategory(
                                        userId, updateRequest.getId(), updateRequest.getSubcategoryId());
                });
        }

        @PatchMapping("/presentation")
        public Mono<ProductEntity> updateProductPresentation(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return authService.getUserIdFromToken().flatMap(userId -> {
                        return productApplicationService.updateProductPresentation(
                                        userId, updateRequest.getId(), updateRequest.getProductPresentation());
                });
        }

        @PatchMapping("/minimumStock")
        public Mono<ProductEntity> updateMinimumStock(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return authService.getUserIdFromToken().flatMap(userId -> {
                        return productApplicationService.updateMinimumStock(
                                        userId, updateRequest.getId(), updateRequest.getMinimumStock());
                });
        }

        @PatchMapping("/retailprice")
        public Mono<ProductEntity> updateRetailPrice(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return authService.getUserIdFromToken().flatMap(userId -> {
                        return productApplicationService.updateRetailPrice(
                                        userId, updateRequest.getId(), updateRequest.getRetailPrice());
                });
        }

        @PatchMapping("/wholesale")
        public Mono<ProductEntity> updateWholesalePrice(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return authService.getUserIdFromToken().flatMap(userId -> {
                        return productApplicationService.updateWholesalePrice(
                                        userId, updateRequest.getId(), updateRequest.getWholesalePrice());
                });
        }

        @PatchMapping("/currency")
        public Mono<ProductEntity> updatePriceCurrency(
                        @Valid @RequestBody ProductUpdateRequest updateRequest) {
                return authService.getUserIdFromToken().flatMap(userId -> {
                        return productApplicationService.updatePriceCurrency(
                                        userId, updateRequest.getId(), updateRequest.getPriceCurrencyId());
                });
        }

}
