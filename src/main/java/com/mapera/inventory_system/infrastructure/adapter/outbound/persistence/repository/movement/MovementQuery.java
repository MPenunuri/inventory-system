package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement;

public class MovementQuery {

        public static final String STANDARD_SELECTION = "SELECT m.id AS movement_id, " +
                        "p.id AS product_id, " +
                        "p.name AS product_name, " +
                        "p.product_presentation AS product_presentation, " +
                        "m.date_time AS date_time, " +
                        "m.type AS type, " +
                        "m.subtype AS subtype, " +
                        "m.reason AS reason, " +
                        "m.comment AS comment, " +
                        "m.quantity AS quantity ";

        public static final String STANDARD_QUERY = "FROM movements m " +
                        "JOIN products p ON p.id = m.product_id ";

        public static final String ALL_QUERY = STANDARD_SELECTION +
                        STANDARD_QUERY;

        public static final String ENTRY_QUERY = STANDARD_SELECTION +
                        ", m.to_location_id AS to_location_id, " +
                        "l.name AS to_location_name " +
                        STANDARD_QUERY +
                        "JOIN locations l ON l.id = m.to_location_id " +
                        "WHERE m.type = 'Entry' ";

        public static final String OUTPUT_QUERY = STANDARD_SELECTION +
                        ", m.from_location_id AS from_location_id, " +
                        "l.name AS from_location_name " +
                        STANDARD_QUERY +
                        "JOIN locations l ON l.id = m.from_location_id " +
                        "WHERE m.type = 'Output' ";

        public static final String TRANSFER_QUERY = STANDARD_SELECTION +
                        ", m.from_location_id AS from_location_id, " +
                        "l_from.name AS from_location_name, " +
                        "m.to_location_id AS to_location_id, " +
                        "l_to.name AS to_location_name " +
                        STANDARD_QUERY +
                        "JOIN locations l_from ON l_from.id = m.from_location_id " +
                        "JOIN locations l_to ON l_to.id = m.to_location_id " +
                        "WHERE m.type = 'Transfer' ";

        public static final String ACQUISITION_QUERY = STANDARD_SELECTION +
                        ", m.to_location_id AS to_location_id, " +
                        "l.name AS to_location_name, " +
                        "s.id AS supplier_id, " +
                        "s.name AS supplier_name, " +
                        "m.transaction_subtype AS cost_type, " +
                        "m.transaction_value AS cost, " +
                        "c.name AS cost_currency " +
                        STANDARD_QUERY +
                        "JOIN locations l ON l.id = m.to_location_id " +
                        "JOIN suppliers s ON s.id = m.supplier_id " +
                        "JOIN currencies c ON c.id = m.transaction_currency_id " +
                        "WHERE m.subtype = 'Acquisition' ";

        public static final String CUSTOMER_RETURN_QUERY = STANDARD_SELECTION +
                        ", m.to_location_id AS to_location_id, " +
                        "l.name AS to_location_name, " +
                        "m.transaction_subtype AS cost_type, " +
                        "m.transaction_value AS cost, " +
                        "c.name AS cost_currency " +
                        STANDARD_QUERY +
                        "JOIN locations l ON l.id = m.to_location_id " +
                        "JOIN currencies c ON c.id = m.transaction_currency_id " +
                        "WHERE m.subtype = 'Customer return' ";

        public static final String ENTRY_ADJUSMENT_QUERY = ENTRY_QUERY +
                        "AND m.subtype = 'Inventory adjustment' ";

        public static final String PRODUCTION_QUERY = STANDARD_SELECTION +
                        ", m.to_location_id AS to_location_id, " +
                        "l.name AS to_location_name, " +
                        "m.transaction_subtype AS cost_type, " +
                        "m.transaction_value AS cost, " +
                        "c.name AS cost_currency " +
                        STANDARD_QUERY +
                        "JOIN locations l ON l.id = m.to_location_id " +
                        "JOIN currencies c ON c.id = m.transaction_currency_id " +
                        "WHERE m.subtype = 'Production' ";

        public static final String OUTPUT_ADJUSMENT_QUERY = OUTPUT_QUERY +
                        "AND m.subtype = 'Inventory adjustment' ";

        public static final String SALES_QUERY = STANDARD_SELECTION +
                        ", m.from_location_id AS from_location_id, " +
                        "l.name AS to_location_name, " +
                        "m.transaction_subtype AS sell_type, " +
                        "m.transaction_value AS sell, " +
                        "c.name AS sell_currency " +
                        STANDARD_QUERY +
                        "JOIN locations l ON l.id = m.from_location_id " +
                        "JOIN currencies c ON c.id = m.transaction_currency_id " +
                        "WHERE m.subtype = 'Sales' ";

        public static final String SUPPLIER_RETURN_QUERY = STANDARD_SELECTION +
                        ", m.from_location_id AS from_location_id, " +
                        "l.name AS from_location_name, " +
                        "s.id AS supplier_id,  " +
                        "s.name AS supplier_name, " +
                        "m.transaction_subtype AS refund_type, " +
                        "m.transaction_value AS refund, " +
                        "c.name AS refund_currency " +
                        STANDARD_QUERY +
                        "JOIN locations l ON l.id = m.from_location_id " +
                        "JOIN suppliers s ON s.id = m.supplier_id " +
                        "JOIN currencies c ON c.id = m.transaction_currency_id " +
                        "WHERE m.subtype = 'Supplier Return' ";

        public static final String INTERNAL_CONSUMPTION_QUERY = OUTPUT_QUERY +
                        "AND m.subtype = 'Internal Consumption' ";

