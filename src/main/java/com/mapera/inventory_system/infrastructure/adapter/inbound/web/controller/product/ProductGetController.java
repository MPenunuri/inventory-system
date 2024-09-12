package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.ProductApplicationService;
import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.LocationProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StandardProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.SupplierProductDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products/get")
public class ProductGetController {

    @Autowired
    private ProductApplicationService productApplicationService;

    @GetMapping("/{productId}")
    public Mono<InventoryProduct> getProductById(@PathVariable Long productId) {
        return productApplicationService.findProductById(productId);
    }

    @GetMapping("/all")
    public Flux<StandardProductDTO> getAllProducts() {
        return productApplicationService.findAllProducts();
    }

    @GetMapping("/category/{categoryId}")
    public Flux<StandardProductDTO> getProductsByCategoryId(@PathVariable long categoryId) {
        return productApplicationService.findProductsByCategoryId(categoryId);
    }

    @GetMapping("/subcategory/{subcategoryId}")
    public Flux<StandardProductDTO> getProductsBySubcategoryId(@PathVariable long subcategoryId) {
        return productApplicationService.findProductsBySubcategoryId(subcategoryId);
    }

    @GetMapping("/supplier/{supplierId}")
    public Flux<SupplierProductDTO> getProductsBySupplierId(@PathVariable long supplierId) {
        return productApplicationService.findProductsBySupplierId(supplierId);
    }

    @GetMapping("/location/{locationId}")
    public Flux<LocationProductDTO> getProductsByLocationId(@PathVariable long locationId) {
        return productApplicationService.findProductsByLocationid(locationId);
    }

    @GetMapping("/stock/product/minimum")
    public Flux<StockProductDTO> getProductsByLocationId() {
        return productApplicationService.findProductsWithMinimumStock();
    }

    @GetMapping("/stock/product/{productId}")
    public Mono<StockProductDTO> getProductStockById(@PathVariable Long productId) {
        return productApplicationService.getProductStockById(productId);
    }

    @GetMapping("/price/retail/")
    public Flux<StandardProductDTO> getProductsBySellingRetailPrice(
            @RequestParam Long currencyId,
            @RequestParam Double min,
            @RequestParam Double max) {
        return productApplicationService.findProductsBySellingRetailPrice(currencyId, min, max);
    }

    @GetMapping("/price/wholesale/")
    public Flux<StandardProductDTO> getProductsBySellingWholesalePrice(
            @RequestParam Long currencyId,
            @RequestParam Double min,
            @RequestParam Double max) {
        return productApplicationService.findProductsBySellingWholesalePrice(currencyId, min, max);
    }

}
