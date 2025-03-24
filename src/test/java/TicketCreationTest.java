import org.junit.jupiter.api.Test;
import org.unict.dieei.domain.Products;
import org.unict.dieei.domain.Ticket;
import org.unict.dieei.domain.User;
import org.unict.dieei.persistence.*;
import org.unict.dieei.service.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketCreationTest extends BaseTest {

    @Test
    void testCreateTicket_Successful() {
        UserDAO userDAO = new UserDAO(em);
        ProductsDAO productsDAO = new ProductsDAO(em);
        TicketDAO ticketDAO = new TicketDAO(em);
        NotificationDAO notificationDAO = new NotificationDAO(em);
        AuthorizationDAO authorizationDAO = new AuthorizationDAO(em);

        AuthorizationService authorizationService = new AuthorizationService(authorizationDAO);
        UserService userService = new UserService(userDAO, authorizationService);
        NotificationService notificationService = new NotificationService(notificationDAO, em);

        ProductsService productsService = new ProductsService(productsDAO);
        TicketService ticketService = new TicketService(em, ticketDAO, productsService, userService, notificationService);

        User user = userService.findById(3);

        List<Products> productsList = productsDAO.getAllProducts();
        assertFalse(productsList.isEmpty(), "Errore: Nessun prodotto trovato nel database!");

        Products product = productsList.get(0);

        Ticket ticket = ticketService.createTicket("Problema nel login in https", "Non funziona il login se Coswin è in https", user, product.getId());

        assertNotNull(ticket);
        assertEquals("Problema nel login in https", ticket.getTitle());
    }

}