        public static final String PRODUCT_QUERY = STANDARD_SELECTION +
                        STANDARD_QUERY +
                        "WHERE p.id = :productId ";

        public static final String SUPPLIER_QUERY = ACQUISITION_QUERY +
                        "AND m.supplier_id = :supplierId ";

        public static final String ACQUISITION_COST_QUERY = ACQUISITION_QUERY +
                        "AND m.transaction_value >= :minCost " +
                        "AND m.transaction_value <= :maxCost " +
                        "AND m.transaction_currency_id = :currencyId " +
                        "AND YEAR(m.date_time) >= :fromYear AND YEAR(m.date_time) <= :toYear " +
                        "AND m.transaction_subtype = :costType";

        public static final String AVERAGE_COST_QUERY = "SELECT m.product_id AS product_id, " +
                        "p.name AS product_name, " +
                        "c.id AS product_category_id, " +
                        "c.name AS product_category, " +
                        "s.id AS product_subcategory_id, " +
                        "s.name AS product_subcategory, " +
                        "p.product_presentation AS product_presentation, " +
                        "m.transaction_subtype AS cost_type, " +
                        "ROUND(AVG(m.transaction_value), 2) AS average_cost_value, " +
                        "cur.name AS cost_currency " +
                        "FROM movements m " +
                        "JOIN products p ON p.id = m.product_id " +
                        "LEFT JOIN subcategories s ON s.id = p.subcategory_id " +
                        "LEFT JOIN categories c ON c.id = s.category_id " +
                        "LEFT JOIN currencies cur ON cur.id = m.transaction_currency_id " +
                        "WHERE m.product_id = :productId AND m.transaction_currency_id = :currencyId " +
                        "AND YEAR(m.date_time) >= :fromYear AND YEAR(m.date_time) <= :toYear ";

        public static final String AVERAGE_COST_PERUNIT_QUERY = AVERAGE_COST_QUERY +
                        "AND m.transaction_subtype = 'PER_UNIT' ";

        public static final String AVERAGE_COST_PERUNIT_ACQUISITION = AVERAGE_COST_PERUNIT_QUERY +
                        "AND m.subtype = 'Acquisition' " +
                        "GROUP BY m.product_id ";

        public static final String AVERAGE_COST_PERUNIT_PRODUCTION = AVERAGE_COST_PERUNIT_QUERY +
                        "AND m.subtype = 'Production' " +
                        "GROUP BY p.id ";

        public static final String AVERAGE_COST_OVERALL_QUERY = AVERAGE_COST_QUERY +
                        "AND m.transaction_subtype = 'OVERALL' ";

        public static final String AVERAGE_COST_OVERALL_ACQUISITION = AVERAGE_COST_OVERALL_QUERY +
                        "AND m.subtype = 'Acquisition' " +
                        "GROUP BY p.id ";

        public static final String AVERAGE_COST_OVERALL_PRODUCTION = AVERAGE_COST_OVERALL_QUERY +
                        "AND m.subtype = 'Production' " +
                        "GROUP BY p.id ";

        public static final String PRODUCTION_COST_QUERY = PRODUCTION_QUERY +
                        "AND m.transaction_value >= :minCost " +
                        "AND m.transaction_value <= :maxCost " +
                        "AND m.transaction_currency_id = :currencyId " +
                        "AND YEAR(m.date_time) >= :fromYear AND YEAR(m.date_time) <= :toYear " +
                        "AND m.transaction_subtype = :costType";

        public static final String SALES_VALUE_QUERY = SALES_QUERY +
                        "AND m.transaction_value >= :minCost " +
                        "AND m.transaction_value <= :maxCost " +
                        "AND m.transaction_currency_id = :currencyId " +
                        "AND YEAR(m.date_time) >= :fromYear AND YEAR(m.date_time) <= :toYear " +
                        "AND m.transaction_subtype = :sellType ";

        public static final String AVERAGE_SELL_QUERY = "SELECT m.product_id AS product_id, " +
                        "p.name AS product_name, " +
                        "c.id AS product_category_id, " +
                        "c.name AS product_category, " +
                        "s.id AS product_subcategory_Id, " +
                        "s.name AS product_subcategory, " +
                        "p.product_presentation AS product_presentation, " +
                        "m.transaction_subtype AS sell_type, " +
                        "ROUND(AVG(m.transaction_value), 2) AS average_sell_value, " +
                        "cur.name AS sell_currency, " +
                        "SUM(m.quantity) AS sells " +
                        "FROM movements m " +
                        "JOIN products p ON p.id = m.product_id " +
                        "LEFT JOIN subcategories s ON s.id = p.subcategory_id " +
                        "LEFT JOIN categories c ON c.id = s.category_id " +
                        "LEFT JOIN currencies cur ON cur.id = m.transaction_currency_id " +
                        "WHERE m.product_id = :productId AND m.transaction_currency_id = :currencyId " +
                        "AND YEAR(m.date_time) >= :fromYear AND YEAR(m.date_time) <= :toYear " +
                        "AND m.subtype = 'Sales' ";

        public static final String AVERAGE_SELL_RETAIL = AVERAGE_SELL_QUERY +
                        "AND m.transaction_subtype = 'RETAIL' " +
                        "GROUP BY p.id ";

        public static final String AVERAGE_SELL_WHOLESALE = AVERAGE_SELL_QUERY +
                        "AND m.transaction_subtype = 'WHOLESALE' " +
                        "GROUP BY p.id ";

}