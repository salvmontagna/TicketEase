import org.junit.jupiter.api.Test;
import org.unict.dieei.domain.User;
import org.unict.dieei.persistence.AuthorizationDAO;
import org.unict.dieei.persistence.UserDAO;
import org.unict.dieei.service.AuthorizationService;
import org.unict.dieei.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserRegisterTest extends BaseTest {

    @Test
    void testRegisterUser_SuccessfulForClient() {
        UserDAO userDAO = new UserDAO(em);
        AuthorizationDAO authorizationDAO = new AuthorizationDAO(em);

        AuthorizationService authorizationService = new AuthorizationService(authorizationDAO);

        UserService userService = new UserService(userDAO, authorizationService);

        userService.registerUser("mariorossi", "mario.rossi@example.com", "password123", 2, null, null);

        User user = userDAO.findByEmail("mario.rossi@example.com");
        assertNotNull(user);
        assertEquals("mariorossi", user.getName());
    }

    @Test
    void testRegisterUser_FailsForUnauthorizedAdmin() {

        UserDAO userDAO = new UserDAO(em);
        AuthorizationDAO authorizationDAO = new AuthorizationDAO(em);

        AuthorizationService authorizationService = new AuthorizationService(authorizationDAO);

        UserService userService = new UserService(userDAO, authorizationService);

        userService.registerUser("SalvatoreMontagna", "o46001695@studium.unict.it", "securePass", 0, "MNTGNN65H15C351A", "chiaveSbagliata");

        User user = userDAO.findByEmail("admin.rossi@example.com");
        assertNull(user);
    }
}
