package org.unict.dieei.persistence;

import org.unict.dieei.configurations.DatabaseConnection;
import org.unict.dieei.observer.NotificationManager;
import org.unict.dieei.dto.Ticket;
import org.unict.dieei.observer.Observer;
import org.unict.dieei.observer.TechnicianObserver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    public static Ticket createTicket(String title, String description, int createdUserId) {
        String sql = "INSERT INTO tickets (title, description, created_user_id) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setInt(3, createdUserId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("Ticket creato con ID: " + id);
                return new Ticket(id, title, description, "open", java.time.LocalDateTime.now(), createdUserId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // **Metodo per l'amministratore per creare e assegnare un ticket direttamente a un tecnico**
    public static void createAndAssignTicket(String title, String description, int clientId, int technicianId, int adminId) {
        Ticket ticket = createTicket(title, description, clientId); // Il ticket è registrato per il CLIENTE
        if (ticket != null) {
            assignTicket(ticket.getId(), technicianId, adminId); // L'amministratore assegna il ticket al tecnico
            System.out.println("Ticket " + ticket.getId() + " creato per il cliente " + clientId + " e assegnato al tecnico " + technicianId);
        } else {
            System.out.println("Errore nella creazione del ticket.");
        }
    }

    // **Recupera tutti i ticket di un utente con stato aggiornato dalla tabella ticket_status**
    public static List<Ticket> getTicketsByUser(int userId, int filtro) {
        String sql = """
            SELECT t.id, t.title, t.description,
                   COALESCE(ts.status, t.status) AS current_status,
                   t.creation_date, t.created_user_id
            FROM tickets t
            LEFT JOIN (
                SELECT DISTINCT ON (ticket_id) ticket_id, status
                FROM ticket_status
                ORDER BY ticket_id, update_date DESC
            ) ts ON t.id = ts.ticket_id
            WHERE t.created_user_id = ?
            """;

        if (filtro == 2) sql += " AND COALESCE(ts.status, t.status) = 'open'";
        if (filtro == 3) sql += " AND COALESCE(ts.status, t.status) = 'closed'";

        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tickets.add(new Ticket(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("current_status"), // Ora prende lo stato più recente
                        rs.getTimestamp("creation_date").toLocalDateTime(),
                        rs.getInt("created_user_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    // **Assegna un ticket a un tecnico IT**
    public static void assignTicket(int ticketId, int technicianId, int adminId) {
        String sqlUpdate = "UPDATE tickets SET assigned_user_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Assegna il ticket al tecnico IT
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                pstmtUpdate.setInt(1, technicianId);
                pstmtUpdate.setInt(2, ticketId);
                pstmtUpdate.executeUpdate();
            }

            // Attiva la notifica al tecnico
            Observer technicianObserver = new TechnicianObserver(technicianId, ticketId);
            NotificationManager.addObserver(technicianObserver);
            NotificationManager.notifyObservers("Ti è stato assegnato un nuovo ticket con ID " + ticketId);
            NotificationManager.removeObserver(technicianObserver);

            System.out.println("Ticket " + ticketId + " assegnato al tecnico " + technicianId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // **Recupera tutti i ticket assegnati a un tecnico IT con stato aggiornato**
    public static List<Ticket> getAssignedTickets(int technicianId) {
        String sql = """
            SELECT t.id, t.title, t.description,
                   COALESCE(ts.status, t.status) AS current_status,
                   t.creation_date, t.created_user_id
            FROM tickets t
            LEFT JOIN (
                SELECT DISTINCT ON (ticket_id) ticket_id, status
                FROM ticket_status
                ORDER BY ticket_id, update_date DESC
            ) ts ON t.id = ts.ticket_id
            WHERE t.assigned_user_id = ? AND COALESCE(ts.status, t.status) != 'closed'
            """;

        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, technicianId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tickets.add(new Ticket(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("current_status"), // Usa lo stato più recente
                        rs.getTimestamp("creation_date").toLocalDateTime(),
                        rs.getInt("created_user_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

}
