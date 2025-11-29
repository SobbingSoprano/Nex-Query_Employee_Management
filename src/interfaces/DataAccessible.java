/**
 * Interface defining data access operations.
 * This demonstrates the use of interfaces to define CRUD operations.
 * Different user roles will have different implementations of these methods.
 */
package interfaces;

public interface DataAccessible {
    boolean canCreate();
    boolean canRead();
    boolean canUpdate();
    boolean canDelete();
}
