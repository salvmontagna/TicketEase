import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {
    protected static EntityManagerFactory emf;
    protected EntityManager em;

    @BeforeEach
    void setUp() {
        //emf = Persistence.createEntityManagerFactory("test-unit");
        //em = emf.createEntityManager();
        emf = Persistence.createEntityManagerFactory("ticket-ease");
        em = emf.createEntityManager();
    }

    @AfterEach
    void tearDown() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
}
