import org.junit.jupiter.api.Test;
import org.unict.dieei.domain.TicketStatus;
import org.unict.dieei.persistence.*;
import org.unict.dieei.service.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketStatusHistoryTest extends BaseTest {

    @Test
    void testGetTicketHistory() {

        AuthorizationService authorizationService = new AuthorizationService(new AuthorizationDAO(em));
        UserService userService = new UserService(new UserDAO(em), authorizationService);
        NotificationService notificationService = new NotificationService(new NotificationDAO(em), em);
        ProductsService productsService = new ProductsService(new ProductsDAO(em));
        TicketService ticketService = new TicketService(em, new TicketDAO(em), productsService, userService, notificationService);
        TicketStatusService ticketStatusService = new TicketStatusService(new TicketStatusDAO(em), ticketService, notificationService);

        int ticketId = 7;

        List<TicketStatus> history = ticketStatusService.getTicketHistory(ticketId);

        assertNotNull(history);
        assertFalse(history.isEmpty());

        System.out.println(history);

        history.forEach(status -> assertEquals(ticketId, status.getTicket().getId()));
    }

}
