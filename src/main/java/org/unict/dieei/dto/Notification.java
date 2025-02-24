package org.unict.dieei.dto;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private String message;
    private int ticketId;
    private int recipientId;
    private LocalDateTime sentDate;

    public Notification(int id, String message, int ticketId, int recipientId, LocalDateTime sentDate) {
        this.id = id;
        this.message = message;
        this.ticketId = ticketId;
        this.recipientId = recipientId;
        this.sentDate = sentDate;
    }

    public Notification(String message, int ticketId, int recipientId) {
        this(0, message, ticketId, recipientId, LocalDateTime.now());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    public int getRecipientId() { return recipientId; }
    public void setRecipientId(int recipientId) { this.recipientId = recipientId; }

    public LocalDateTime getSentDate() { return sentDate; }
    public void setSentDate(LocalDateTime sentDate) { this.sentDate = sentDate; }
}
