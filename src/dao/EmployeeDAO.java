/**
 * Data Access Object (DAO) for Employee operations.
 * This class handles all database interactions related to employee authentication and data retrieval.
 * Demonstrates the DAO design pattern for separation of data access logic.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Employee;
import models.Person;
import utils.DatabaseConnection;

public class EmployeeDAO {
        // Payroll summary: total payroll by job title
        public List<String[]> getPayrollByJobTitle() {
            List<String[]> result = new ArrayList<>();
            String query = "SELECT jt.job_title, SUM(e.Salary) AS total_payroll " +
                "FROM employees e " +
                "JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                "GROUP BY jt.job_title";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    result.add(new String[] { rs.getString("job_title"), String.valueOf(rs.getDouble("total_payroll")) });
                }
            } catch (SQLException e) {
                System.err.println("Error generating payroll by job title: " + e.getMessage());
            }
            return result;
        }

        // Payroll summary: total payroll by division
        public List<String[]> getPayrollByDivision() {
            List<String[]> result = new ArrayList<>();
            String query = "SELECT d.Name, SUM(e.Salary) AS total_payroll " +
                "FROM employees e " +
                "JOIN employee_division ed ON e.empid = ed.empid " +
                "JOIN division d ON ed.div_ID = d.ID " +
                "GROUP BY d.Name";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    result.add(new String[] { rs.getString("Name"), String.valueOf(rs.getDouble("total_payroll")) });
                }
            } catch (SQLException e) {
                System.err.println("Error generating payroll by division: " + e.getMessage());
            }
            return result;
        }

        // Employee pay history (assumes payroll table with empid, pay_date, salary)
        public List<String[]> getEmployeePayHistory(int empId) {
            List<String[]> result = new ArrayList<>();
            String query = "SELECT pay_date, Earnings FROM payroll WHERE empid = ? ORDER BY pay_date DESC";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, empId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    result.add(new String[] { rs.getString("pay_date"), String.valueOf(rs.getDouble("Earnings")) });
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving pay history: " + e.getMessage());
            }
            return result;
        }

        // Employees hired in a date range
        public List<Employee> getEmployeesHiredInRange(String startDate, String endDate) {
            List<Employee> employees = new ArrayList<>();
            String query = "SELECT e.empid, e.Fname, e.Lname, e.Email, e.Salary, e.HireDate, e.SSN, jt.job_title " +
                "FROM employees e " +
                "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                "WHERE e.HireDate BETWEEN ? AND ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, startDate);
                stmt.setString(2, endDate);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    employees.add(new Employee(
                        rs.getInt("empid"),
                        rs.getString("Fname"),
                        rs.getString("Lname"),
                        rs.getString("Email"),
                        rs.getDouble("Salary"),
                        rs.getString("HireDate"),
                        rs.getString("SSN"),
                        rs.getString("job_title")
                    ));
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving employees hired in range: " + e.getMessage());
            }
            return employees;
        }
    private Connection connection;
    
    public EmployeeDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Authenticates user by empId, lastName, HireDate, and SSN, and determines role via job_title_id
     * @param empId Employee ID
     * @param lastName Last Name
     * @param hireDate Hire Date (format as in DB)
     * @param ssn Social Security Number (format as in DB)
     * @return Employee or HRAdmin object if authenticated, null otherwise
     */
    public Person authenticate(int empId, String lastName, String hireDate, String ssn) {
        String query = "SELECT e.empid, e.Fname, e.Lname, e.Email, e.Salary, e.HireDate, e.SSN, jt.job_title_id, jt.job_title " +
            "FROM employees e " +
            "JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
            "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
            "WHERE e.empid = ? AND e.Lname = ? AND e.HireDate = ? AND e.SSN = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, empId);
            stmt.setString(2, lastName);
            stmt.setString(3, hireDate);
            stmt.setString(4, ssn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int jobTitleId = rs.getInt("job_title_id");
                String firstName = rs.getString("Fname");
                String lastNameFromDB = rs.getString("Lname");
                String email = rs.getString("Email");
                double salary = 0.0;
                String occupation = null;
                String hireDateDb = null;
                String ssnDb = null;
                try { salary = rs.getDouble("Salary"); } catch (Exception ignore) {}
                try { occupation = rs.getString("job_title"); } catch (Exception ignore) {}
                try { hireDateDb = rs.getString("HireDate"); } catch (Exception ignore) {}
                try { ssnDb = rs.getString("SSN"); } catch (Exception ignore) {}
                if (jobTitleId >= 900) {
                    return new models.HRAdmin(empId, firstName, lastNameFromDB, email, salary, occupation);
                } else {
                    return new Employee(empId, firstName, lastNameFromDB, email, salary, hireDateDb, ssnDb, occupation);
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Authentication failed
    }
    
    /**
     * Retrieves employee data for a specific employee ID
     * @param empId Employee ID
     * @return Employee object if found, null otherwise
     */
    public Employee getEmployeeById(int empId) {
        String query = "SELECT e.empid, e.Fname, e.Lname, e.Email, e.Salary, e.HireDate, e.SSN, jt.job_title " +
            "FROM employees e " +
            "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
            "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
            "WHERE e.empid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(
                    rs.getInt("empid"),
                    rs.getString("Fname"),
                    rs.getString("Lname"),
                    rs.getString("Email"),
                    rs.getDouble("Salary"),
                    rs.getString("HireDate"),
                    rs.getString("SSN"),
                    rs.getString("job_title")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employee: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT e.empid, e.Fname, e.Lname, e.Email, e.Salary, e.HireDate, e.SSN, jt.job_title " +
            "FROM employees e " +
            "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
            "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employees.add(new Employee(
                    rs.getInt("empid"),
                    rs.getString("Fname"),
                    rs.getString("Lname"),
                    rs.getString("Email"),
                    rs.getDouble("Salary"),
                    rs.getString("HireDate"),
                    rs.getString("SSN"),
                    rs.getString("job_title")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employees: " + e.getMessage());
        }
        return employees;
    }

    // Add a new employee
    public boolean addEmployee(Employee emp, String hireDate, String ssn, double salary) {
        String empInsert = "INSERT INTO employees (empid, Fname, Lname, Email, HireDate, SSN, Salary) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String occupation = emp.getOccupation();
        if (occupation == null || occupation.trim().isEmpty()) {
            System.err.println("Occupation is required for new employee.");
            return false;
        }
        try {
            // 1. Insert employee
            try (PreparedStatement stmt = connection.prepareStatement(empInsert)) {
                stmt.setInt(1, emp.getEmpId());
                stmt.setString(2, emp.getFirstName());
                stmt.setString(3, emp.getLastName());
                stmt.setString(4, emp.getEmail());
                stmt.setString(5, hireDate);
                stmt.setString(6, ssn);
                stmt.setDouble(7, salary);
                stmt.executeUpdate();
            }

            // 2. Check if occupation exists in job_titles
            Integer jobTitleId = null;
            String selectJobTitle = "SELECT job_title_id FROM job_titles WHERE job_title = ?";
            try (PreparedStatement stmt = connection.prepareStatement(selectJobTitle)) {
                stmt.setString(1, occupation);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    jobTitleId = rs.getInt("job_title_id");
                }
            }

            // 3. If not, insert new job_title with id 300-800
            if (jobTitleId == null) {
                // Find unused job_title_id in 300-800
                int newId = 300;
                String findUsed = "SELECT job_title_id FROM job_titles WHERE job_title_id BETWEEN 300 AND 800 ORDER BY job_title_id";
                List<Integer> usedIds = new ArrayList<>();
                try (PreparedStatement stmt = connection.prepareStatement(findUsed)) {
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        usedIds.add(rs.getInt("job_title_id"));
                    }
                }
                while (usedIds.contains(newId) && newId <= 800) newId++;
                if (newId > 800) {
                    System.err.println("No available job_title_id in range 300-800.");
                    return false;
                }
                String insertJobTitle = "INSERT INTO job_titles (job_title_id, job_title) VALUES (?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(insertJobTitle)) {
                    stmt.setInt(1, newId);
                    stmt.setString(2, occupation);
                    stmt.executeUpdate();
                }
                jobTitleId = newId;
            }

            // 4. Insert into employee_job_titles
            String insertEmpJob = "INSERT INTO employee_job_titles (empid, job_title_id) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertEmpJob)) {
                stmt.setInt(1, emp.getEmpId());
                stmt.setInt(2, jobTitleId);
                stmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    // Update an employee
    public boolean updateEmployee(Employee emp) {
        String query = "UPDATE employees SET Fname = ?, Lname = ?, Email = ?, Salary = ? WHERE empid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, emp.getFirstName());
            stmt.setString(2, emp.getLastName());
            stmt.setString(3, emp.getEmail());
            stmt.setDouble(4, emp.getSalary());
            stmt.setInt(5, emp.getEmpId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }

    // Delete an employee
    public boolean deleteEmployee(int empId) {
        String query = "DELETE FROM employees WHERE empid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, empId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }
}
