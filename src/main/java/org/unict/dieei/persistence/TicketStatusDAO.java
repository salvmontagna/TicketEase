package org.unict.dieei.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.unict.dieei.domain.TicketStatus;
import org.unict.dieei.domain.User;
import org.unict.dieei.observer.Observer;
import org.unict.dieei.observer.ClientObserver;
import org.unict.dieei.observer.NotificationManager;

import java.util.List;

import org.unict.dieei.domain.Ticket;

public class TicketStatusDAO {
    private final EntityManager entityManager;

    public TicketStatusDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void updateTicketStatus(int ticketId, String newStatus, int updatedBy) {
        entityManager.getTransaction().begin();

        Ticket ticket = entityManager.find(Ticket.class, ticketId);
        User user = entityManager.find(User.class, updatedBy);

        if (ticket == null || user == null) {
            System.out.println("Errore: Ticket o utente non trovato.");
            entityManager.getTransaction().rollback();
            return;
        }

        TicketStatus ticketStatus = new TicketStatus(newStatus, ticket, user);
        entityManager.persist(ticketStatus);

        ticket.setStatus(newStatus);
        entityManager.merge(ticket);

        entityManager.getTransaction().commit();

        if ("closed".equalsIgnoreCase(newStatus)) {
            sendClosureNotification(ticket);
        }

        System.out.println("Stato del ticket " + ticketId + " aggiornato a: " + newStatus);
    }

    public List<TicketStatus> getTicketHistory(int ticketId) {
        TypedQuery<TicketStatus> query = entityManager.createQuery(
                "SELECT ts FROM TicketStatus ts WHERE ts.ticket.id = :ticketId ORDER BY ts.updateDate DESC",
                TicketStatus.class
        );
        query.setParameter("ticketId", ticketId);
        return query.getResultList();
    }

    private void sendClosureNotification(Ticket ticket) {
        int clientId = ticket.getCreatedUser().getId();

        if (clientId > 0) {
            Observer clientObserver = new ClientObserver(clientId, ticket.getId(), entityManager);
            NotificationManager.addObserver(clientObserver);
            NotificationManager.notifyObservers("Il tuo ticket con ID " + ticket.getId() + " è stato chiuso.");
            NotificationManager.removeObserver(clientObserver);
        }
    }

}
