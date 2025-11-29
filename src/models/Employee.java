/**
 * Employee class representing a regular employee user.
 * This demonstrates inheritance (extends Person) and interface implementation.
 * Employees have read-only access to their own data.
 */
package models;

import interfaces.DataAccessible;

public class Employee extends Person implements DataAccessible {
    private String role;
    
    public Employee(int empId, String firstName, String lastName, String email) {
        super(empId, firstName, lastName, email);
        this.role = "EMPLOYEE";
    }
    
    @Override
    public String getRole() {
        return role;
    }
    
    // Employees can only read their own data
    @Override
    public boolean canCreate() {
        return false;
    }
    
    @Override
    public boolean canRead() {
        return true;
    }
    
    @Override
    public boolean canUpdate() {
        return false;
    }
    
    @Override
    public boolean canDelete() {
        return false;
    }
}
