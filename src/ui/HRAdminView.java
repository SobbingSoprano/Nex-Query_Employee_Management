/**
 * HR Admin View - Displays HR Admin specific menu options and information.
 * This class handles the display logic for HR Admin users with full CRUD privileges.
 * Aggregates ConsoleUI for reusable UI components.
 */
package ui;

import interfaces.DataAccessible;
import models.Person;

public class HRAdminView {
    private ConsoleUI consoleUI;
    
    public HRAdminView(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;  // Aggregation - HRAdminView HAS-A ConsoleUI
    }
    
    public void displayPermissions(Person user) {
        if (user instanceof DataAccessible) {
            DataAccessible dataUser = (DataAccessible) user;
            
            System.out.println("Your Permissions:");
            System.out.println("  Create: " + (dataUser.canCreate() ? "✓" : "✗"));
            System.out.println("  Read:   " + (dataUser.canRead() ? "✓" : "✗"));
            System.out.println("  Update: " + (dataUser.canUpdate() ? "✓" : "✗"));
            System.out.println("  Delete: " + (dataUser.canDelete() ? "✓" : "✗"));
            System.out.println();
        }
    }
    
    public void displayAdminDashboard() {
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║       HR Admin Dashboard           ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
    }
}
