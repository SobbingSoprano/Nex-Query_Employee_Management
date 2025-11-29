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
import models.Employee;
import models.Person;
import utils.DatabaseConnection;

public class EmployeeDAO {
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
        String query = "SELECT e.empid, e.Fname, e.Lname, e.Email, jt.job_title_id " +
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
                if (jobTitleId >= 900) {
                    return new models.HRAdmin(empId, firstName, lastNameFromDB, email);
                } else {
                    return new Employee(empId, firstName, lastNameFromDB, email);
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
        String query = "SELECT empid, Fname, Lname, Email FROM employees WHERE empid = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Employee(
                    rs.getInt("empid"),
                    rs.getString("Fname"),
                    rs.getString("Lname"),
                    rs.getString("Email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employee: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
