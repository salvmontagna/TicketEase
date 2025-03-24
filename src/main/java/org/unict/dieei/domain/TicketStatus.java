package org.unict.dieei.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_status")
public class TicketStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 20)
    private String status; // "open", "in_progress", "closed"

    @Column(name = "status_description", nullable = false)
    private String statusDescription;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "updated_by", nullable = false)
    private User updatedBy; // L'utente che ha modificato lo stato (Admin o Tecnico IT)

    protected TicketStatus() {}

    @Override
    public String toString() {
        return "\n--- Ticket Status Update ---\n" +
                "Status:           " + status + "\n" +
                "Description:      " + statusDescription + "\n" +
                "Updated on:       " + updateDate + "\n" +
                "Ticket ID:        " + (ticket != null ? ticket.getId() : "N/A") + "\n" +
                "Updated by:       " + (updatedBy != null ? updatedBy.getEmail() : "N/A") + "\n";
    }

    public TicketStatus(String status, Ticket ticket, User updatedBy, String statusDescription) {
        this.status = status;
        this.ticket = ticket;
        this.updatedBy = updatedBy;
        this.updateDate = LocalDateTime.now();
        this.statusDescription = statusDescription;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusDescription() { return statusDescription; }
    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }

    public LocalDateTime getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDateTime updateDate) { this.updateDate = updateDate; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public User getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(User updatedBy) { this.updatedBy = updatedBy; }
}