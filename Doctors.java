package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctors {
    private Connection connection;

    // Constructor to initialize connection
    public Doctors(Connection connection) {
        this.connection = connection;
    }

    // Method to view all Doctors
    public void viewDoctors() {
        String query = "SELECT * FROM DOCTORS"; // Corrected table name
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query); // Use connection object to prepare statement
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors List:");
            System.out.println("+------------+------------------+-----------+-------------+");
            System.out.println("| Doctor id  | Name               | Specialization    ");
            System.out.println("+------------+------------------+-----------+-------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                // Fixed column name typo
                System.out.printf("| %-11d | %-16s | %-19s |\n", id, name, specialization);
                System.out.println("+------------+------------------+-----------+-------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get a doctor by ID
    public boolean getDoctorById(int id) {
        String query = "SELECT id, name, age FROM doctors WHERE id = ?";   // Corrected table name
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String specialization = resultSet.getString("specialization");
                System.out.println("Doctor found ");
                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Age: " + age);
                System.out.println("Gender: " + gender);
                System.out.println("Specialization: " + specialization);
                return true; // Return true if doctor is found
            } else {
                System.out.println("No doctor found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if doctor is not found
    }
}

