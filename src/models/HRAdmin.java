/**
 * HRAdmin class representing an HR administrator user.
 * This demonstrates inheritance (extends Person) and interface implementation.
 * HR Admins have full CRUD privileges on all employee data.
 */
package models;

import interfaces.DataAccessible;

public class HRAdmin extends Person implements DataAccessible {
    private String role;
    private double salary;
    private String occupation;

    public HRAdmin(int empId, String firstName, String lastName, String email, double salary, String occupation) {
        super(empId, firstName, lastName, email);
        this.role = "HR_ADMIN";
        this.salary = salary;
        this.occupation = occupation;
    }

    // For backward compatibility
    public HRAdmin(int empId, String firstName, String lastName, String email) {
        this(empId, firstName, lastName, email, 0.0, null);
    }

    public double getSalary() {
        return salary;
    }

    public String getOccupation() {
        return occupation;
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
