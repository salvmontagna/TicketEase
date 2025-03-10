package org.unict.dieei.service;

import org.unict.dieei.dto.Products;
import org.unict.dieei.dto.Ticket;
import org.unict.dieei.dto.User;
import org.unict.dieei.persistence.ProductsDAO;
import org.unict.dieei.persistence.TicketDAO;

import java.util.List;

public class TicketService {

    private TicketDAO ticketDAO;
    private ProductsDAO productsDAO;

    public TicketService(TicketDAO ticketDAO, ProductsDAO productsDAO) {
        this.ticketDAO = ticketDAO;
        this.productsDAO = productsDAO;
    }

    public void createTicket(String title, String description, User user, int productId) {

        Products product = productsDAO.findById(productId);
        if (product == null) {
            System.out.println("Errore: Il prodotto con ID " + productId + " non esiste.");
            return;
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setCreatedUser(user);
        ticket.setProduct(product);
        ticketDAO.saveTicket(ticket);
    }

    public List<Ticket> getAllOpenedTickets() {
        return ticketDAO.getAllOpenedTickets();
    }

    public void showProducts(){
        List<Products> productList = productsDAO.getAllProducts();
        System.out.println("Lista Prodotti Disponibili:");
        productList.forEach(product -> System.out.println(product.getId() + ": " + product.getProductName()));
    }

}
