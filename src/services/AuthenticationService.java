/**
 * Authentication Service - Business logic layer for authentication operations.
 * This service class implements the Authenticatable interface and coordinates
 * authentication operations between the UI and DAO layers.
 * Demonstrates the Service layer pattern for business logic separation.
 */
package services;

import dao.EmployeeDAO;
import interfaces.Authenticatable;
import models.Person;

public class AuthenticationService implements Authenticatable {
    private EmployeeDAO employeeDAO;
    private Person currentUser;
    
    public AuthenticationService() {
        this.employeeDAO = new EmployeeDAO();
        this.currentUser = null;
    }
    
    @Override
    public boolean authenticate(int empId, String lastName, String dob, String ssn) {
        Person user = employeeDAO.authenticate(empId, lastName, dob, ssn);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean hasPermission(String operation) {
        if (currentUser == null) {
            return false;
        }
        
        // Check if user is HR Admin for full permissions
        return "HR_ADMIN".equals(currentUser.getRole());
    }
    
    public Person getCurrentUser() {
        return currentUser;
    }
    
    public void logout() {
        this.currentUser = null;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
