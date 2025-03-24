import org.junit.jupiter.api.Test;
import org.unict.dieei.domain.Ticket;
import org.unict.dieei.persistence.*;
import org.unict.dieei.service.*;

import static org.junit.jupiter.api.Assertions.*;

class TicketAssignmentTest extends BaseTest {

    @Test
    void testAssignTicketToTechnician() {
        UserService userService = new UserService(new UserDAO(em), new AuthorizationService(new AuthorizationDAO(em)));
        NotificationService notificationService = new NotificationService(new NotificationDAO(em), em);
        ProductsService productsService = new ProductsService(new ProductsDAO(em));
        TicketService ticketService = new TicketService(em, new TicketDAO(em), productsService, userService, notificationService);

        ticketService.assignTicket(6, 2);

        Ticket ticket = ticketService.getTicketById(6);
        assertNotNull(ticket);
        assertEquals(2, ticket.getAssignedUser().getId());
    }


}
