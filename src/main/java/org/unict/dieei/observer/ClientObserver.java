package org.unict.dieei.observer;

import org.unict.dieei.persistence.NotificationDAO;
import jakarta.persistence.EntityManager;

public class ClientObserver implements Observer {
    private final int clientId;
    private final int ticketId;
    private final NotificationDAO notificationDAO;

    public ClientObserver(int clientId, int ticketId, EntityManager entityManager) {
        this.clientId = clientId;
        this.ticketId = ticketId;
        this.notificationDAO = new NotificationDAO(entityManager);
    }

    @Override
    public void update(String message) {
        System.out.println("Notifica per Cliente (ID: " + clientId + "): " + message);

        try{
            notificationDAO.saveNotification(message, ticketId, clientId);
        } catch (Exception e) {
            System.out.println("Errore durante il salvataggio della notifica del cliente: " + e.getMessage());
        }
    }
}
