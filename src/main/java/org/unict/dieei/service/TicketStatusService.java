package org.unict.dieei.service;

import org.unict.dieei.domain.Ticket;
import org.unict.dieei.domain.TicketStatus;
import org.unict.dieei.persistence.TicketDAO;
import org.unict.dieei.persistence.TicketStatusDAO;

import java.util.List;

public class TicketStatusService {

    private final TicketStatusDAO ticketStatusDAO;
    private final TicketDAO ticketDAO;

    public TicketStatusService(TicketStatusDAO ticketStatusDAO, TicketDAO ticketDAO) {
        this.ticketStatusDAO = ticketStatusDAO;
        this.ticketDAO = ticketDAO;
    }

    public void updateTicketStatus(int ticketId, String newStatus, int updatedBy) {
        Ticket ticket = ticketDAO.findById(ticketId);
        if (ticket == null) {
            System.out.println("Errore: Ticket con ID " + ticketId + " non trovato.");
            return;
        }

        ticketStatusDAO.updateTicketStatus(ticketId, newStatus, updatedBy);
    }

    public List<TicketStatus> getTicketHistory(int ticketId) {
        return ticketStatusDAO.getTicketHistory(ticketId);
    }

}
