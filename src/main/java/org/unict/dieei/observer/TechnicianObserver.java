package org.unict.dieei.observer;

import org.unict.dieei.persistence.NotificationDAO;

public class TechnicianObserver implements Observer {
    private final int technicianId;
    private final int ticketId;

    public TechnicianObserver(int technicianId, int ticketId) {
        this.technicianId = technicianId;
        this.ticketId = ticketId;
    }

    @Override
    public void update(String message) {
        System.out.println("Notifica per Tecnico IT (ID: " + technicianId + "): " + message);
        NotificationDAO.insertNotification(message, ticketId, technicianId);
    }
}
