package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product;

public class ProductQuery {

        public static final String PRODUCT_ATTRIBUTES_SELECTION = "SELECT p.id AS product_id, " +
                        "p.name AS product_name, " +
                        "p.subcategory_id AS subcategory_id, " +
                        "p.product_presentation AS product_presentation, " +
                        "p.minimum_stock AS minimum_stock, " +
                        "p.retail_price AS retail_price, " +
                        "p.wholesale_price AS wholesale_price, ";

        public static final String STANDARD_QUERY = PRODUCT_ATTRIBUTES_SELECTION +
                        "p.user_id AS user_id, " +
                        "s.name AS subcategory_name, " +
                        "c.id AS category_id, " +
                        "c.name AS category_name, " +
                        "pc.id AS price_currency_id, " +
                        "pc.name AS price_currency, " +
                        "SUM(sl.quantity) AS total_stock " +
                        "FROM products p " +
                        "LEFT JOIN subcategories s ON p.subcategory_id = s.id " +
                        "LEFT JOIN categories c ON s.category_id = c.id " +
                        "LEFT JOIN currencies pc ON pc.id = p.price_currency_id " +
                        "LEFT JOIN stock_list sl ON sl.product_id = p.id " +
                        "GROUP BY p.id, p.user_id, p.name, p.subcategory_id, p.product_presentation, p.minimum_stock, "
                        +
                        "p.retail_price, p.wholesale_price, s.name, c.id, c.name, pc.name ";

        public static final String STOCK_DATA_CTE = "WITH StockData AS ( " +
                        "SELECT sl.product_id AS product_id, sl.id AS stock_id, " +
                        "sl.location_id AS stock_location_id, " +
                        "l.name AS stock_location_name, " +
                        "l.address AS stock_location_address, " +
                        "sl.quantity AS stock_location_quantity, " +
                        "sl.maximum_storage AS stock_location_maximum_storage " +
                        "FROM stock_list sl " +
                        "JOIN locations l ON l.id = sl.location_id " +
                        "WHERE sl.product_id = :productId), ";

        public static final String SUPPLIER_DATA_CTE = "SupplierData AS ( " +
                        "SELECT ps.product_id AS product_id, ps.supplier_id AS supplier_id, " +
                        "su.name AS supplier_name " +
                        "FROM product_supplier ps " +
                        "JOIN suppliers su ON su.id = ps.supplier_id " +
                        "WHERE ps.product_id = :productId) ";

        public static final String FULL_QUERY = STOCK_DATA_CTE +
                        SUPPLIER_DATA_CTE +
                        PRODUCT_ATTRIBUTES_SELECTION +
                        "s.name AS subcategory_name, " +
                        "c.id AS category_id, " +
                        "c.name AS category_name, " +
                        "pc.name AS price_currency, " +
                        "sd.stock_id AS stock_id, " +
                        "sd.stock_location_id AS stock_location_id, " +
                        "sd.stock_location_name AS stock_location_name, " +
                        "sd.stock_location_address AS stock_location_address, " +
                        "sd.stock_location_quantity AS stock_location_quantity, " +
                        "sd.stock_location_maximum_storage AS stock_location_maximum_storage, " +
                        "su.supplier_id AS supplier_id, " +
                        "su.supplier_name AS supplier_name " +
                        "FROM products p " +
                        "LEFT JOIN subcategories s ON p.subcategory_id = s.id " +
                        "LEFT JOIN categories c ON s.category_id = c.id " +
                        "LEFT JOIN StockData sd ON sd.product_id = p.id " +
                        "LEFT JOIN SupplierData su ON su.product_id = p.id " +
                        "LEFT JOIN currencies pc ON pc.id = p.price_currency_id " +
                        "WHERE p.id = :productId " +
                        "AND p.user_id = :userId ";

        public static final String SUPPLIER_QUERY = PRODUCT_ATTRIBUTES_SELECTION +
                        "s.name AS subcategory_name, " +
                        "c.id AS category_id, " +
                        "c.name AS category_name, " +
                        "pc.name AS price_currency, " +
                        "su.id AS supplier_id, " +
                        "su.name AS supplier_name " +
                        "FROM products p " +
                        "LEFT JOIN subcategories s ON p.subcategory_id = s.id " +
                        "LEFT JOIN categories c ON s.category_id = c.id  " +
                        "LEFT JOIN product_supplier ps ON ps.product_id = p.id " +
                        "LEFT JOIN suppliers su ON su.id = ps.supplier_id " +
                        "LEFT JOIN currencies pc ON pc.id = p.price_currency_id ";

        public static final String NO_SUPPLIER_QUERY = "SELECT DISTINCT p.id AS product_id, " +
                        "p.name AS product_name, " +
                        "p.subcategory_id AS subcategory_id, " +
                        "p.product_presentation AS product_presentation, " +
                        "s.name AS subcategory_name, " +
                        "c.id AS category_id, " +
                        "c.name AS category_name " +
                        "FROM products p " +
                        "LEFT JOIN subcategories s ON p.subcategory_id = s.id " +
                        "LEFT JOIN categories c ON s.category_id = c.id " +
                        "WHERE p.user_id = :userId " +
                        "AND NOT EXISTS ( " +
                        "    SELECT 1 " +
                        "    FROM product_supplier ps " +
                        "    WHERE ps.product_id = p.id " +
                        "    AND ps.supplier_id = :supplierId " +
                        ")";

        public static final String LOCATION_QUERY = PRODUCT_ATTRIBUTES_SELECTION +
                        "s.name AS subcategory_name, " +
                        "c.id AS category_id, " +
                        "c.name AS category_name, " +
                        "pc.id AS price_currency_id, " +
                        "pc.name AS price_currency, " +
                        "l.id AS stock_location_id, " +
                        "l.name AS stock_location_name, " +
                        "l.address AS stock_location_address, " +
                        "sl.quantity AS stock_location_quantity, " +
                        "sl.maximum_storage AS stock_location_maximum_storage " +
                        "FROM products p " +
                        "LEFT JOIN subcategories s ON p.subcategory_id = s.id " +
                        "LEFT JOIN categories c ON s.category_id = c.id " +
                        "LEFT JOIN stock_list sl ON sl.product_id = p.id " +
                        "LEFT JOIN locations l ON l.id = sl.location_id " +
                        "LEFT JOIN currencies pc ON pc.id = p.price_currency_id ";

        public static final String MINIMUM_STOCK_QUERY = "SELECT p.id, p.name, p.product_presentation, p.minimum_stock, "
                        +
                        "IFNULL(SUM(sl.quantity), 0) AS total_stock " +
                        "FROM products p " +
                        "LEFT JOIN stock_list sl ON sl.product_id = p.id " +
                        "WHERE p.user_id = :userId " +
                        "GROUP BY p.id " +
                        "HAVING (p.minimum_stock IS NOT NULL AND total_stock < p.minimum_stock) " +
                        " OR (p.minimum_stock IS NOT NULL AND total_stock = 0) ";

        public static final String STOCK_QUERY = "SELECT p.id, p.name, p.product_presentation, p.minimum_stock, " +
                        "SUM(sl.quantity) AS total_stock " +
                        "FROM products p " +
                        "LEFT JOIN stock_list sl ON sl.product_id = p.id " +
                        "WHERE p.id = :productId AND p.user_id = :userId " +
                        "GROUP BY p.id ";
}