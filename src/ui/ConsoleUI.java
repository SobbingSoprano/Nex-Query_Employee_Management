/**
 * Console User Interface - Handles all terminal/console input and output.
 * This class provides methods for displaying menus and gathering user input.
 * Demonstrates separation of UI concerns from business logic.
 */
package ui;

import java.util.Scanner;

public class ConsoleUI {
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
    
    public int getEmployeeId() {
        System.out.print("Enter Employee ID: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter Employee ID (numeric): ");
            scanner.next();
        }
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return id;
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
    
    public void displayLoginSuccess(String name) {
        System.out.println("\n✓ Login successful!");
        System.out.println("Welcome, " + name.split(" ")[0]);
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
