package org.unict.dieei.service;

import org.unict.dieei.domain.Ticket;
import org.unict.dieei.domain.TicketStatus;
import org.unict.dieei.persistence.TicketStatusDAO;

import java.util.List;

public class TicketStatusService {

    private final TicketStatusDAO ticketStatusDAO;
    private final TicketService ticketService;
    private final NotificationService notificationService;

    public TicketStatusService(TicketStatusDAO ticketStatusDAO, TicketService ticketService, NotificationService notificationService) {
        this.ticketStatusDAO = ticketStatusDAO;
        this.ticketService = ticketService;
        this.notificationService = notificationService;
    }



    public void updateTicketStatus(int ticketId, String newStatus, int updatedBy, String statusDescription) {
        Ticket ticket = ticketService.findById(ticketId);
        if (ticket == null) {
            System.out.println("Errore: Ticket con ID " + ticketId + " non trovato.");
            return;
        }

        if (statusDescription == null || statusDescription.isEmpty()) {
            System.out.println("Errore: la descrizione non deve essere vuota.");
            return;
        }

        try {
            ticketStatusDAO.update(ticketId, newStatus, updatedBy, statusDescription);
        } catch (Exception e) {
            System.out.println("Errore durante l'aggiornamento dello stato del ticket: " + e.getMessage());
        }

        if ("closed".equalsIgnoreCase(newStatus)) {
            notificationService.sendClientNotification(ticket);
        }

    }

    public List<TicketStatus> getTicketHistory(int ticketId) {
        try {
            return ticketStatusDAO.getTicketHistory(ticketId);
        } catch (Exception e) {
            return null;
        }
    }

}
