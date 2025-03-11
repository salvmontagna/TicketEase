package org.unict.dieei.observer;

import org.unict.dieei.persistence.NotificationDAO;
import jakarta.persistence.EntityManager;

public class TechnicianObserver implements Observer {
    private final int technicianId;
    private final int ticketId;
    private final NotificationDAO notificationDAO;

    public TechnicianObserver(int technicianId, int ticketId, EntityManager entityManager) {
        this.technicianId = technicianId;
        this.ticketId = ticketId;
        this.notificationDAO = new NotificationDAO(entityManager); // Usa un'istanza valida
    }

    @Override
    public void update(String message) {
        System.out.println("Notifica per Tecnico IT (ID: " + technicianId + "): " + message);
        notificationDAO.insertNotification(message, ticketId, technicianId);
    }
}
