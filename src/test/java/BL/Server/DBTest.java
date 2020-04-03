package BL.Server;

import BL.Server.utils.DB;
import junit.framework.TestCase;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;

/**
 * Description:     <p>Tests for DB with persistence</p>
 * ID:              X
 *
 * @author Serfati
 * @version 1.0
 **/

public class DBTest extends TestCase {

    private static EntityManagerFactory emf;
    private static DB facade;
    private static Connection conn;

    @BeforeAll
    public static void setUpClass() {
        emf = ServerSystem.createEntityManagerFactory(
                "sportify",
                "jdbc:mysql://localhost:3306/sportify_test",
                "root",
                "",
                ServerSystem.Strategy.CREATE);
        facade = DB.getDataBaseInstance(emf);
        conn = DB.createConnection(ServerSystem.DbSelector.TEST);
        System.out.println("BeforeAll done");
    }


    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        System.out.println("Before Each done");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // TODO
            em.getTransaction().commit();
        } finally {
            em.close();
        }

    }


    //Remove any data after each test was run
    @AfterEach
    public void tearDown() {
        System.out.println("After Each done");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // TODO
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test INSERT")
    void dbInsertTest() {
        assertEquals(true, 1 == 1);
    }

    @Test
    @Order(2)
    @DisplayName("Test Retrival")
    void dbRetrivalTest() {
        assertEquals(1, 2-1);
    }
}