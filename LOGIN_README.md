# NexQuery Login and Authorization System

### Object-Oriented Concepts Used:

1. **Abstract Classes**

   - `models/Person.java` - Base class for all person types with common attributes

2. **Inheritance**

   - `models/Employee.java` extends `Person`
   - `models/HRAdmin.java` extends `Person`

3. **Interfaces**

   - `interfaces/Authenticatable.java` - Authentication contract
   - `interfaces/DataAccessible.java` - CRUD permissions contract

4. **Interface Implementation**

   - Both `Employee` and `HRAdmin` implement `DataAccessible`
   - `AuthenticationService` implements `Authenticatable`

5. **Aggregation**

   - `HRAdminView` HAS-A `ConsoleUI` (aggregation relationship)

6. **Design Patterns**
   - **Singleton Pattern**: `DatabaseConnection` ensures single DB connection
   - **DAO Pattern**: `EmployeeDAO` separates data access logic
   - **Service Layer**: `AuthenticationService` handles business logic
   - **MVC-like**: Separation of UI, business logic, and data access

## File Structure

```
src/
├── App.java                          # Main entry point
├── models/
│   ├── Person.java                   # Abstract base class
│   ├── Employee.java                 # Employee with read-only access
│   └── HRAdmin.java                  # HR Admin with full CRUD access
├── interfaces/
│   ├── Authenticatable.java          # Authentication interface
│   └── DataAccessible.java           # CRUD permissions interface
├── utils/
│   └── DatabaseConnection.java       # Singleton DB connection
├── dao/
│   └── EmployeeDAO.java              # Data access layer
├── services/
│   └── AuthenticationService.java    # Authentication business logic
└── ui/
    ├── ConsoleUI.java                # Terminal interface
    └── HRAdminView.java              # HR Admin specific views
```

## Features Implemented

### ✓ User Login

- Validates Employee ID and password against database
- Prevents unauthorized access with error messaging
- Maximum 3 login attempts

### ✓ Role-Based Authorization

- **HR Admin**: Full CRUD permissions (Create, Read, Update, Delete)
- **Employee**: Read-only access to their own data

### ✓ OOP Principles

- Abstraction through abstract `Person` class
- Inheritance with `Employee` and `HRAdmin` subclasses
- Polymorphism through interfaces
- Encapsulation with proper access modifiers
- Aggregation in UI components

## Next Steps (To Be Implemented)

The current implementation focuses on login and authorization. Future enhancements will include:

- Full CRUD operations for HR Admins
- Employee data viewing
- Password encryption
- Input validation and sanitization
- Error handling improvements
- Logging system
