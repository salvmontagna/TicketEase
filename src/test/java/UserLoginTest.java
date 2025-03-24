import org.junit.jupiter.api.Test;
import org.unict.dieei.domain.User;
import org.unict.dieei.persistence.AuthorizationDAO;
import org.unict.dieei.persistence.UserDAO;
import org.unict.dieei.service.AuthorizationService;
import org.unict.dieei.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginTest extends BaseTest {

    @Test
    void testLogin_Successful() {
        UserDAO userDAO = new UserDAO(em);
        AuthorizationDAO authorizationDAO = new AuthorizationDAO(em);

        AuthorizationService authorizationService = new AuthorizationService(authorizationDAO);

        UserService userService = new UserService(userDAO, authorizationService);

        User loggedInUser = userService.loginUser("cliente", "cliente");
        assertNotNull(loggedInUser);
        assertEquals("cliente", loggedInUser.getName());
    }

    @Test
    void testLogin_FailsForWrongPassword() {
        UserDAO userDAO = new UserDAO(em);
        AuthorizationDAO authorizationDAO = new AuthorizationDAO(em);

        AuthorizationService authorizationService = new AuthorizationService(authorizationDAO);

        UserService userService = new UserService(userDAO, authorizationService);

        User loggedInUser = userService.loginUser("mario.rossi@example.com", "wrongPass");
        assertNull(loggedInUser);
    }

}
