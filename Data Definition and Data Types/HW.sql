--12
CREATE TABLE `categories` (
	id INT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(20),
    daily_rate DOUBLE,
    weekly_rate DOUBLE,
    monthly_rate DOUBLE,
    weekend_rate DOUBLE
);

INSERT INTO `categories` (`category`)
VALUES ("TestName1"), ("TestName2"), ("TestName3");

CREATE TABLE `cars` (
    `id` INT PRIMARY KEY AUTO_INCREMENT, 
    `plate_number` VARCHAR(20),
    `make` VARCHAR(20),
    `model` VARCHAR(20),
    `car_year` INT,
    `category_id` INT,
    `doors` INT,
    `picture` BLOB,
    `car_condition` VARCHAR(30),
    `available` BOOLEAN   
);
 
INSERT INTO `cars` (`plate_number`)
VALUES 
('TestName1'),
('TestName2'),
('TestName3');
 
CREATE TABLE `employees` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(50),
    `last_name` VARCHAR(50),
    `title` VARCHAR(50),
    `notes` TEXT
);
 
INSERT INTO `employees` (`first_name`, `last_name`)
VALUES 
('TestName1', 'TestName1'),
('TestName2', 'TestName2'),
('TestName3', 'TestName3');
 
CREATE TABLE `customers` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `driver_license` VARCHAR(20),
    `full_name` VARCHAR(50),
    `address` VARCHAR(50),
    `city` VARCHAR(10),
    `zip_code` VARCHAR(10),
    `notes` TEXT
);
 
INSERT INTO `customers` (`driver_license`, `full_name`)
VALUES 
('TestName1', 'TestName1'),
('TestName2', 'TestName2'),
('TestName3', 'TestName3');
 
CREATE TABLE `rental_orders` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `employee_id` INT,
    `customer_id` INT,
    `car_id` INT,
    `car_condition` VARCHAR(50),
    `tank_level` VARCHAR(20),
    `kilometrage_start` INT,
    `kilometrage_end` INT,
    `total_kilometrage` INT,
    `start_date` DATE, 
    `end_date` DATE, 
    `total_days` INT,
    `rate_applied` DOUBLE,
    `tax_rate` DOUBLE,
    `order_status` VARCHAR(20),
    `notes` TEXT
);
 
INSERT INTO `rental_orders` (`employee_id`, `customer_id`)
VALUES 
(1, 2),
(2, 3),
(3, 1);

-- 13
INSERT INTO `towns` (`name`)
VALUES ("Sofia"), ("Plovdiv"), ("Varna"), ("Burgas");

INSERT INTO `departments` (`name`)
VALUES ("Engineering"), ("Sales"), ("Marketing"), 
	("Software Development"), ("Quality Assurance");
    
INSERT INTO `employees` (`first_name`, `middle_name`, `last_name`,
	`job_title`, `department_id`, `hire_date`, `salary`)
VALUES
('Ivan', 'Ivanov', 'Ivanov', '.NET Developer', 4, '2013-02-01', 3500.00),
('Petar', 'Petrov', 'Petrov', 'Senior Engineer', 1, '2004-03-02', 4000.00),
('Maria', 'Petrova', 'Ivanova', 'Intern', 5, '2016-08-28', 525.25),
('Georgi', 'Terziev', 'Ivanov', 'CEO', 2, '2007-12-09', 3000.00),
('Peter', 'Pan', 'Pan', 'Intern', 3, '2016-08-28', 599.88);

-- 14
SELECT * FROM `towns`;

SELECT * FROM `departments`;

SELECT * FROM `employees`;

-- 15
SELECT * FROM `towns`
ORDER BY `name`;

SELECT * FROM `departments`
ORDER BY `name`;

SELECT * FROM `employees`
ORDER BY `salary` DESC;

-- 16
SELECT `name` FROM `towns`
ORDER BY `name`;

SELECT `name` FROM `departments`
ORDER BY `name`;

SELECT `first_name`, `last_name`, `job_title`, `salary` FROM `employees`
ORDER BY `salary` DESC;

-- 17
UPDATE `employees`
SET `salary` = `salary` * 1.10;

SELECT `salary` FROM `employees`;