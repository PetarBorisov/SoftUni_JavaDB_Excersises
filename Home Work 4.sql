SELECT 
    COUNT(*) AS `count`
FROM
    `wizzard_deposits`;
    
    -- 2
SELECT 
    MAX(magic_wand_size) as `longest_magic_wand`
FROM
    wizzard_deposits;
    
    -- 3
    SELECT 
    deposit_group, MAX(magic_wand_size) AS longest_magic_wand
FROM
    wizzard_deposits
GROUP BY deposit_group
ORDER BY longest_magic_wand ASC , deposit_group ASC;

-- 4
SELECT 
    deposit_group
FROM
    wizzard_deposits
GROUP BY deposit_group
ORDER BY AVG(magic_wand_size) ASC
LIMIT 1;

-- 5 
SELECT 
    `deposit_group`, SUM(`deposit_amount`) AS `total_sum`
FROM
    `wizzard_deposits`
GROUP BY `deposit_group`
ORDER BY `total_sum` ASC;

-- 6

SELECT 
    `deposit_group`, SUM(`deposit_amount`) AS 'total_sum'
FROM
    `wizzard_deposits`
WHERE
    `magic_wand_creator` = 'Ollivander family'
GROUP BY `deposit_group`
ORDER BY `deposit_group` ASC;

-- 7 
SELECT 
    `deposit_group`, SUM(`deposit_amount`) AS 'total_sum'
FROM
    `wizzard_deposits`
WHERE
    `magic_wand_creator` = 'Ollivander family'
GROUP BY `deposit_group`
HAVING `total_sum` < 150000
ORDER BY `total_sum` DESC;

-- 8 
SELECT 
    deposit_group,
    magic_wand_creator,
    MIN(deposit_charge) AS 'min_deposit_charge'
FROM
    wizzard_deposits
GROUP BY deposit_group , magic_wand_creator
ORDER BY magic_wand_creator , deposit_group;