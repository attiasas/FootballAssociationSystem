package BL.Server;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * Description:
 **/
public class ServerSystemTest {

    @BeforeAll
    public static void setUpClass() {
        System.out.println("build setup");
    }

    @BeforeEach
    public void setUp() {
        System.out.println("build setup");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("build setup");
    }

    @Test
    public void testServerSystem() {
        System.out.println("success");
    }

}