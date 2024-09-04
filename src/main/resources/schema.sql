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
    minimum_stock INT,
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