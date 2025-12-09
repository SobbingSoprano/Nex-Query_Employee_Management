/**
 * Authentication Service - Business logic layer for authentication operations.
 * This service class implements the Authenticatable interface and coordinates
 * authentication operations between the UI and DAO layers.
 * Demonstrates the Service layer pattern for business logic separation.
 */
package services;

import dao.EmployeeDAO;
import interfaces.Authenticatable;
import java.util.List;
import models.Employee;
import models.Person;

public class AuthenticationService implements Authenticatable {
                        public boolean authenticateByPassword(int empId, String password) {
                            Person user = employeeDAO.authenticateByPassword(empId, password);
                            if (user != null) {
                                this.currentUser = user;
                                return true;
                            }
                            return false;
                        }

                        public boolean resetPassword(int empId, String newPassword) {
                            return employeeDAO.resetPassword(empId, newPassword);
                        }
                    public List<String> getAllJobTitles() {
                        return employeeDAO.getAllJobTitles();
                    }
                public List<models.Employee> getEmployeesByJobTitle(String jobTitle) {
                    return employeeDAO.getEmployeesByJobTitle(jobTitle);
                }
            public java.util.List<models.HRAdmin> getAllHRAdmins() {
                return employeeDAO.getAllHRAdmins();
            }
        // Payroll summary methods
        public List<String[]> getPayrollByJobTitle() {
            return employeeDAO.getPayrollByJobTitle();
        }

        public List<String[]> getPayrollByDivision() {
            return employeeDAO.getPayrollByDivision();
        }

        public List<String[]> getEmployeePayHistory(int empId) {
            return employeeDAO.getEmployeePayHistory(empId);
        }

        public List<Employee> getEmployeesHiredInRange(String startDate, String endDate) {
            return employeeDAO.getEmployeesHiredInRange(startDate, endDate);
        }
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
            // If user is an employee, fetch full record with salary/occupation
            if (user instanceof models.Employee) {
                models.Employee fullEmp = employeeDAO.getEmployeeById(empId);
                if (fullEmp != null) {
                    this.currentUser = fullEmp;
                } else {
                    this.currentUser = user;
                }
            } else {
                this.currentUser = user;
            }
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

    // CRUD methods for admin
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }
    public boolean addEmployee(Employee emp, String hireDate, String ssn, double salary) {
        return employeeDAO.addEmployee(emp, hireDate, ssn, salary);
    }
    public boolean updateEmployee(Employee emp) {
        return employeeDAO.updateEmployee(emp);
    }
    public boolean deleteEmployee(int empId) {
        return employeeDAO.deleteEmployee(empId);
    }
}
