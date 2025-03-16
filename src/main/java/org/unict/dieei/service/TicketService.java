package org.unict.dieei.service;

import jakarta.persistence.EntityManager;
import org.unict.dieei.domain.Products;
import org.unict.dieei.domain.Ticket;
import org.unict.dieei.domain.User;
import org.unict.dieei.observer.NotificationManager;
import org.unict.dieei.observer.Observer;
import org.unict.dieei.observer.TechnicianObserver;
import org.unict.dieei.persistence.TicketDAO;
import org.unict.dieei.persistence.UserDAO;

import java.util.List;

public class TicketService {

    private final EntityManager entityManager;
    private TicketDAO ticketDAO;
    private ProductsService productsService;
    private UserDAO userDAO;

    public TicketService(EntityManager entityManager, TicketDAO ticketDAO, ProductsService productsService, UserDAO userDAO) {
        this.entityManager = entityManager;
        this.ticketDAO = ticketDAO;
        this.productsService = productsService;
        this.userDAO = userDAO;
    }

    public Ticket createTicket(String title, String description, User user, int productId) {

        Products product = productsService.findById(productId);

        if (product == null) {
            System.out.println("Errore: Il prodotto con ID " + productId + " non esiste.");
            return null;
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setCreatedUser(user);
        ticket.setProduct(product);

        return ticketDAO.saveTicket(ticket);

    }

    public List<Ticket> getAllOpenedTickets() {
        return ticketDAO.getAllOpenedTickets();
    }

    public List<Ticket> getTicketsByUser(int user, int filter) {
        return ticketDAO.getTicketsByUser(user, filter);
    }

    public void assignTicket(int ticketId, int technicianId) {
        Ticket ticket = ticketDAO.findById(ticketId);
        if (ticket == null) {
            System.out.println("Errore: Il ticket con ID " + ticketId + " non esiste.");
            return;
        }

        User user = userDAO.findById(technicianId);
        if (user == null) {
            System.out.println("Errore: L'utente con ID " + technicianId + " non esiste.");
            return;
        }

        Observer technicianObserver = new TechnicianObserver(technicianId, ticketId, entityManager);
        NotificationManager.addObserver(technicianObserver);
        NotificationManager.notifyObservers("Ti è stato assegnato un nuovo ticket con ID " + ticketId);
        NotificationManager.removeObserver(technicianObserver);

        ticket.setAssignedUser(user);
        ticketDAO.saveTicket(ticket);

    }

    public void createAndAssignTicket(String title, String description, User client, int technicianId, int productId) {
        Ticket ticket = createTicket(title, description, client, productId);
        assignTicket(ticket.getId(), technicianId);
    }

    public List<Ticket> getAssignedTickets(int userId) {
        return ticketDAO.getAssignedTickets(userId);
    }

}
