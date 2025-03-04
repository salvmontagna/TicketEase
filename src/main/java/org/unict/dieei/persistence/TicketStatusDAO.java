package org.unict.dieei.persistence;

import org.unict.dieei.configurations.DatabaseConnection;
import org.unict.dieei.observer.Observer;
import org.unict.dieei.observer.ClientObserver;
import org.unict.dieei.observer.NotificationManager;

import java.sql.*;

public class TicketStatusDAO {

    // **Aggiorna lo stato di un ticket e invia notifiche se necessario**
    public static void updateTicketStatus(int ticketId, String newStatus, int updatedBy) {
        String sqlInsertStatus = "INSERT INTO ticket_status (ticket_id, status, updated_by) VALUES (?, ?, ?)";
        String sqlUpdateTicket = "UPDATE tickets SET status = ? WHERE id = ?";
        String sqlGetClient = "SELECT created_user_id FROM tickets WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Aggiorna lo stato del ticket nella tabella principale
            try (PreparedStatement pstmtUpdateTicket = conn.prepareStatement(sqlUpdateTicket)) {
                pstmtUpdateTicket.setString(1, newStatus);
                pstmtUpdateTicket.setInt(2, ticketId);
                pstmtUpdateTicket.executeUpdate();
            }

            // Registra lo stato aggiornato nella tabella `ticket_status`
            try (PreparedStatement pstmtInsertStatus = conn.prepareStatement(sqlInsertStatus)) {
                pstmtInsertStatus.setInt(1, ticketId);
                pstmtInsertStatus.setString(2, newStatus);
                pstmtInsertStatus.setInt(3, updatedBy);
                pstmtInsertStatus.executeUpdate();
            }

            // Se lo stato diventa "closed", invia una notifica al cliente
            if ("closed".equalsIgnoreCase(newStatus)) {
                int clientId = -1;
                try (PreparedStatement pstmtGetClient = conn.prepareStatement(sqlGetClient)) {
                    pstmtGetClient.setInt(1, ticketId);
                    ResultSet rs = pstmtGetClient.executeQuery();
                    if (rs.next()) {
                        clientId = rs.getInt("created_user_id");
                    }
                }

                // Se il cliente esiste, invia una notifica
                if (clientId != -1) {
                    Observer clientObserver = new ClientObserver(clientId, ticketId);
                    NotificationManager.addObserver(clientObserver);
                    NotificationManager.notifyObservers("Il tuo ticket con ID " + ticketId + " è stato chiuso.");
                    NotificationManager.removeObserver(clientObserver);
                }
            }

            System.out.println("Stato del ticket " + ticketId + " aggiornato a: " + newStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getTicketHistory(int ticketId) {
        String sql = "SELECT * FROM ticket_status WHERE ticket_id = ? ORDER BY update_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Stato: " + rs.getString("status") +
                        ", Aggiornato da: " + rs.getInt("updated_by") + ", Data: " + rs.getTimestamp("update_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
