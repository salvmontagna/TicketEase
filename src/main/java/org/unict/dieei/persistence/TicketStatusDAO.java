package org.unict.dieei.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.unict.dieei.domain.TicketStatus;
import org.unict.dieei.domain.User;

import java.util.List;

import org.unict.dieei.domain.Ticket;

public class TicketStatusDAO {
    private final EntityManager entityManager;

    public TicketStatusDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Ticket update(int ticketId, String newStatus, int updatedBy, String statusDescription) {
        entityManager.getTransaction().begin();

        Ticket ticket = entityManager.find(Ticket.class, ticketId);
        User user = entityManager.find(User.class, updatedBy);

        if (ticket == null || user == null) {
            System.out.println("Errore: Ticket o utente non trovato.");
            entityManager.getTransaction().rollback();
            return null;
        }

        TicketStatus ticketStatus = new TicketStatus(newStatus, ticket, user, statusDescription);
        entityManager.persist(ticketStatus);

        ticket.setStatus(newStatus);
        entityManager.merge(ticket);

        entityManager.getTransaction().commit();

        System.out.println("Stato del ticket " + ticketId + " aggiornato a: " + newStatus);

        return ticket;
    }

    public List<TicketStatus> getTicketHistory(int ticketId) {
        TypedQuery<TicketStatus> query = entityManager.createQuery(
                "SELECT ts FROM TicketStatus ts WHERE ts.ticket.id = :ticketId ORDER BY ts.updateDate DESC",
                TicketStatus.class
        );
        query.setParameter("ticketId", ticketId);
        return query.getResultList();
    }


}
