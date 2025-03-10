package org.unict.dieei.dto;

import java.time.LocalDateTime;

public class Ticket {
    private int id;
    private String title;
    private String description;
    private String status; // "open", "in_progress", "closed"
    private LocalDateTime creationDate;
    private int createdUserId;

    public Ticket(int id, String title, String description, String status, LocalDateTime creationDate, int createdUserId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.creationDate = creationDate;
        this.createdUserId = createdUserId;
    }
    /*
    @Override
    public String toString() {
        return "Ticket ID: " + id + "\n"
                + "Titolo: " + title + "\n"
                + "Descrizione: " + description + "\n"
                + "Stato: " + status + "\n"
                + "Data Creazione: " + creationDate + "\n"
                + "Creato da User ID: " + createdUserId ;
    }*/

    @Override
    public String toString() {
        return "Ticket ID: " + id + " - "
                + "Titolo: " + title + " - "
                + "Descrizione: " + description + " - "
                + "Stato: " + status + " - "
                + "Data Creazione: " + creationDate + " - "
                + "Creato da User ID: " + createdUserId;
    }

    public Ticket(String title, String description, int createdUserId) {
        this(0, title, description, "open", LocalDateTime.now(), createdUserId);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public int getCreatedUserId() { return createdUserId; }
    public void setCreatedUserId(int createdUserId) { this.createdUserId = createdUserId; }
}

