package com.mapera.inventory_system.domain.aggregate.inventory_product;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.domain.entity.Stock;

public class StockManager {
    private final List<Stock> stockList;

    protected StockManager(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public void addStock(int id, Location location, int quantity, int maximumStorage) {
        Stock stock = new Stock(id, location, quantity, maximumStorage);
        this.stockList.add(stock);
    }

    public void addStock(int id, Location location, int quantity) {
        Stock stock = new Stock(id, location, quantity);
        this.stockList.add(stock);
    }

    public boolean removeStock(int id, Location location) {
        Iterator<Stock> iterator = this.stockList.iterator();
        while (iterator.hasNext()) {
            Stock stock = iterator.next();
            if (stock.getId() == id) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public Stock getStockInLocation(int locationId) {
        Optional<Stock> found = this.stockList.stream()
                .filter(stock -> stock.getLocation().getId() == locationId)
                .findFirst();
        if (!found.isPresent()) {
            throw new IllegalArgumentException("Stock not found");
        }
        Stock stock = found.get();
        return stock;
    }

    public void increseStockInLocation(int locationId, int quantity) {
        Stock stock = getStockInLocation(locationId);
        stock.increase(quantity);
    }

    public void decreaseStockInLocation(int locationId, int quantity) {
        Stock stock = getStockInLocation(locationId);
        stock.decrease(quantity);
    }

    public void setMaximumStockInLocation(int locationId, int quantity) {
        Stock stock = getStockInLocation(locationId);
        stock.setMaximumStorage(quantity);
    }

}
