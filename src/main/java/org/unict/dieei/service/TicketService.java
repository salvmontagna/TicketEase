package org.unict.dieei.service;

import jakarta.persistence.EntityManager;
import org.unict.dieei.domain.Products;
import org.unict.dieei.domain.Ticket;
import org.unict.dieei.domain.User;
import org.unict.dieei.persistence.TicketDAO;

import java.util.List;

public class TicketService {

    private final EntityManager entityManager;
    private TicketDAO ticketDAO;
    private ProductsService productsService;
    private UserService userService;
    private NotificationService notificationService;

    public TicketService(EntityManager entityManager, TicketDAO ticketDAO, ProductsService productsService, UserService userService, NotificationService notificationService) {
        this.entityManager = entityManager;
        this.ticketDAO = ticketDAO;
        this.productsService = productsService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public Ticket createTicket(String title, String description, User user, int productId) {

        Products product = productsService.findProductById(productId);

        if (product == null) {
            System.out.println("Errore: Il prodotto con ID " + productId + " non esiste.");
            return null;
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setCreatedUser(user);
        ticket.setProduct(product);

        try {
            return ticketDAO.saveTicket(ticket);
        } catch (Exception e) {
            System.out.println("Errore durante la creazione del ticket: " + e.getMessage());
            return null;
        }

    }

    public List<Ticket> getAllOpenedTickets() {
        try {
            return ticketDAO.getAllOpenedTickets();
        } catch (Exception e) {
            System.out.println("Errore durante il recupero dei ticket aperti: " + e.getMessage());
            return null;
        }
    }

    public List<Ticket> getTicketsByUser(int user, int filter) {
        try {
            return ticketDAO.getTicketsByUser(user, filter);
        } catch (Exception e) {
            System.out.println("Errore durante il recupero dei ticket per l'utente: " + e.getMessage());
            return null;
        }
    }

    public Ticket assignTicket(int ticketId, int technicianId) {

        Ticket ticket = ticketDAO.findById(ticketId);
        if (ticket == null) {
            System.out.println("Errore: Il ticket con ID " + ticketId + " non esiste.");
            return null;
        }

        User user = userService.findById(technicianId);
        if (user == null) {
            System.out.println("Errore: L'utente con ID " + technicianId + " non esiste.");
            return null;
        }

        try {
            notificationService.sendTechNotification(technicianId, ticketId, entityManager);

            ticket.setAssignedUser(user);

            return ticketDAO.saveTicket(ticket);

        } catch (Exception e) {
            System.out.println("Errore durante l'assegnazione del ticket: " + e.getMessage());
            return null;
        }

    }

    public void createAndAssignTicket(String title, String description, User client, int technicianId, int productId) {
        Ticket ticket = createTicket(title, description, client, productId);
        assignTicket(ticket.getId(), technicianId);
    }

    public List<Ticket> getAssignedTickets(int userId) {
        try{
            return ticketDAO.getAssignedTickets(userId);
        } catch (Exception e) {
            System.out.println("Errore durante il recupero dei ticket assegnati: " + e.getMessage());
            return null;
        }
    }

    public Ticket findById(int ticketId){
        try {
            return ticketDAO.findById(ticketId);
        } catch (Exception e) {
            System.out.println("Errore durante il recupero del ticket: " + e.getMessage());
            return null;
        }
    }


}
