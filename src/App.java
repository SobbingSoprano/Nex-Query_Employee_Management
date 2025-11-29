/**
 * Main Application Entry Point
 * This is the main driver class that initializes the NexQuery Employee Management System.
 * It coordinates the login/authorization workflow and delegates to appropriate UI components.
 * 
 * System Architecture:
 * - models/Person.java (Abstract class) - Base class for all person types
 * - models/Employee.java (Inheritance) - Extends Person, implements DataAccessible
 * - models/HRAdmin.java (Inheritance) - Extends Person, implements DataAccessible
 * - interfaces/Authenticatable.java - Defines authentication contract
 * - interfaces/DataAccessible.java - Defines CRUD permissions
 * - utils/DatabaseConnection.java (Singleton) - Manages database connection
 * - dao/EmployeeDAO.java - Data access layer for employee operations
 * - services/AuthenticationService.java - Business logic for authentication
 * - ui/ConsoleUI.java - Terminal interface for user interaction
 * - ui/HRAdminView.java (Aggregation) - Specialized view for HR Admin users
 */

import models.Person;
import services.AuthenticationService;
import ui.ConsoleUI;
import ui.HRAdminView;
import utils.DatabaseConnection;

public class App {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        AuthenticationService authService = new AuthenticationService();
        HRAdminView adminView = new HRAdminView(ui);
        
        ui.displayWelcome();
        
        // Login Loop
        boolean loggedIn = false;
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        while (!loggedIn && attempts < MAX_ATTEMPTS) {
            int empId = ui.getEmployeeId();
            String lastName = ui.getLastName();
            String hireDate = ui.getHireDate();
            String ssn = ui.getSSN();

            if (authService.authenticate(empId, lastName, hireDate, ssn)) {
                Person currentUser = authService.getCurrentUser();
                ui.displayLoginSuccess(currentUser.getFullName());

                if ("HR_ADMIN".equals(currentUser.getRole())) {
                    System.out.println("Admin View: Full Access");
                } else {
                    System.out.println("Employee View: Read Only Access");
                    System.out.println("NOTE: Only Administrators and Yourself can view your information.\n");
                }

                loggedIn = true;

                // Main application loop (placeholder for future functionality)
                boolean running = true;
                while (running) {
                    boolean isAdmin = "HR_ADMIN".equals(currentUser.getRole());
                    int choice = ui.displayMainMenu(isAdmin);
                    switch (choice) {
                        case 0:
                            System.out.println("Logging out...");
                            authService.logout();
                            running = false;
                            break;
                        case 1:
                            System.out.println("\nViewing your information...");
                            System.out.println("Employee ID: " + currentUser.getEmpId());
                            System.out.println("Name: " + currentUser.getFullName());
                            System.out.println("Email: " + currentUser.getEmail());
                            System.out.println();
                            break;
                        default:
                            if (isAdmin) {
                                System.out.println("\n[Feature not yet implemented]\n");
                            } else {
                                ui.displayAccessDenied();
                            }
                    }
                }
            } else {
                ui.displayLoginFailure();
                attempts++;
            }
        }
        
        if (attempts >= MAX_ATTEMPTS) {
            System.out.println("Maximum login attempts exceeded. Exiting...");
        }
        
        // Cleanup
        ui.close();
        DatabaseConnection.getInstance().closeConnection();
        System.out.println("Thank you for using NexQuery!");
    }
}
