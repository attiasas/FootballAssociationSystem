package BL.Server;

import BL.Server.utils.DB;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Description:     X
 * ID:              X
 **/

public class DBTest {

    private static EntityManagerFactory emf;
    private static DB facade;

    @BeforeAll
    public static void setUpClass() {
        emf = ServerSystem.createEntityManagerFactory(
                "sportify",
                "jdbc:mysql://localhost:3306/sportify",
                "root",
                "",
                ServerSystem.Strategy.DROP);
        facade = DB.getFacade(emf);
    }

//    @BeforeAll
//    public static void setUpClassV2() {
//        emf = ServerSystem.createEntityManagerFactory(ServerSystem.DbSelector.TEST, ServerSystem.Strategy.DROP);
//        facade = DB.getFacade(emf);
//    }


    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
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
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // TODO
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // TODO
    @Test
    public void testAFacadeMethod() {
        System.out.println("success");
    }
}