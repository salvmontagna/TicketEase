import org.junit.jupiter.api.Test;
import org.unict.dieei.domain.Products;
import org.unict.dieei.domain.Ticket;
import org.unict.dieei.domain.User;
import org.unict.dieei.persistence.*;
import org.unict.dieei.service.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TicketUpdateStatusTest extends BaseTest{

    @Test
    void testUpdateTicketStatusToInProgress() {

        AuthorizationService authorizationService = new AuthorizationService(new AuthorizationDAO(em));
        UserService userService = new UserService(new UserDAO(em), authorizationService);
        NotificationService notificationService = new NotificationService(new NotificationDAO(em), em);
        ProductsService productsService = new ProductsService(new ProductsDAO(em));
        TicketService ticketService = new TicketService(em, new TicketDAO(em), productsService, userService, notificationService);
        TicketStatusService ticketStatusService = new TicketStatusService(new TicketStatusDAO(em), ticketService, notificationService);

        User tecnico = userService.findById(2);

        Ticket ticket = ticketService.getTicketById(7);

        String description = "Il tecnico ha iniziato a lavorare sul ticket.";
        ticketStatusService.updateTicketStatus(ticket.getId(), "in_progress", tecnico.getId(), description);

        assertNotNull(ticket);
        assertEquals("in_progress", ticket.getStatus());
    }


    @Test
    void testUpdateTicketStatusToClosed() {
        UserService userService = new UserService(new UserDAO(em), new AuthorizationService(new AuthorizationDAO(em)));
        NotificationService notificationService = new NotificationService(new NotificationDAO(em), em);
        ProductsService productsService = new ProductsService(new ProductsDAO(em));
        TicketService ticketService = new TicketService(em, new TicketDAO(em), productsService, userService, notificationService);
        TicketStatusService ticketStatusService = new TicketStatusService(new TicketStatusDAO(em), ticketService, notificationService);

        String description = "Ticket chiuso dal tecnico.";

        Ticket ticket = ticketService.getTicketById(6);

        ticketStatusService.updateTicketStatus(ticket.getId(), "closed", 2, description);

        assertNotNull(ticket);
        assertEquals("closed", ticket.getStatus());
    }


}
