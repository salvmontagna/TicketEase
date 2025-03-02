package org.unict.dieei.persistence;

import org.unict.dieei.configurations.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificationDAO {
    public static void insertNotification(String message, int ticketId, int recipientId) {
        String sql = "INSERT INTO notifications (message, ticket_id, recipient_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, message);
            pstmt.setInt(2, ticketId);
            pstmt.setInt(3, recipientId);
            pstmt.executeUpdate();
            System.out.println("Notifica salvata per utente ID: " + recipientId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
