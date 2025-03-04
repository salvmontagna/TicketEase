package org.unict.dieei.observer;

import org.unict.dieei.persistence.NotificationDAO;

public class ClientObserver implements Observer {
    private final int clientId;
    private final int ticketId;

    public ClientObserver(int clientId, int ticketId) {
        this.clientId = clientId;
        this.ticketId = ticketId;
    }

    @Override
    public void update(String message) {
        System.out.println("Notifica per Cliente (ID: " + clientId + "): " + message);
        NotificationDAO.insertNotification(message, clientId, ticketId);
    }
}
