CREATE SCHEMA universities_db;

-- 1 
CREATE TABLE countries (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE cities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(40) NOT NULL UNIQUE,
    population INT,
    country_id INT NOT NULL,
    CONSTRAINT fk_cities_countries FOREIGN KEY (country_id)
        REFERENCES countries (id)
);

CREATE TABLE universities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL UNIQUE,
    address VARCHAR(80) NOT NULL UNIQUE,
    tuition_fee DECIMAL(19 , 2 ) NOT NULL,
    number_of_staff INT,
    city_id INT,
    CONSTRAINT fk_universities_cities FOREIGN KEY (city_id)
        REFERENCES cities (id)
);

CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(40) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    age INT,
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    is_graduated TINYINT(1) NOT NULL,
    city_id INT,
    CONSTRAINT fk_students_cities FOREIGN KEY (city_id)
        REFERENCES cities (id)
);

CREATE TABLE courses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(40) NOT NULL UNIQUE,
    duration_hours DECIMAL(19 , 2 ),
    start_date DATE,
    teacher_name VARCHAR(60) NOT NULL UNIQUE,
    description TEXT,
    university_id INT,
    CONSTRAINT fk_courses_universities FOREIGN KEY (university_id)
        REFERENCES universities (id)
);

CREATE TABLE students_courses (
    grade DECIMAL(19 , 2 ) NOT NULL,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    CONSTRAINT fk_students_courses_students FOREIGN KEY (student_id)
        REFERENCES students (id),
    CONSTRAINT fk_students_courses_courses FOREIGN KEY (course_id)
        REFERENCES courses (id)
);

-- Set foreign_key_checks = 0
-- 2
INSERT INTO courses (name,duration_hours,start_date,teacher_name,description,university_id)
SELECT
        concat(teacher_name, ' ', 'course'),
        length(name) / 10,
        date_add(start_date, interval 5 DAY),
        REVERSE(teacher_name),
        CONCAT('Course',' ', teacher_name, REVERSE(description)),
        day(start_date)
FROM courses
WHERE id <= 5;

-- 3
UPDATE universities AS u 
SET 
    u.tuition_fee = u.tuition_fee + 300
WHERE
    u.id BETWEEN 5 AND 12;

-- 4
DELETE u FROM universities AS u 
WHERE
    u.number_of_staff IS NULL;

-- 5
SELECT 
    *
FROM
    cities
ORDER BY population DESC;

-- 6
SELECT 
    first_name, last_name, age, phone, email
FROM
    students
WHERE
    age >= 21
ORDER BY first_name DESC , email , id
LIMIT 10;

-- 7
SELECT 
    CONCAT(s.first_name, ' ', s.last_name) AS 'full_name',
    SUBSTRING(s.email, 2, 10) AS 'username',
    REVERSE(s.phone) AS 'password'
FROM
    students AS s
        LEFT JOIN
    students_courses AS sc ON sc.student_id = s.id
        LEFT JOIN
    courses AS c ON c.id = sc.course_id
WHERE
    c.id IS NULL
ORDER BY `password` DESC;
 
 -- 8
SELECT 
    COUNT(*) AS students_count, u.name AS university_name
FROM
    universities AS u
        JOIN
    courses AS c ON u.id = c.university_id
        JOIN
    students_courses AS sc ON c.id = sc.course_id
GROUP BY u.id , u.name
HAVING students_count >= 8
ORDER BY students_count DESC , university_name DESC;

-- 9
SELECT 
    u.name AS university_name,
    c.name AS city_name,
    u.address AS address,
    CASE
        WHEN u.tuition_fee < 800 THEN 'cheap'
        WHEN
            u.tuition_fee >= 800
                AND u.tuition_fee < 1200
        THEN
            'normal'
        WHEN
            u.tuition_fee >= 1200
                AND u.tuition_fee < 2500
        THEN
            'high'
        ELSE 'expensive'
    END AS price_rank,
    u.tuition_fee
FROM
    universities AS u
        JOIN
    cities c ON c.id = u.city_id
ORDER BY tuition_fee ASC;

-- 10 
DELIMITER $$
CREATE FUNCTION udf_average_alumni_grade_by_course_name(course_name VARCHAR(60))
RETURNS DECIMAL(19,2)
DETERMINISTIC
BEGIN
DECLARE average_grade DECIMAL(19,2);
SET average_grade := (
SELECT AVG(sc.grade) FROM students as s
JOIN students_courses as sc on s.id = sc.student_id
JOIN courses as c on sc.course_id = c.id
WHERE course_name = c.name AND s.is_graduated is TRUE
);
RETURN average_grade;
END$$

DELIMITER ;

-- 11
DELIMITER $$
CREATE PROCEDURE udp_graduate_all_students_by_year(year_started INT)
BEGIN
UPDATE students as s
JOIN students_courses as sc on s.id = sc.student_id
JOIN courses as c on sc.course_id = c.id
set s.is_graduated = 1
WHERE year(c.start_date) = year_started;

END$$
DELIMITER ;