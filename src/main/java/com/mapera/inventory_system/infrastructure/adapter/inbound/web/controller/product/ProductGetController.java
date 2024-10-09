package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.security.AuthenticationService;
import com.mapera.inventory_system.application.service.ProductApplicationService;
import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.LocationProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.NoSupplierProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StandardProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.SupplierProductDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure/product")
public class ProductGetController {

    @Autowired
    private ProductApplicationService productApplicationService;

    @Autowired
    private AuthenticationService authService;

    @GetMapping("/{productId}")
    public Mono<InventoryProduct> getProductById(@PathVariable Long productId) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return productApplicationService.findProductById(userId, productId);
        });
    }

    @GetMapping("/all")
    public Flux<StandardProductDTO> getAllProducts() {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return productApplicationService.findAllProducts(userId);
        });
    }

    @GetMapping("/category/{categoryId}")
    public Flux<StandardProductDTO> getProductsByCategoryId(@PathVariable long categoryId) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return productApplicationService.findProductsByCategoryId(userId, categoryId);
        });
    }

    @GetMapping("/subcategory/{subcategoryId}")
    public Flux<StandardProductDTO> getProductsBySubcategoryId(@PathVariable long subcategoryId) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return productApplicationService.findProductsBySubcategoryId(userId, subcategoryId);
        });
    }

    @GetMapping("/supplier/{supplierId}")
    public Flux<SupplierProductDTO> getProductsBySupplierId(@PathVariable long supplierId) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return productApplicationService.findProductsBySupplierId(userId, supplierId);
        });
    }

    @GetMapping("/no-supplier/{supplierId}")
    public Flux<NoSupplierProductDTO> getProductsWithNoSupplierRelation(@PathVariable long supplierId) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return productApplicationService.findProductsWithNoSupplierRelation(userId, supplierId);
        });
    }

    @GetMapping("/location/{locationId}")
    public Flux<LocationProductDTO> getProductsByLocationId(@PathVariable long locationId) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return productApplicationService.findProductsByLocationid(userId, locationId);
        });
    }

    @GetMapping("/stock/minimum")
    public Flux<StockProductDTO> findProductsWithMinimumStock() {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return productApplicationService.findProductsWithMinimumStock(userId);
        });
    }

    @GetMapping("/stock/{productId}")
    public Mono<StockProductDTO> getProductStockById(@PathVariable Long productId) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return productApplicationService.getProductStockById(userId, productId);
        });
    }

    @GetMapping("/currency/{currencyId}")
    public Flux<StandardProductDTO> getProductsByPriceCurrency(
            @PathVariable Long currencyId) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return productApplicationService.getProductsByPriceCurrency(userId, currencyId);
        });
    }

    @GetMapping("/price/retail")
    public Flux<StandardProductDTO> getProductsBySellingRetailPrice(
            @RequestParam Long currencyId,
            @RequestParam Double min,
            @RequestParam Double max) {
        if (min > max) {
            throw new IllegalArgumentException("min should not be greater than max");
        }
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return productApplicationService.findProductsBySellingRetailPrice(userId, currencyId, min, max);
        });
    }

    @GetMapping("/price/wholesale")
    public Flux<StandardProductDTO> getProductsBySellingWholesalePrice(
            @RequestParam Long currencyId,
            @RequestParam Double min,
            @RequestParam Double max) {
        if (min > max) {
            throw new IllegalArgumentException("min should not be greater than max");
        }
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return productApplicationService.findProductsBySellingWholesalePrice(userId, currencyId, min, max);
        });
    }
}
