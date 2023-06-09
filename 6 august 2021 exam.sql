CREATE SCHEMA SoftUni_Game_Dev_Branch;

-- 1 
CREATE TABLE addresses(
id INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL
);

CREATE TABLE categories(
id INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(10) NOT NULL
);

CREATE TABLE offices(
id INT PRIMARY KEY AUTO_INCREMENT,
workspace_capacity INT NOT NULL,
website VARCHAR(50),
address_id INT NOT NULL,
CONSTRAINT fk_offices_addresses FOREIGN KEY (address_id)
REFERENCES addresses(id)
);

CREATE TABLE employees(
id INT PRIMARY KEY AUTO_INCREMENT,
first_name VARCHAR(30) NOT NULL,
last_name VARCHAR(30) NOT NULL,
age INT NOT NULL,
salary DECIMAL(10,2) NOT NULL,
job_title VARCHAR(20) NOT NULL,
happiness_level CHAR(1) NOT NULL
);

CREATE TABLE teams(
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(40) NOT NULL,
office_id INT NOT NULL,
leader_id INT NOT NULL UNIQUE,
CONSTRAINT fk_teams_offices FOREIGN KEY (office_id)
REFERENCES offices(id),
CONSTRAINT fk_teams_employees FOREIGN KEY (leader_id)
REFERENCES employees(id)
);

CREATE TABLE games(
id INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL UNIQUE,
`description` TEXT ,
rating FLOAT NOT NULL DEFAULT 5.5,
budget DECIMAL(10,2) NOT NULL,
release_date DATE,
team_id INT NOT NULL,
CONSTRAINT fk_games_teams FOREIGN KEY (team_id)
REFERENCES teams(id)
);

CREATE TABLE games_categories(
game_id INT NOT NULL,
category_id INT NOT NULL,
CONSTRAINT composite_pk_games_categories PRIMARY KEY (game_id,category_id)
);

-- 2
INSERT INTO games (`name`,rating,budget,team_id)
(
SELECT lower(reverse(substring(`name`,2,char_length(`name`)))),
id,(leader_id * 1000),id FROM teams
WHERE id BETWEEN 1 AND 9
);

-- 3

UPDATE employees as e
SET e.salary = e.salary + 1000
WHERE e.age <= 40 AND e.salary <= 5000;

SET SQL_SAFE_UPDATES = 0;
-- 4 
DELETE g FROM games g
        LEFT JOIN
    games_categories AS gc ON gc.game_id = g.id
        LEFT JOIN
    categories AS c ON c.id = gc.category_id 
WHERE
    c.id IS NULL AND g.release_date IS NULL;

-- 5
SELECT first_name,last_name,age,salary,happiness_level FROM employees
ORDER BY salary,id;

-- 6
