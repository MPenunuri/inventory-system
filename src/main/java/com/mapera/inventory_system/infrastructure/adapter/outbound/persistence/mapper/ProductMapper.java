
package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.mapper;

import java.util.List;
import java.util.Optional;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.entity.Category;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.domain.entity.Stock;
import com.mapera.inventory_system.domain.entity.Subcategory;
import com.mapera.inventory_system.domain.entity.Supplier;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.FullProductDTO;

public class ProductMapper {
    public InventoryProduct toDomain(List<FullProductDTO> dtoList) {

        if (dtoList.isEmpty()) {
            return null;
        }

        FullProductDTO dto = dtoList.get(0);

        Category category = new Category(dto.getCategoryId(), dto.getCategoryName());

        Subcategory subcategory = new Subcategory(
                dto.getSubcategoryId(), category, dto.getSubcategoryName());

        InventoryProduct inventoryProduct = new InventoryProduct(dto.getProductName());
        inventoryProduct.setId(dto.getProductId());
        inventoryProduct.setSubcategory(subcategory);
        inventoryProduct.setProductPresentation(dto.getProductPresentation());
        inventoryProduct.setMinimumStock(dto.getMinimumStock());
        inventoryProduct.setSellingPrice(
                dto.getRetailPrice(), dto.getWholesalePrice(), dto.getPriceCurrency());

        for (int i = 0; i < dtoList.size(); i++) {
            FullProductDTO d = dtoList.get(i);

            if (d.getStockLocationId() != null && d.getStockId() != null) {
                Optional<Stock> stockFound = inventoryProduct.stockManager
                        .findStockInLocation(d.getStockLocationId());

                if (!stockFound.isPresent()) {
                    Location location = new Location(
                            d.getStockLocationId(),
                            d.getStockLocationName(),
                            d.getStockLocationAddress());
                    Stock stock = d.getStockLocationMaximumStorage() == 0 ? new Stock(
                            d.getStockId(),
                            location,
                            d.getStockLocationQuantity())
                            : new Stock(
                                    d.getStockId(),
                                    location,
                                    d.getStockLocationQuantity(),
                                    d.getStockLocationMaximumStorage());
                    inventoryProduct.stockManager.addStock(stock);
                }
            }

            if (d.getSupplierId() != null) {
                Optional<Supplier> found = inventoryProduct.supplierManager.findSupplier(d.getSupplierId());
                if (!found.isPresent()) {
                    Supplier supplier = new Supplier(
                            d.getSupplierId(),
                            d.getSupplierName());
                    inventoryProduct.supplierManager.addSupplier(supplier);
                }
            }
        }

        return inventoryProduct;
    }
}
