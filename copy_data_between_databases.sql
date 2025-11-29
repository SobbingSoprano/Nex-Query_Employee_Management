-- Script to copy all data from employeeData2 database to Schema database
-- This assumes both databases exist and have matching table structures
-- NOTE: Replace 'Schema' with your actual target database name if different
-- Run this script in your MySQL client or DBeaver
-- Copy employees table
INSERT INTO Schema.employees
SELECT *
FROM employeeData2.employees;
-- Copy division table (if exists)
INSERT INTO Schema.division
SELECT *
FROM employeeData2.division;
-- Copy employee_division table (if exists)
INSERT INTO Schema.employee_division
SELECT *
FROM employeeData2.employee_division;
-- Copy payroll table (if exists)
INSERT INTO Schema.payroll
SELECT *
FROM employeeData2.payroll;
-- Copy job_titles table (if exists)
INSERT INTO Schema.job_titles
SELECT *
FROM employeeData2.job_titles;
-- Copy employee_job_titles table (if exists)
INSERT INTO Schema.employee_job_titles
SELECT *
FROM employeeData2.employee_job_titles;
-- Copy address table (if exists in employeeData2, comment out if not)
-- INSERT INTO Schema.address 
-- SELECT * FROM employeeData2.address;
-- Copy state table (if exists in employeeData2, comment out if not)
-- INSERT INTO Schema.state 
-- SELECT * FROM employeeData2.state;
-- Copy city table (if exists in employeeData2, comment out if not)
-- INSERT INTO Schema.city 
-- SELECT * FROM employeeData2.city;
-- Verify row counts (comment out tables that don't exist in employeeData2)
SELECT 'employees' AS table_name,
    COUNT(*) AS row_count
FROM Schema.employees
UNION ALL
SELECT 'division',
    COUNT(*)
FROM Schema.division
UNION ALL
SELECT 'employee_division',
    COUNT(*)
FROM Schema.employee_division
UNION ALL
SELECT 'payroll',
    COUNT(*)
FROM Schema.payroll
UNION ALL
SELECT 'job_titles',
    COUNT(*)
FROM Schema.job_titles
UNION ALL
SELECT 'employee_job_titles',
    COUNT(*)
FROM Schema.employee_job_titles;
-- UNION ALL
-- SELECT 'address', COUNT(*) FROM Schema.address
-- UNION ALL
-- SELECT 'state', COUNT(*) FROM Schema.state
-- UNION ALL
-- SELECT 'city', COUNT(*) FROM Schema.city;