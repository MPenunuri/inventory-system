package com.mapera.inventory_system.domain.aggregate.inventory_product;

import java.util.List;

import com.mapera.inventory_system.domain.entity.Stock;
import com.mapera.inventory_system.domain.entity.Subcategory;
import com.mapera.inventory_system.domain.entity.Supplier;
import com.mapera.inventory_system.domain.valueobject.MinimumStock;
import com.mapera.inventory_system.domain.valueobject.SellingPrice;

public class Product {
        private final int id;
        private String name;
        private Subcategory subcategory;
        private List<Stock> stockList;
        private String productPresentation;
        private List<Supplier> suppliers;
        private MinimumStock minimumStock;
        private SellingPrice sellingPrice;

        public Product(int id, String name, Subcategory subcategory, List<Stock> stockList,
                        String productPresentation, List<Supplier> suppliers,
                        MinimumStock minimumStock, SellingPrice sellingPrice) {
                this.id = id;
                this.name = name;
                this.subcategory = subcategory;
                this.stockList = stockList;
                this.productPresentation = productPresentation;
                this.suppliers = suppliers;
                this.minimumStock = minimumStock;
                this.sellingPrice = sellingPrice;
        }

        public Product(Integer id, String name) {
                this(id, name, null, null,
                                null, null,
                                null, null);
        }

        public Integer getId() {
                return this.id;
        }

        public String getName() {
                return this.name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Subcategory getSubcategory() {
                return this.subcategory;
        }

        public void setSubcategory(Subcategory subcategory) {
                this.subcategory = subcategory;
        }

        public List<Stock> getStockList() {
                return this.stockList;
        }

        public void setStockList(List<Stock> stockList) {
                this.stockList = stockList;
        }

        public String getProductPresentation() {
                return this.productPresentation;
        }

        public void setProductPresentation(String productPresentation) {
                this.productPresentation = productPresentation;
        }

        public List<Supplier> getSuppliers() {
                return this.suppliers;
        }

        public void setSuppliers(List<Supplier> suppliers) {
                this.suppliers = suppliers;
        }

        public MinimumStock getMinimumStock() {
                return this.minimumStock;
        }

        public void setMinimumStock() {
                this.minimumStock = new MinimumStock();
        }

        public void setMinimumStock(int minimumStock) {
                this.minimumStock = new MinimumStock(minimumStock);
        }

        public SellingPrice getSellingPrice() {
                return this.sellingPrice;
        }

        public void setSellingPrice(double retail, double wholesale, String currency) {
                SellingPrice sellingPrice = new SellingPrice(retail, wholesale, currency);
                this.sellingPrice = sellingPrice;
        }

        @Override
        public String toString() {
                return "{" +
                                " id='" + getId() + "'" +
                                ", name='" + getName() + "'" +
                                ", subcategory='" + getSubcategory() + "'" +
                                ", stockList='" + getStockList() + "'" +
                                ", productPresentation='" + getProductPresentation() + "'" +
                                ", suppliers='" + getSuppliers() + "'" +
                                ", minimumStock='" + getMinimumStock() + "'" +
                                ", sellingPrice='" + getSellingPrice() + "'" +
                                "}";
        }

}
