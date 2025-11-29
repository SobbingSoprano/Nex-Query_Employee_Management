/**
 * Interface for authentication operations.
 * This demonstrates interface usage for contract-based programming.
 * Any class implementing this interface must provide authentication functionality.
 */
package interfaces;

public interface Authenticatable {
    boolean authenticate(int empId, String lastName, String dob, String ssn);
    boolean hasPermission(String operation);
}
