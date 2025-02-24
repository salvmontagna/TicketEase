package org.unict.dieei.dto;

import java.time.LocalDateTime;

public class TicketStatus {
    private int id;
    private String status; // "open", "in_progress", "closed"
    private LocalDateTime updateDate;
    private int ticketId;
    private int updatedBy; // ID dell'utente che ha modificato lo stato (Admin o Tecnico IT)

    public TicketStatus(int id, String status, LocalDateTime updateDate, int ticketId, int updatedBy) {
        this.id = id;
        this.status = status;
        this.updateDate = updateDate;
        this.ticketId = ticketId;
        this.updatedBy = updatedBy;
    }

    public TicketStatus(String status, int ticketId, int updatedBy) {
        this(0, status, LocalDateTime.now(), ticketId, updatedBy);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDateTime updateDate) { this.updateDate = updateDate; }

    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    public int getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(int updatedBy) { this.updatedBy = updatedBy; }
}
