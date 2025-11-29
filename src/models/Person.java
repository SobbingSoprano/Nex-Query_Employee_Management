/**
 * Abstract base class representing a person in the system.
 * This demonstrates abstraction and serves as the parent class for all person types.
 * Contains common attributes shared by all persons (employees, admins, etc.)
 */
package models;

public abstract class Person {
    protected int empId;
    protected String firstName;
    protected String lastName;
    protected String email;
    
    public Person(int empId, String firstName, String lastName, String email) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    // Abstract method to be implemented by subclasses
    public abstract String getRole();
    
    // Getters
    public int getEmpId() {
        return empId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
