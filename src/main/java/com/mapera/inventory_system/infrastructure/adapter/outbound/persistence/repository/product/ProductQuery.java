package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product;

public class ProductQuery {

        public static final String PRODUCT_ATTRIBUTES_SELECTION = "SELECT p.id AS product_id, " +
                        "p.name AS productName, " +
                        "p.subcategory_id AS subcategoryId, " +
                        "p.product_presentation AS productPresentation, " +
                        "p.minimum_stock AS minimumStock, " +
                        "p.retail_price AS retailPrice, " +
                        "p.wholesale_price AS wholesalePrice, " +
                        "p.price_currency AS priceCurrency, ";

        public static final String STANDARD_QUERY = PRODUCT_ATTRIBUTES_SELECTION +
                        "s.name AS subcategoryName, " +
                        "c.id AS categoryId, " +
                        "c.name AS categoryName " +
                        "FROM products p " +
                        "JOIN subcategories s ON p.subcategory_id = s.id " +
                        "JOIN categories c ON s.category_id = c.id ";

        public static final String FULL_QUERY = PRODUCT_ATTRIBUTES_SELECTION +
                        "s.name AS subcategoryName, " +
                        "c.id AS categoryId, " +
                        "c.name AS categoryName, " +
                        "l.id AS stockLocationId, " +
                        "l.name AS stockLocationName, " +
                        "l.address AS stockLocationAddress, " +
                        "sl.quantity AS stockLocationQuantity, " +
                        "sl.maximum_storage AS stockLocationMaximumStorage, " +
                        "su.id AS supplierId, " +
                        "su.name AS supplierName " +
                        "FROM products p " +
                        "JOIN subcategories s ON p.subcategory_id = s.id " +
                        "JOIN categories c ON s.category_id = c.id  " +
                        "LEFT JOIN stock_list sl ON sl.product_id = p.id " +
                        "LEFT JOIN locations l ON l.id = sl.location_id " +
                        "LEFT JOIN product_supplier ps ON ps.product_id = p.id " +
                        "LEFT JOIN suppliers su ON su.id = ps.supplier_id ";

        public static final String SUPPLIER_QUERY = PRODUCT_ATTRIBUTES_SELECTION +
                        "s.name AS subcategoryName, " +
                        "c.id AS categoryId, " +
                        "c.name AS categoryName, " +
                        "su.id AS supplierId, " +
                        "su.name AS supplierName, " +
                        "FROM products p " +
                        "JOIN subcategories s ON p.subcategory_id = s.id " +
                        "JOIN categories c ON s.category_id = c.id  " +
                        "LEFT JOIN product_supplier ps ON ps.product_id = p.id " +
                        "LEFT JOIN suppliers su ON su.id = ps.supplier_id ";

        public static final String LOCATION_QUERY = PRODUCT_ATTRIBUTES_SELECTION +
                        "s.name AS subcategoryName, " +
                        "c.id AS categoryId, " +
                        "c.name AS categoryName, " +
                        "l.id AS stockLocationId, " +
                        "l.name AS stockLocationName, " +
                        "l.address AS stockLocationAddress, " +
                        "sl.quantity AS stockLocationQuantity, " +
                        "sl.maximum_storage AS stockLocationMaximumStorage " +
                        "FROM products p " +
                        "JOIN subcategories s ON p.subcategory_id = s.id " +
                        "JOIN categories c ON s.category_id = c.id " +
                        "LEFT JOIN stock_list sl ON sl.product_id = p.id " +
                        "LEFT JOIN locations l ON l.id = sl.location_id ";

        public static final String STOCK_QUERY = "SELECT p.id, p.name, p.product_presentation, p.minimum_stock, " +
                        "SUM(sl.quantity) AS total_stock " +
                        "FROM products p " +
                        "JOIN stock_list sl ON sl.product_id = p.id " +
                        "GROUP BY p.id ";
}