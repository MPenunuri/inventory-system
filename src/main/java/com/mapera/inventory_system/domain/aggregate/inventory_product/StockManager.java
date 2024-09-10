package com.mapera.inventory_system.domain.aggregate.inventory_product;

import java.util.List;
import java.util.Iterator;
import java.util.Optional;

import com.mapera.inventory_system.domain.entity.Stock;

public class StockManager {
    private final List<Stock> stockList;

    protected StockManager(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public int getTotalStock() {
        int stock = 0;
        for (int i = 0; i < this.stockList.size(); i++) {
            stock += this.stockList.get(i).getQuantity();
        }
        return stock;
    }

    private void validateStockAddition(Optional<Stock> stockFound, Optional<Stock> locationFound) {
        String errMsgStock = "Cannot add a new stock to stocklist if stocklist contains a stock with the same id";
        String errMsgLocation = "Cannot add a new stock to stocklist if stocklist contains a stock with the same location";
        if (stockFound.isPresent()) {
            throw new IllegalArgumentException(errMsgStock);
        }
        if (locationFound.isPresent()) {
            throw new IllegalArgumentException(errMsgLocation);
        }
    }

    public void addStock(Stock stock) {
        Optional<Stock> stockFound = findStockById(stock.getId());
        Optional<Stock> locationFound = findStockInLocation(stock.getLocation().getId());
        validateStockAddition(stockFound, locationFound);
        this.stockList.add(stock);
    }

    public boolean removeStock(long id) {
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

    public Optional<Stock> findStockById(long stockId) {
        return this.stockList.stream()
                .filter(stock -> stock.getId() == stockId)
                .findFirst();
    }

    public Optional<Stock> findStockInLocation(long locationId) {
        return this.stockList.stream()
                .filter(stock -> stock.getLocation().getId() == locationId)
                .findFirst();
    }

    public Stock getStockInLocation(long locationId) {
        Optional<Stock> found = findStockInLocation(locationId);
        if (!found.isPresent()) {
            throw new IllegalArgumentException("Stock not found");
        }
        Stock stock = found.get();
        return stock;
    }

    public void increseStockInLocation(long locationId, int quantity) {
        Stock stock = getStockInLocation(locationId);
        stock.increase(quantity);
    }

    public void decreaseStockInLocation(long locationId, int quantity) {
        Stock stock = getStockInLocation(locationId);
        stock.decrease(quantity);
    }

    public void setMaximumStockInLocation(long locationId, int quantity) {
        Stock stock = getStockInLocation(locationId);
        stock.setMaximumStorage(quantity);
    }

}
