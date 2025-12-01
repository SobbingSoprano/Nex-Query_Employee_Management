/**
 * Employee class representing a regular employee user.
 * This demonstrates inheritance (extends Person) and interface implementation.
 * Employees have read-only access to their own data.
 */
package models;

import interfaces.DataAccessible;

public class Employee extends Person implements DataAccessible {
    private String role;
    private double salary;
    private String hireDate;
    private String ssn;
    private String occupation;

    // Full constructor
    public Employee(int empId, String firstName, String lastName, String email, double salary, String hireDate, String ssn, String occupation) {
        super(empId, firstName, lastName, email);
        this.role = "EMPLOYEE";
        this.salary = salary;
        this.hireDate = hireDate;
        this.ssn = ssn;
        this.occupation = occupation;
    }

    // Minimal constructor for backward compatibility
    public Employee(int empId, String firstName, String lastName, String email) {
        this(empId, firstName, lastName, email, 0.0, null, null, null);
    }
    

    @Override
    public String getRole() {
        return role;
    }

    public double getSalary() {
        return salary;
    }

    public String getHireDate() {
        return hireDate;
    }

    public String getSSN() {
        return ssn;
    }

    public String getOccupation() {
        return occupation;
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
