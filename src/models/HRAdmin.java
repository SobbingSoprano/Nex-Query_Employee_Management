/**
 * HRAdmin class representing an HR administrator user.
 * This demonstrates inheritance (extends Person) and interface implementation.
 * HR Admins have full CRUD privileges on all employee data.
 */
package models;

import interfaces.DataAccessible;

public class HRAdmin extends Person implements DataAccessible {
    private String role;
    
    public HRAdmin(int empId, String firstName, String lastName, String email) {
        super(empId, firstName, lastName, email);
        this.role = "HR_ADMIN";
    }
    
    @Override
    public String getRole() {
        return role;
    }
    
    // HR Admins have full CRUD access
    @Override
    public boolean canCreate() {
        return true;
    }
    
    @Override
    public boolean canRead() {
        return true;
    }
    
    @Override
    public boolean canUpdate() {
        return true;
    }
    
    @Override
    public boolean canDelete() {
        return true;
    }
}
