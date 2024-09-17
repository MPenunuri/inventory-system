CREATE TABLE IF NOT EXISTS users(
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    roles TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS categories(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS subcategories(
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS currencies(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    subcategory_id INT,
    product_presentation VARCHAR(255),
    minimum_stock INT CHECK (minimum_stock >= 0),
    retail_price DECIMAL(20, 2) CHECK (retail_price >= 0),
    wholesale_price DECIMAL(20, 2) CHECK (wholesale_price >= 0),
    price_currency_id INT,
    FOREIGN KEY (subcategory_id) REFERENCES subcategories(id),
    FOREIGN KEY (price_currency_id) REFERENCES currencies(id)
);

CREATE TABLE IF NOT EXISTS locations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS stock_list (
    id INT AUTO_INCREMENT PRIMARY KEY,
    location_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT CHECK (quantity >= 0) NOT NULL,
    maximum_storage INT,
    FOREIGN KEY (location_id) REFERENCES locations(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS suppliers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS product_supplier (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    supplier_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);

 CREATE TABLE IF NOt EXISTS movements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    date_time DATETIME NOT NULL,
    type VARCHAR(50) NOT NULL,
    subtype VARCHAR(50) NOT NULL,
    reason VARCHAR(300) NOT NULL,
    comment TEXT NOT NULL,
    quantity INT NOT NULL,
    supplier_id INT,
    from_location_id INT,
    to_location_id INT,
    transaction_type VARCHAR(50),
    transaction_subtype VARCHAR(50),
    transaction_value DECIMAL(20, 2),
    transaction_currency_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id),    
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    FOREIGN KEY (from_location_id) REFERENCES locations(id),
    FOREIGN KEY (to_location_id) REFERENCES locations(id),
    FOREIGN KEY (transaction_currency_id) REFERENCES currencies(id),
    CONSTRAINT type CHECK (type IN ('Entry', 'Output', 'Transfer')),
    CONSTRAINT subtype CHECK (subtype IN (
        'Acquisition', 'Customer return', 'Inventory adjustment', 'Production',
        'Sales', 'Supplier Return', 'Internal Consumption',
        'None'))
);