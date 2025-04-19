package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

import static java.lang.Class.forName;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "8055";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctors doctors = new Doctors(connection);

            while (true) {
                System.out.println("1 Add Patient");
                System.out.println("2 View Patients");
                System.out.println("3 View Doctors");
                System.out.println("4 Book Appointment");
                System.out.println("5 Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1: // Add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2: // View patients
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3: // View doctors
                        doctors.viewDoctors();  // Fixed method name
                        System.out.println();
                        break;
                    case 4: // Book Appointment
                        bookAppointment(patient, doctors, connection, scanner);
                        System.out.println();
                        break;
                    case 5: // Exit
                        return;
                    default:
                        System.out.println("Enter a valid choice!!!");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctors doctors, Connection connection, Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        // Check if patient and doctor exist
        if (patient.getPatientById(patientId) && doctors.getDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
                String appointmentQuery = "INSERT INTO appointment(patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery)) {
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment Booked! Details:");
                        System.out.println("Patient ID: " + patientId);
                        System.out.println("Doctor ID: " + doctorId);
                        System.out.println("Appointment Date: " + appointmentDate);
                    } else {
                        System.out.println("Failed to Book Appointment");
                    }
                } catch (SQLException e) {
                    System.err.println("An error occurred while booking the appointment: " + e.getMessage());
                }
            } else {
                System.out.println("Doctor is not available on this date.");
            }
        } else {
            System.out.println("Doctor or Patient doesn't exist!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
        // This query counts the number of appointments on a given date for a specific doctor
        String query = "SELECT COUNT(*) FROM appointment WHERE doctor_id = ? AND appointment_date = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    // Assuming doctor can have up to 5 appointments per day (as an example)
                    int maxAppointmentsPerDay = 5;
                    return count < maxAppointmentsPerDay;  // Doctor is available if count is less than max
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error occurred: " + e.getMessage());
        }
        return false;
    }
}


