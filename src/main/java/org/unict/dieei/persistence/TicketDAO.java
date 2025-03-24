package org.unict.dieei.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.unict.dieei.domain.User;
import org.unict.dieei.domain.Ticket;

import java.util.List;

public class TicketDAO {
    private static EntityManager entityManager;

    public TicketDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Ticket saveTicket(Ticket ticket) {
        entityManager.getTransaction().begin();
        entityManager.persist(ticket);
        entityManager.getTransaction().commit();
        return ticket;
    }

    public List<Ticket> getAllOpenedTickets() {
        TypedQuery<Ticket> query = entityManager.createQuery(
                "SELECT t FROM Ticket t WHERE t.status != 'closed'", Ticket.class);
        return query.getResultList();
    }

    public List<Ticket> getAllTickets() {
        TypedQuery<Ticket> query = entityManager.createQuery(
                "SELECT t FROM Ticket t", Ticket.class);
        return query.getResultList();
    }

    public void assignTicket(int ticketId, int technicianId) {
        entityManager.getTransaction().begin();
        Ticket ticket = entityManager.find(Ticket.class, ticketId);
        User technician = entityManager.find(User.class, technicianId);
        ticket.setAssignedUser(technician);
        entityManager.merge(ticket);
        entityManager.getTransaction().commit();
    }

    public static List<Ticket> getTicketsByUser(int userId, String statusFilter) {
        TypedQuery<Ticket> query = entityManager.createQuery(
                "SELECT t FROM Ticket t WHERE t.createdUser.id = :userId AND t.status = :status", Ticket.class);
        query.setParameter("userId", userId);
        query.setParameter("status", statusFilter);
        return query.getResultList();
    }

    public List<Ticket> getAssignedTickets(int technicianId) {
        TypedQuery<Ticket> query = entityManager.createQuery(
                "SELECT t FROM Ticket t WHERE t.assignedUser.id = :technicianId AND t.status != 'closed'", Ticket.class);
        query.setParameter("technicianId", technicianId);
        return query.getResultList();
    }

    public List<Ticket> getTicketsByUser(int userId, int filtro) {
        String jpql = """
        SELECT t FROM Ticket t
        LEFT JOIN TicketStatus ts ON ts.ticket.id = t.id
        WHERE t.createdUser.id = :userId
        AND (ts.updateDate IS NULL OR ts.updateDate = (
            SELECT MAX(ts2.updateDate) FROM TicketStatus ts2 WHERE ts2.ticket.id = t.id
        ))
        """;

        if (filtro == 1) {
        } else if (filtro == 2) {
            jpql += " AND COALESCE(ts.status, t.status) = 'open'";
        } else if (filtro == 3) {
            jpql += " AND COALESCE(ts.status, t.status) = 'closed'";
        }

        TypedQuery<Ticket> query = entityManager.createQuery(jpql, Ticket.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }


    public Ticket findTicketById(int ticketId) {
        return entityManager.find(Ticket.class, ticketId);
    }


}
