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
SELECT t.name as 'team_name',a.name as 'address_name',length(a.name) as 'count_of_characters' FROM teams as t
JOIN offices as o ON o.id = t.office_id
JOIN addresses as a ON  a.id = o.address_id 
WHERE o.website is NOT NULL
ORDER BY `team_name`,`address_name`;

-- 7
SELECT c.name,COUNT(g.id) as 'games_count',round(avg(g.budget),2) as 'avg_budget',MAX(g.rating) as 'max_rating' FROM games as g
JOIN games_categories as gc ON gc.game_id = g.id
JOIN categories as c ON c.id = gc.category_id 
GROUP BY c.id
HAVING `max_rating` >= 9.5
ORDER BY `games_count`DESC,c.name;

-- 8
SELECT g.name,g.release_date,CONCAT(substring(g.`description`,1,10),'...') as 'summary',
CASE
    WHEN month(g.release_date) BETWEEN 1 and 3 then 'Q1' 
    WHEN month(g.release_date) BETWEEN 4 and 6 then 'Q2' 
    WHEN month(g.release_date) BETWEEN 7 and 9 then 'Q3'
    WHEN month(g.release_date) BETWEEN 10 and 12 then 'Q4'
    END as 'quarter',
    t.name as 'team_name'
 FROM games as g
 JOIN teams as t on t.id = g.team_id
 WHERE g.name LIKE '%2' AND month(g.release_date) % 2 = 0 AND year(g.release_date) = '2022'
 ORDER BY `quarter`;

-- 9
SELECT 
	g.`name`, 
    CASE
    WHEN g.budget < 50000 then 'Normal budget'
    ELSE 'Insufficient budget'
    END
    AS 'budget_level',
    t.`name` as 'team_name', 
    a.`name` as 'address_name '
FROM games as g
JOIN teams as t on t.id = g.team_id
JOIN offices as o on o.id = t.office_id
JOIN addresses as a on a.id=o.address_id
left JOIN games_categories as gc on gc.game_id = g.id
WHERE g.release_date is Null and gc.category_id is NULL
ORDER BY g.`name`;

-- 10
DELIMITER $$
CREATE FUNCTION udf_game_info_by_name (game_name VARCHAR (20)) 
RETURNS TEXT
DETERMINISTIC
BEGIN
	DECLARE info VARCHAR (255);
	DECLARE team_name VARCHAR (40);
	DECLARE address_text VARCHAR (50);
    
    SET team_name := (SELECT t.`name`
        	FROM teams AS t 
        	JOIN games AS g 
        	ON g.team_id = t.id 
        	WHERE g.`name` = game_name);
            
	SET address_text := (SELECT a.`name`
        	FROM addresses AS a
        	JOIN offices AS o
        	ON a.id = o.address_id
        	JOIN teams AS t
        	ON o.id = t.office_id
        	WHERE t.`name` = team_name);
    
  	SET info := concat_ws(' ', 'The', game_name, 'is developed by a', team_name, 'in an office with an address', address_text);
  	RETURN info;

END $$


-- 11
CREATE PROCEDURE udp_update_budget (min_game_rating FLOAT)
BEGIN
	UPDATE games AS g
	LEFT JOIN games_categories AS c
    	ON g.id = c.game_id
    	SET g.budget = g.budget + 100000, 
		g.release_date = adddate(g.release_date, INTERVAL 1 YEAR)
	WHERE c.category_id IS NULL 
		AND g.release_date IS NOT NULL 
		AND g.rating > min_game_rating;
END