import org.junit.jupiter.api.Test;
import org.unict.dieei.domain.Notification;
import org.unict.dieei.domain.Ticket;
import org.unict.dieei.persistence.*;
import org.unict.dieei.service.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationsTest extends BaseTest {

    @Test
    void testSendTechNotification() {

        UserService userService = new UserService(new UserDAO(em), new AuthorizationService(new AuthorizationDAO(em)));
        NotificationService notificationService = new NotificationService(new NotificationDAO(em), em);
        ProductsService productsService = new ProductsService(new ProductsDAO(em));
        TicketService ticketService = new TicketService(em, new TicketDAO(em), productsService, userService, notificationService);

        Ticket ticket = ticketService.getTicketById(7);
        notificationService.sendTechNotification(3, ticket.getId(), em);

        List<Notification> notifications = notificationService.getNotifications(3);

        assertNotNull(notifications);
        assertFalse(notifications.isEmpty());
        assertTrue(notifications.get(0).getMessage().contains("Ti è stato assegnato un nuovo ticket"));

    }

    @Test
    void testSendClientNotification() {

        UserService userService = new UserService(new UserDAO(em), new AuthorizationService(new AuthorizationDAO(em)));
        NotificationService notificationService = new NotificationService(new NotificationDAO(em), em);
        ProductsService productsService = new ProductsService(new ProductsDAO(em));
        TicketService ticketService = new TicketService(em, new TicketDAO(em), productsService, userService, notificationService);

        Ticket ticket = ticketService.getTicketById(6);
        notificationService.sendClientNotification(ticket);

        List<Notification> notifications = notificationService.getNotifications(ticket.getCreatedUser().getId());

        assertNotNull(notifications);
        assertFalse(notifications.isEmpty());
        assertTrue(notifications.get(0).getMessage().contains("Il tuo ticket con ID " + ticket.getId() + " è stato chiuso."));
    }

    @Test
    void testGetNotifications() {

        NotificationService notificationService = new NotificationService(new NotificationDAO(em), em);

        List<Notification> notifications = notificationService.getNotifications(2);

        System.out.println(notifications);

        assertNotNull(notifications);
        assertFalse(notifications.isEmpty());
    }
}
