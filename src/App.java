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

import java.util.ArrayList;
import java.util.List;
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
                    adminView.displayAdminDashboard();
                    adminView.displayAdminWelcome(currentUser.getFirstName());
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
                        case 8:
                            if (isAdmin) {
                                System.out.println("\nPayroll Summary Generator:");
                                // Prompt for summary type
                                System.out.println("Select summary type:");
                                System.out.println("1. Total payroll by job title");
                                System.out.println("2. Total payroll by division");
                                System.out.println("3. Employee pay history");
                                System.out.println("4. Employees hired in date range");
                                System.out.print("Enter option (1-4, or 'q' to cancel): ");
                                String summaryType = ui.getScanner().nextLine();
                                if ("q".equalsIgnoreCase(summaryType)) break;
                                switch (summaryType) {
                                    case "1":
                                        // Total payroll by job title
                                        List<String[]> jobTitleSummary = authService.getPayrollByJobTitle();
                                        System.out.println("\nPayroll by Job Title:");
                                        System.out.printf("%-25s %-15s\n", "Job Title", "Total Payroll");
                                        for (String[] row : jobTitleSummary) {
                                            System.out.printf("%-25s $%,.2f\n", row[0], Double.parseDouble(row[1]));
                                        }
                                        System.out.println();
                                        break;
                                    case "2":
                                        // Total payroll by division
                                        List<String[]> divisionSummary = authService.getPayrollByDivision();
                                        System.out.println("\nPayroll by Division:");
                                        System.out.printf("%-25s %-15s\n", "Division", "Total Payroll");
                                        for (String[] row : divisionSummary) {
                                            System.out.printf("%-25s $%,.2f\n", row[0], Double.parseDouble(row[1]));
                                        }
                                        System.out.println();
                                        break;
                                    case "3":
                                        // Employee pay history by ID, display name
                                        System.out.print("Enter Employee ID: ");
                                        String empIdInput = ui.getScanner().nextLine();
                                        int empIdHist = -1;
                                        try { empIdHist = Integer.parseInt(empIdInput); } catch (NumberFormatException e) { System.out.println("Invalid ID."); break; }
                                        models.Employee foundEmp = null;
                                        for (models.Employee emp : authService.getAllEmployees()) {
                                            if (emp.getEmpId() == empIdHist) {
                                                foundEmp = emp;
                                                break;
                                            }
                                        }
                                        if (foundEmp == null) {
                                            System.out.println("Employee not found.\n");
                                            break;
                                        }
                                        List<String[]> payHistory = authService.getEmployeePayHistory(empIdHist);
                                        System.out.println("\nPay History for " + foundEmp.getFullName() + ":");
                                        System.out.printf("%-15s %-15s\n", "Date", "Salary");
                                        for (String[] row : payHistory) {
                                            System.out.printf("%-15s $%,.2f\n", row[0], Double.parseDouble(row[1]));
                                        }
                                        System.out.println();
                                        break;
                                    case "4":
                                        // Employees hired in date range
                                        System.out.print("Enter start date (YYYY-MM-DD): ");
                                        String startDate = ui.getScanner().nextLine();
                                        System.out.print("Enter end date (YYYY-MM-DD): ");
                                        String endDate = ui.getScanner().nextLine();
                                        List<models.Employee> hiredRange = authService.getEmployeesHiredInRange(startDate, endDate);
                                        System.out.println("\nEmployees hired between " + startDate + " and " + endDate + ":");
                                        System.out.printf("%-10s %-20s %-15s\n", "ID", "Name", "Hire Date");
                                        for (models.Employee emp : hiredRange) {
                                            System.out.printf("%-10d %-20s %-15s\n", emp.getEmpId(), emp.getFullName(), emp.getHireDate());
                                        }
                                        System.out.println();
                                        break;
                                    default:
                                        System.out.println("Invalid summary type. Returning to menu.\n");
                                }
                            } else {
                                ui.displayAccessDenied();
                            }
                            break;
                                                case 7:
                                                    if (isAdmin) {
                                                        System.out.println("\nAdjust Range of Salaries:");
                                                        System.out.print("Enter a salary to adjust (e.g., 50000- enter 'q' at any prompt to cancel- WARNING: This will ERASE your progress): ");
                                                        String baseSalaryInput = ui.getScanner().nextLine();
                                                        double baseSalary;
                                                        try {
                                                            baseSalary = Double.parseDouble(baseSalaryInput.replace(",", ""));
                                                        } catch (NumberFormatException e) {
                                                            System.out.println("\nInvalid salary input. Returning to menu.\n");
                                                            break;
                                                        }
                                                        System.out.print("Enter percentage for salary range (e.g., 5 for +5%/-5% or 'q' to cancel): ");
                                                        String percentInput = ui.getScanner().nextLine();
                                                        if ("q".equalsIgnoreCase(percentInput)) {
                                                            System.out.println("\nReturning to menu.\n");
                                                            break;
                                                        }
                                                        double percent;
                                                        try {
                                                            percent = Double.parseDouble(percentInput);
                                                        } catch (NumberFormatException e) {
                                                            System.out.println("\nInvalid percentage input. Returning to menu.\n");
                                                            break;
                                                        }
                                                        // Salary range calculation and confirmation loop
                                                        while (true) {
                                                            double lowerBound = baseSalary * (1 - Math.abs(percent) / 100.0);
                                                            double upperBound = baseSalary * (1 + Math.abs(percent) / 100.0);
                                                            System.out.printf("\nSalary range to be adjusted: $%,.2f - $%,.2f\n", lowerBound, upperBound);
                                                            System.out.print("Is this the salary range you wish to proceed with? (y/n): ");
                                                            String rangeConfirm = ui.getScanner().nextLine();
                                                            if ("n".equalsIgnoreCase(rangeConfirm)) {
                                                                System.out.print("Re-enter percentage for salary range (e.g., 5 for +5%/-5% or 'q' to cancel): ");
                                                                String newPercentInput = ui.getScanner().nextLine();
                                                                if ("q".equalsIgnoreCase(newPercentInput)) {
                                                                    System.out.println("Returning to menu.\n");
                                                                    break;
                                                                }
                                                                try {
                                                                    percent = Double.parseDouble(newPercentInput);
                                                                } catch (NumberFormatException e) {
                                                                    System.out.println("Invalid percentage input. Returning to menu.\n");
                                                                    break;
                                                                }
                                                                continue;
                                                            } else if (!"y".equalsIgnoreCase(rangeConfirm)) {
                                                                System.out.println("Returning to menu.\n");
                                                                break;
                                                            }
                                                            // Filter employees in range
                                                            List<models.Employee> inRange = new ArrayList<>();
                                                            for (models.Employee emp : authService.getAllEmployees()) {
                                                                double sal = emp.getSalary();
                                                                if (sal >= lowerBound && sal <= upperBound) {
                                                                    inRange.add(emp);
                                                                }
                                                            }
                                                            if (inRange.isEmpty()) {
                                                                System.out.println("No employees found in the selected salary range. Returning to menu.\n");
                                                                break;
                                                            }
                                                            // Prompt for adjustment percentage
                                                            while (true) {
                                                                System.out.print("Enter percentage to increase salaries by (e.g., 5 for 5% or 'q' to cancel): ");
                                                                String adjInput = ui.getScanner().nextLine();
                                                                if ("q".equalsIgnoreCase(adjInput)) {
                                                                    System.out.println("Returning to menu.\n");
                                                                    break;
                                                                }
                                                                double adjPercent;
                                                                try {
                                                                    adjPercent = Double.parseDouble(adjInput);
                                                                } catch (NumberFormatException e) {
                                                                    System.out.println("Invalid percentage input. Try again.\n");
                                                                    continue;
                                                                }
                                                                // Show preview
                                                                System.out.println("\nEmployees to be adjusted:");
                                                                for (models.Employee emp : inRange) {
                                                                    double newSal = emp.getSalary() * (1 + adjPercent / 100.0);
                                                                    System.out.printf("\nID: %d, Name: %s, Old Salary: $%,.2f, New Salary: $%,.2f\n",
                                                                        emp.getEmpId(), emp.getFullName(), emp.getSalary(), newSal);
                                                                }
                                                                System.out.print("\nWould you like to confirm this salary adjustment? (y/n): ");
                                                                String confirmAdj = ui.getScanner().nextLine();
                                                                if ("y".equalsIgnoreCase(confirmAdj)) {
                                                                    // Update salaries
                                                                    int updated = 0;
                                                                    for (models.Employee emp : inRange) {
                                                                        double newSal = emp.getSalary() * (1 + adjPercent / 100.0);
                                                                        models.Employee updatedEmp = new models.Employee(
                                                                            emp.getEmpId(), emp.getFirstName(), emp.getLastName(), emp.getEmail(),
                                                                            newSal, emp.getHireDate(), emp.getSSN(), emp.getOccupation());
                                                                        if (authService.updateEmployee(updatedEmp)) {
                                                                            updated++;
                                                                        }
                                                                    }
                                                                    System.out.printf("\n%d employee(s) had their salaries updated successfully.\n\n", updated);
                                                                    break;
                                                                } else if ("n".equalsIgnoreCase(confirmAdj)) {
                                                                    System.out.println("Enter another salary adjustment percentage.\n");
                                                                    continue;
                                                                } else {
                                                                    System.out.println("Returning to menu.\n");
                                                                    break;
                                                                }
                                                            }
                                                            break;
                                                        }
                                                    } else {
                                                        ui.displayAccessDenied();
                                                    }
                                                    break;
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
                            if (currentUser instanceof models.Employee emp) {
                                System.out.println("Salary: $" + String.format("%,.2f", emp.getSalary()));
                                System.out.println("Occupation: " + (emp.getOccupation() != null ? emp.getOccupation() : "N/A"));
                            } else if (currentUser instanceof models.HRAdmin admin) {
                                System.out.println("Salary: $" + String.format("%,.2f", admin.getSalary()));
                                System.out.println("Occupation: " + (admin.getOccupation() != null ? admin.getOccupation() : "N/A"));
                            }
                            System.out.println();
                            break;
                        case 2:
                            if (isAdmin) {
                                System.out.println("\nAll Employees:");
                                for (models.Employee emp : authService.getAllEmployees()) {
                                    System.out.println("ID: " + emp.getEmpId() + ", Name: " + emp.getFullName() + ", Email: " + emp.getEmail());
                                }
                                System.out.println();
                            } else {
                                ui.displayAccessDenied();
                            }
                            break;
                        case 3:
                            if (isAdmin) {
                                System.out.println("\nAdd New Employee (enter 'q' at any prompt to cancel- WARNING: This will ERASE your progress):");
                                String newIdInput;
                                int newId = -1;
                                boolean firstPrompt = true;
                                while (true) {
                                    newIdInput = ui.getEmployeeIdInput();
                                    if ("q".equalsIgnoreCase(newIdInput)) break;
                                    try { newId = Integer.parseInt(newIdInput); break; }
                                    catch (NumberFormatException e) {
                                        if (!firstPrompt) {
                                            System.out.println("Invalid input. Enter Employee ID (numeric) or 'q' to cancel.");
                                        }
                                    }
                                    firstPrompt = false;
                                }
                                if ("q".equalsIgnoreCase(newIdInput)) break;
                                String newFName = ui.getFirstName();
                                if ("q".equalsIgnoreCase(newFName)) break;
                                String newLName = ui.getLastName();
                                if ("q".equalsIgnoreCase(newLName)) break;
                                String newEmail = ui.getEmail();
                                if ("q".equalsIgnoreCase(newEmail)) break;
                                String newHireDate = ui.getHireDate();
                                if ("q".equalsIgnoreCase(newHireDate)) break;
                                String newSSN = ui.getSSN();
                                if ("q".equalsIgnoreCase(newSSN)) break;
                                Double newSalary = ui.getSalary();
                                if (newSalary == null) break;
                                String newOccupation = ui.getOccupation();
                                if ("q".equalsIgnoreCase(newOccupation)) break;
                                models.Employee newEmp = new models.Employee(newId, newFName, newLName, newEmail, newSalary, newHireDate, newSSN, newOccupation);
                                if (authService.addEmployee(newEmp, newHireDate, newSSN, newSalary)) {
                                    System.out.println("Employee added successfully.\n");
                                } else {
                                    System.out.println("Failed to add employee.\n");
                                }
                            } else {
                                ui.displayAccessDenied();
                            }
                            break;
                        case 4:
                            if (isAdmin) {
                                System.out.println("\nUpdate Employee (enter 'q' at any prompt to cancel):");
                                String upIdInput;
                                int upId = -1;
                                boolean firstPromptUp = true;
                                while (true) {
                                    upIdInput = ui.getEmployeeIdInput();
                                    if ("q".equalsIgnoreCase(upIdInput)) break;
                                    try { upId = Integer.parseInt(upIdInput); break; }
                                    catch (NumberFormatException e) {
                                        if (!firstPromptUp) {
                                            System.out.println("Invalid input. Enter Employee ID (numeric) or 'q' to cancel.");
                                        }
                                    }
                                    firstPromptUp = false;
                                }
                                if ("q".equalsIgnoreCase(upIdInput)) break;
                                String upFName = ui.getFirstName();
                                if ("q".equalsIgnoreCase(upFName)) break;
                                String upLName = ui.getLastName();
                                if ("q".equalsIgnoreCase(upLName)) break;
                                String upEmail = ui.getEmail();
                                if ("q".equalsIgnoreCase(upEmail)) break;
                                models.Employee upEmp = new models.Employee(upId, upFName, upLName, upEmail);
                                if (authService.updateEmployee(upEmp)) {
                                    System.out.println("Employee updated successfully.\n");
                                } else {
                                    System.out.println("Failed to update employee.\n");
                                }
                            } else {
                                ui.displayAccessDenied();
                            }
                            break;
                        case 5:
                            if (isAdmin) {
                                System.out.println("\nDelete Employee (enter 'q' to cancel):");
                                String delIdInput;
                                int tempDelId = -1;
                                boolean firstPromptDel = true;
                                while (true) {
                                    delIdInput = ui.getEmployeeIdInput();
                                    if ("q".equalsIgnoreCase(delIdInput)) break;
                                    try { tempDelId = Integer.parseInt(delIdInput); break; }
                                    catch (NumberFormatException e) {
                                        if (!firstPromptDel) {
                                            System.out.println("Invalid input. Enter Employee ID (numeric) or 'q' to cancel.");
                                        }
                                    }
                                    firstPromptDel = false;
                                }
                                if ("q".equalsIgnoreCase(delIdInput)) break;
                                final int delId = tempDelId;
                                // Fetch employee info for confirmation
                                models.Employee empToDelete = authService.getAllEmployees().stream()
                                    .filter(e -> e.getEmpId() == delId)
                                    .findFirst().orElse(null);
                                if (empToDelete == null) {
                                    System.out.println("No employee found with ID " + delId + ".\n");
                                    break;
                                }
                                System.out.print("\nAre you sure you wish to delete '" + empToDelete.getFirstName() + "'? This action cannot be reversed. (y/n): ");
                                String confirm = ui.getScanner().nextLine();
                                if (!"y".equalsIgnoreCase(confirm)) {
                                    System.out.println("\nAction aborted.\n");
                                    break;
                                }
                                if (authService.deleteEmployee(delId)) {
                                    System.out.println("\nEmployee has been deleted successfully.\n");
                                } else {
                                    System.out.println("Failed to delete employee.\n");
                                }
                            } else {
                                ui.displayAccessDenied();
                            }
                            break;
                        case 6:
                            if (isAdmin) {
                                System.out.println("\nSearch Employee by ID (enter 'q' to cancel):");
                                String searchIdInput;
                                int searchId = -1;
                                boolean firstPromptSearch = true;
                                while (true) {
                                    searchIdInput = ui.getEmployeeIdInput();
                                    if ("q".equalsIgnoreCase(searchIdInput)) break;
                                    try { searchId = Integer.parseInt(searchIdInput); break; }
                                    catch (NumberFormatException e) {
                                        if (!firstPromptSearch) {
                                            System.out.println("Invalid input. Enter Employee ID (numeric) or 'q' to cancel.");
                                        }
                                    }
                                    firstPromptSearch = false;
                                }
                                if ("q".equalsIgnoreCase(searchIdInput)) break;
                                models.Employee foundEmp = null;
                                for (models.Employee emp : authService.getAllEmployees()) {
                                    if (emp.getEmpId() == searchId) {
                                        foundEmp = emp;
                                        break;
                                    }
                                }
                                if (foundEmp != null) {
                                    System.out.println("\nEmployee Found:");
                                    System.out.println("ID: " + foundEmp.getEmpId());
                                    System.out.println("Name: " + foundEmp.getFullName());
                                    System.out.println("Email: " + foundEmp.getEmail());
                                    System.out.println("Salary: $" + String.format("%,.2f", foundEmp.getSalary()));
                                    System.out.println("Hire Date: " + (foundEmp.getHireDate() != null ? foundEmp.getHireDate() : "N/A"));
                                    System.out.println("SSN: " + (foundEmp.getSSN() != null ? foundEmp.getSSN() : "N/A"));
                                    System.out.println("Occupation: " + (foundEmp.getOccupation() != null ? foundEmp.getOccupation() : "N/A"));
                                    // Salary increase option
                                    System.out.print("\nWould you like to increase this employee's salary? (y/n): ");
                                    String incChoice = ui.getScanner().nextLine();
                                    if ("y".equalsIgnoreCase(incChoice)) {
                                        System.out.print("Enter percentage to increase salary by (e.g., 5 for 5%): ");
                                        String percentInput = ui.getScanner().nextLine();
                                        try {
                                            double percent = Double.parseDouble(percentInput);
                                            double oldSalary = foundEmp.getSalary();
                                            double newSalary = oldSalary * (1 + percent / 100.0);
                                            System.out.printf("Original Salary: $%,.2f\n", oldSalary);
                                            System.out.printf("Proposed New Salary: $%,.2f\n", newSalary);
                                            System.out.print("Would you like to confirm this salary adjustment for " + foundEmp.getFullName() + "? (y/n): ");
                                            String confirm = ui.getScanner().nextLine();
                                            if ("y".equalsIgnoreCase(confirm)) {
                                                // Update salary in DB
                                                foundEmp = new models.Employee(foundEmp.getEmpId(), foundEmp.getFirstName(), foundEmp.getLastName(), foundEmp.getEmail(), newSalary, foundEmp.getHireDate(), foundEmp.getSSN(), foundEmp.getOccupation());
                                                if (authService.updateEmployee(foundEmp)) {
                                                    System.out.println("\nSalary has been updated successfully.\n");
                                                } else {
                                                    System.out.println("\nFailed to update salary.\n");
                                                }
                                            } else {
                                                System.out.println("\nSalary adjustment cancelled.\n");
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid percentage. Salary adjustment cancelled.\n");
                                        }
                                    }
                                } else {
                                    System.out.println("\nEmployee not found.\n");
                                }
                            } else {
                                ui.displayAccessDenied();
                            }
                            break;
                        default:
                            ui.displayAccessDenied();
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
