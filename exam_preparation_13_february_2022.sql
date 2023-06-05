CREATE DATABASE online_store;
CREATE TABLE brands (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(40) NOT NULL UNIQUE
);
CREATE TABLE categories(
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(40) NOT NULL UNIQUE
);
CREATE TABLE reviews(
id INT PRIMARY KEY AUTO_INCREMENT,
content TEXT,
rating DECIMAL(10,2) NOT NULL,
picture_url VARCHAR(80) NOT NULL,
published_at DATETIME NOT NULL
);
CREATE TABLE products(
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(40) NOT NULL,
price DECIMAL(19,2) NOT NULL,
quantity_in_stock INT,
`description` TEXT,
brand_id INT NOT NULL,
category_id INT NOT NULL,
review_id INT,
CONSTRAINT fk_product_brands FOREIGN KEY (brand_id)
REFERENCES brands(id),
CONSTRAINT fk_product_categories FOREIGN KEY (category_id)
REFERENCES categories(id),
CONSTRAINT fk_product_reviews FOREIGN KEY(review_id) 
REFERENCES reviews(id)
 );
 CREATE TABLE customers(
 id INT PRIMARY KEY AUTO_INCREMENT,
 first_name VARCHAR(20) NOT NULL,
 last_name VARCHAR(20) NOT NULL,
 phone VARCHAR(30) NOT NULL UNIQUE,
 address VARCHAR(60) NOT NULL,
 discount_card BIT NOT NULL DEFAULT 0
 );
 CREATE TABLE orders(
 id INT PRIMARY KEY AUTO_INCREMENT,
 order_datetime DATETIME NOT NULL,
 customer_id INT,
 CONSTRAINT fk_orders_customers FOREIGN KEY(customer_id)
REFERENCES customers(id)
 );
 CREATE TABLE orders_products (
    order_id INT,
    product_id INT,
    CONSTRAINT fk_orders_products_orders FOREIGN KEY (order_id)
        REFERENCES orders(id),
    CONSTRAINT fk_orders_products_products FOREIGN KEY (product_id)
        REFERENCES products (id)
);
-- 2
INSERT INTO reviews (content,picture_url,published_at,rating)
SELECT LEFT(p.description, 15), reverse(p.name), DATE('2010/10/10'), p.price / 8
FROM products as p
WHERE p.id >= 5;


 
 # You will have to insert records of data into the reviews table, based on the products table.
#For products with id equal or greater than 5, insert data in the reviews table with the following values:
#•	content – set it to the first 15 characters from the description of the product.
#•	picture_url – set it to the product's name but reversed.
#•	published_at – set it to 10-10-2010.
#•	rating – set it to the price of the product divided by 8.

 -- 3
UPDATE products p
SET p.quantity_in_stock = p.quantity_in_stock - 5
WHERE p.quantity_in_stock BETWEEN 60 AND 70;
 
 
 
 
