package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    // Constructor to initialize connection and scanner
    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Method to add a patient
    public void addPatient() {
        System.out.print("Enter patient Name: ");
        String name = scanner.next();
        System.out.print("Enter patient age: ");
        int age = scanner.nextInt();
        System.out.print("Enter patient Gender: ");
        String gender = scanner.next();
        try {
            String query = "INSERT INTO patients(name, age, gender) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient added successfully!!");
            } else {
                System.out.println("Failed to add patient!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view all patients
    public void viewPatient() {
        String query = "SELECT * FROM patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patient:");
            System.out.println("+------------+------------------+-------+--------+");
            System.out.println("| Patient ID | Name             | Age   | Gender |");
            System.out.println("+------------+------------------+-------+--------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");  // Corrected: Using getInt for age
                String gender = resultSet.getString("gender");
                System.out.printf("| %-10d | %-16s | %-5d | %-6s |\n", id, name, age, gender);
            }
            System.out.println("+------------+------------------+-------+--------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get a patient by ID
    public boolean getPatientById(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";  // Corrected to query the patients table
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.println("Patient found:");
                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Age: " + age);
                System.out.println("Gender: " + gender);
                return true;  // Return true when patient is found
            } else {
                System.out.println("No patient found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if no patient is found
    }
}
