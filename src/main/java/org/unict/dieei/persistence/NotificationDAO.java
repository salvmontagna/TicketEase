package org.unict.dieei.persistence;

import jakarta.persistence.EntityManager;
import org.unict.dieei.domain.Notification;
import org.unict.dieei.domain.Ticket;
import org.unict.dieei.domain.User;

public class NotificationDAO {
    private final EntityManager entityManager;

    public NotificationDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveNotification(String message, int ticketId, int recipientId) {
        entityManager.getTransaction().begin();

        Ticket ticket = entityManager.find(Ticket.class, ticketId);
        User recipient = entityManager.find(User.class, recipientId);

        if (ticket == null || recipient == null) {
            System.out.println("Errore: Ticket o destinatario non trovato.");
            entityManager.getTransaction().rollback();
            return;
        }

        Notification notification = new Notification(message, ticket, recipient);
        entityManager.persist(notification);
        entityManager.getTransaction().commit();
    }

}
