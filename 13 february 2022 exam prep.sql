CREATE SCHEMA online_store;

CREATE TABLE brands(
id INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE categories(
id INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(40) NOT NULL UNIQUE
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
`name` VARCHAR(40) NOT NULL,
price DECIMAL(19,2) NOT NULL,
quantity_in_stock INT,
`description` TEXT,
brand_id INT,
category_id INT NOT NULL,
review_id INT,
CONSTRAINT fk_products_brands FOREIGN KEY (brand_id)
REFERENCES brands(id),
CONSTRAINT fk_products_categories FOREIGN KEY (category_id)
REFERENCES categories(id),
CONSTRAINT fk_products_reviews FOREIGN KEY (review_id)
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

CREATE TABLE orders (
id INT PRIMARY KEY AUTO_INCREMENT,
order_datetime DATETIME NOT NULL,
customer_id INT NOT NULL,
CONSTRAINT fk_orders_customers FOREIGN KEY (customer_id)
REFERENCES customers(id)
);

CREATE TABLE orders_products(
order_id INT,
product_id INT,
CONSTRAINT fk_orders_product_orders FOREIGN KEY (order_id)
REFERENCES orders(id),
CONSTRAINT fk_orders_product_products FOREIGN KEY (product_id) 
REFERENCES products(id)
);

-- 2
INSERT INTO reviews(content,picture_url,published_at,rating)
SELECT left(p.`description`,15),(reverse(p.name)),DATE('2010/10/10'),p.price / 8
FROM products p
WHERE p.id >=5;

-- 3
UPDATE products p
SET p.quantity_in_stock = p.quantity_in_stock - 5
WHERE p.quantity_in_stock BETWEEN 60 AND 70;

-- 4
DELETE c FROM customers c
LEFT JOIN orders o on  c.id = o.customer_id
WHERE o.customer_id is NULL;

-- 5
SELECT * FROM categories as c
ORDER BY c.name DESC;

-- 6
SELECT id,brand_id,name,quantity_in_stock FROM products as p
WHERE price > 1000 AND quantity_in_stock < 30
ORDER BY quantity_in_stock ;

-- 7
SELECT * FROM reviews as r
WHERE (SELECT r.content LIKE('My%') AND length(r.content) > 61)
ORDER BY r.rating DESC;

-- 8
SELECT 
    CONCAT(c.first_name, ' ', last_name) AS 'full_name',
    address,
    o.order_datetime AS 'order_date'
FROM
    customers AS c
        JOIN
    orders AS o ON o.customer_id = c.id
    WHERE year(o.order_datetime) <= 2018
ORDER BY full_name DESC;

-- 9
SELECT 
    COUNT(c.id) AS 'items_count',
    c.name,
    SUM(p.quantity_in_stock) AS 'total_quantity'
FROM
    products AS p
        JOIN
    categories AS c ON c.id = p.category_id
GROUP BY c.id
ORDER BY `items_count` DESC , `total_quantity` ASC
LIMIT 5;

-- 10
DELIMITER $$
CREATE FUNCTION udf_customer_products_count(name VARCHAR(30))
RETURNS INT
DETERMINISTIC
BEGIN
DECLARE products_count INT;
SET products_count := (SELECT COUNT(c.id) FROM customers as c
JOIN orders as o on c.id = o.customer_id
JOIN orders_products as op ON o.id = op.order_id
WHERE c.first_name = name);
RETURN products_count;
END$$
DELIMITER ;

-- 11
DELIMITER $$
CREATE PROCEDURE udp_reduce_price(`category_name`VARCHAR(50))
 BEGIN 
 UPDATE products as p
 JOIN reviews as r ON r.id = p.review_id
 JOIN categories as c ON c.id = p.category_id
 SET p.price = price * 0.70
WHERE c.name = category_name AND r.rating <= 4;
End$$
DELIMITER ;


