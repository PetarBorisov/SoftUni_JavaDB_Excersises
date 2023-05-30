SELECT 
    e.employee_id, e.job_title, a.address_id, a.address_text
FROM
    employees AS e
        JOIN
    addresses AS a ON e.address_id = a.address_id
ORDER BY a.address_id
LIMIT 5;

-- 2
SELECT 
    e.first_name, e.last_name, t.name as town, a.address_text
FROM
    employees AS e
        JOIN
    addresses AS a ON e.address_id = a.address_id
        JOIN
    towns AS t ON a.town_id = t.town_id
ORDER BY e.first_name , e.last_name
LIMIT 5;

-- 3
SELECT 
    employee_id, first_name, last_name, d.name
FROM
    employees AS e
        JOIN
    departments AS d ON e.department_id = d.department_id
WHERE
    d.name = 'Sales'
ORDER BY e.employee_id DESC
LIMIT 5;

-- 4
SELECT 
    e.employee_id,
    e.first_name,
    e.salary,
    d.name AS department_name
FROM
    employees AS e
        JOIN
    departments AS d ON e.department_id = d.department_id
WHERE
    e.salary > 15000
ORDER BY d.department_id DESC
LIMIT 5;

-- 5
SELECT 
    e.employee_id, e.first_name
FROM
    employees AS e
        LEFT JOIN
    employees_projects AS ep ON e.employee_id = ep.employee_id
WHERE
    ep.project_id IS NULL
ORDER BY e.employee_id DESC
LIMIT 3;

-- 6
SELECT 
    e.first_name, e.last_name, e.hire_date, d.name AS dept_name
FROM
    employees AS e
        JOIN
    departments AS d ON e.department_id = d.department_id
WHERE
    e.hire_date > '1999-01-01'
        AND d.name IN ('SALES' , 'Finance')
ORDER BY e.hire_date;

-- 7
SELECT 
    e.employee_id, e.first_name, p.name AS project_name
FROM
    employees AS e
        JOIN
    employees_projects AS ep ON e.employee_id = ep.employee_id
        JOIN
    projects AS p ON ep.project_id = p.project_id
WHERE
    DATE(p.start_date) > '2002-08-13'
        AND p.end_date IS NULL
ORDER BY e.first_name , p.name
LIMIT 5;

-- 8
SELECT 
    e.employee_id,
    e.first_name,
    IF(YEAR(p.start_date) >= 2005,
        NULL,
        p.name) AS project_name
FROM
    employees AS e
        JOIN
    employees_projects AS ep ON e.employee_id = ep.employee_id
        JOIN
    projects AS p ON p.project_id = ep.project_id
WHERE
    e.employee_id = 24
ORDER BY p.name;

-- 9
SELECT 
    e.employee_id, e.first_name, e.manager_id, m.first_name
FROM
    employees AS e
        JOIN
    employees AS m ON e.manager_id = m.employee_id
WHERE
    e.manager_id IN (3 , 7)
ORDER BY e.first_name
LIMIT 5;


-- 10
SELECT e.employee_id,
concat_ws(' ',e.first_name,e.last_name)as employee_name,
concat_ws(' ',m.first_name,m.last_name) as manager_name,
d.name
 from employees as e
JOIN employees as m on e.manager_id = m.employee_id
JOIN departments as d on  e.department_id = d.department_id
ORDER BY e.employee_id
LIMIT 5;

-- 11
SELECT 
    AVG(e.salary) AS avg_salary
FROM
    employees AS e
GROUP BY e.department_id
ORDER BY avg_salary
LIMIT 1;

-- 12
USE geography;


SELECT 
    c.country_code, m.mountain_range, p.peak_name, p.elevation
FROM
    countries AS c
        JOIN
    mountains_countries AS mc ON c.country_code = mc.country_code
        JOIN
    mountains AS m ON m.id = mc.mountain_id
        JOIN
    peaks AS p ON p.mountain_id = m.id
WHERE
    c.country_name = 'Bulgaria'
        AND p.elevation > 2835
ORDER BY p.elevation DESC;

-- 13
SELECT 
    c.country_code, COUNT(mc.mountain_id) AS 'mountain_range'
FROM
    `countries` AS c
        JOIN
    `mountains_countries` AS mc ON c.country_code = mc.country_code
WHERE
    c.country_name IN ('United States' , 'Russia', 'Bulgaria')
GROUP BY c.country_code
ORDER BY `mountain_range` DESC;
