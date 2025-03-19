package org.unict.dieei.service;

import jakarta.persistence.EntityManager;
import org.unict.dieei.domain.Ticket;
import org.unict.dieei.observer.ClientObserver;
import org.unict.dieei.observer.NotificationManager;
import org.unict.dieei.observer.Observer;
import org.unict.dieei.observer.TechnicianObserver;
import org.unict.dieei.persistence.NotificationDAO;

public class NotificationService {

    private NotificationDAO notificationDAO;
    private EntityManager em;

    public NotificationService(NotificationDAO notificationDAO, EntityManager em) {
        this.notificationDAO = notificationDAO;
        this.em = em;
    }

    public void sendTechNotification(int technicianId, int ticketId, EntityManager em) {
        Observer technicianObserver = new TechnicianObserver(technicianId, ticketId, em);
        NotificationManager.addObserver(technicianObserver);
        NotificationManager.notifyObservers("Ti è stato assegnato un nuovo ticket con ID " + ticketId);
        NotificationManager.removeObserver(technicianObserver);
    }

    public void sendClientNotification(Ticket ticket) {
        int clientId = ticket.getCreatedUser().getId();

        if (clientId > 0) {
            Observer clientObserver = new ClientObserver(clientId, ticket.getId(), em);
            NotificationManager.addObserver(clientObserver);
            NotificationManager.notifyObservers("Il tuo ticket con ID " + ticket.getId() + " è stato chiuso.");
            NotificationManager.removeObserver(clientObserver);
        }
    }

}
