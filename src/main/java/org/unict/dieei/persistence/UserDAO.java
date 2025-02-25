package org.unict.dieei.persistence;

import org.unict.dieei.configurations.DatabaseConnection;
import org.unict.dieei.dto.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public static User registerUser(String name, String email, String password, int role, String taxCode, String secretKey) {

        // Se l'utente vuole registrarsi come Cliente, bypassa il controllo
        if (role == 2)
            return insertUser(name, email, password, role);

        // Verifica se il tax_code e la secret_key sono presenti nella tabella authorizations
        if (!isAuthorized(taxCode, secretKey, role)) {
            System.out.println("Errore: Il codice fiscale e/o la chiave segreta non sono validi per il ruolo selezionato.");
            return null;
        }

        // Se la verifica è superata, registra l'utente con il ruolo selezionato
        return insertUser(name, email, password, role);
    }

    // **Metodo per verificare se il codice fiscale e la secret_key sono validi**
    private static boolean isAuthorized(String taxCode, String secretKey, int role) {
        String sql = "SELECT * FROM authorizations WHERE tax_code = ? AND secret_key = ? AND role = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, taxCode);
            pstmt.setString(2, secretKey);
            pstmt.setInt(3, role);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Se esiste un record, la registrazione è autorizzata
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // **Metodo per inserire un utente nel database**
    private static User insertUser(String name, String email, String password, int role) {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setInt(4, role);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("Utente registrato con ID: " + id + " e ruolo " + role);
                return new User(id, name, email, password, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // **Metodo per autenticare un utente (Login)**
    public static User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Login fallito
    }

    // **Metodo per selezionare tutti gli utenti**
    public static List<User> getAllUsers() {
        return getUsersByRole(-1); // -1: "qualsiasi ruolo"
    }

    // **Metodo per selezionare tutti gli amministratori**
    public static List<User> getAllAdministrators() {
        return getUsersByRole(0);
    }

    // **Metodo per selezionare tutti i tecnici**
    public static List<User> getAllTechnicians() {
        return getUsersByRole(1);
    }

    // **Metodo per selezionare tutti i clienti**
    public static List<User> getAllCustomers() {
        return getUsersByRole(2);
    }

    // **Metodo generico per ottenere utenti per ruolo**
    private static List<User> getUsersByRole(int role) {
        String sql = "SELECT * FROM users" + (role >= 0 ? " WHERE role = ?" : "");
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (role >= 0) pstmt.setInt(1, role);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}