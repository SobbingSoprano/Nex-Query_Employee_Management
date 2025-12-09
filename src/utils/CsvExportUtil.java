package utils;

import java.io.FileWriter;
import java.io.IOException;
import models.Employee;

public class CsvExportUtil {
    public static boolean exportEmployeeToCsv(Employee emp, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ID,First Name,Last Name,Email,Salary,Hire Date,SSN,Occupation\n");
            writer.write(String.format("%d,%s,%s,%s,%.2f,%s,%s,%s\n",
                emp.getEmpId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getEmail(),
                emp.getSalary(),
                emp.getHireDate() != null ? emp.getHireDate() : "",
                emp.getSSN() != null ? emp.getSSN() : "",
                emp.getOccupation() != null ? emp.getOccupation() : ""
            ));
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting employee to CSV: " + e.getMessage());
            return false;
        }
    }
}
