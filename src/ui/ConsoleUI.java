

/**
 * Console User Interface - Handles all terminal/console input and output.
 * This class provides methods for displaying menus and gathering user input.
 * Demonstrates separation of UI concerns from business logic.
 */
package ui;

import java.util.Scanner;

public class ConsoleUI {
                public Scanner getScanner() {
                    return scanner;
                }
            public String getOccupation() {
                System.out.print("Enter Occupation/Job Title: ");
                return scanner.nextLine();
            }
        public Double getSalary() {
            while (true) {
                System.out.print("Enter Salary: ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("q")) return null;
                try {
                    String sanitized = input.replace(",", "");
                    return Double.parseDouble(sanitized);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a numeric salary or 'q' to cancel.");
                }
            }
        }
    private Scanner scanner;
    
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }
    
    public void displayWelcome() {
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║     Welcome to NexQuery System     ║");
        System.out.println("║      Employee Management Portal    ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
    }
    
    public String getEmployeeIdInput() {
        System.out.print("Enter Employee ID: ");
        String input = scanner.nextLine();
        return input;
    }

    // For backward compatibility, keep getEmployeeId for non-cancel flows
    public int getEmployeeId() {
        while (true) {
            System.out.print("Enter Employee ID: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) return -1; // -1 means cancel
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter Employee ID (numeric): ");
            }
        }
    }

    public String getFirstName() {
        System.out.print("Enter First Name: ");
        return scanner.nextLine();
    }

    public String getLastName() {
        System.out.print("Enter Last Name: ");
        return scanner.nextLine();
    }

    public String getHireDate() {
        System.out.print("Enter Hire Date (YYYY-MM-DD): ");
        return scanner.nextLine();
    }

    public String getSSN() {
        System.out.print("Enter SSN (xxx-xx-xxxx): ");
        return scanner.nextLine();
    }
    
    public String getEmail() {
        System.out.print("Enter Email: ");
        return scanner.nextLine();
    }
    
    public void displayLoginSuccess(String name) {
        System.out.println("\n✓ Login successful!");
        System.out.println();
    }
    
    public void displayLoginFailure() {
        System.out.println("\n✗ Login failed!");
        System.out.println("Invalid Employee ID, Last Name, DOB, or SSN.");
        System.out.println("Please try again.\n");
    }
    
    public void displayAccessDenied() {
        System.out.println("\n✗ Access Denied!");
        System.out.println("You do not have permission to perform this operation.\n");
    }
    
    public int displayMainMenu(boolean isAdmin) {
        System.out.println("─────────────────────────────────────");
        System.out.println("Main Menu:");
        System.out.println("─────────────────────────────────────");
        System.out.println("1. View My Information");
        
        if (isAdmin) {
            System.out.println("2. View All Employees");
            System.out.println("3. Add Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. Search Employee");
        }
        
        System.out.println("0. Logout");
        System.out.println("─────────────────────────────────────");
        System.out.print("Select an option: ");
        
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
    
    public void close() {
        scanner.close();
    }
}
