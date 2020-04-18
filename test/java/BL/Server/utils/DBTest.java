package BL.Server.utils;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import BL.Server.ServerSystem;
import BL.Server.ServerSystem.DbSelector;
import BL.Server.ServerSystem.Strategy;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Description:     <p>Tests for DB with persistence</p> ID:              X
 *
 * @author Serfati
 * @version 1.0
 **/

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class DBTest {

//  private static EntityManagerFactory emf;
//  private static EntityManager em;
//
//  @BeforeAll
//  public static void setUpClass() {
//    emf = ServerSystem.createEntityManagerFactory(
//        "sportify",
//        "jdbc:mysql://localhost:3306/sportify_test",
//        "root",
//        "",
//        Strategy.DROP_AND_CREATE);
//
//    System.out.println("setUpClass done");
//  }
//
//  //Clean up any data after test class
//  @AfterAll
//  public static void tearDownClass() {
//    em.getTransaction().begin();
//    em.createNativeQuery("DROP TABLE IF EXISTS Testing").executeUpdate();
//    em.getTransaction().commit();
//    em.close();
//    emf.close();
//  }
//
//  // Setup the DataBase in a known state BEFORE EACH TEST
//  @BeforeEach
//  public void setUpV1() {
//    emf = ServerSystem.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
//    DB.getDataBaseInstance(emf);
//    em = emf.createEntityManager();
//    em.getTransaction().begin();
//    em.createNativeQuery(
//        "CREATE TABLE IF NOT EXISTS Testing (id INTEGER PRIMARY KEY, name VARCHAR(50) NOT NULL)")
//        .executeUpdate();
//    em.createNativeQuery("TRUNCATE TABLE Testing").executeUpdate(); // clears the table content
//    em.createNativeQuery(
//        "INSERT INTO Testing VALUES (1,'one'),(2,'two'),(3,'three'),(4,'four'),(5,'five')")
//        .executeUpdate();
//    em.getTransaction().commit();
//  }
//
//  // Setup the DataBase in a known state BEFORE EACH TEST
//  @BeforeEach
//  public void setUpV2() {
//    em.getTransaction().begin();
//    //Mocks
//    em.createNamedQuery("Mock.deleteAllRows").executeUpdate();
//    em.persist(new Mock("one"));
//    em.persist(new Mock("two"));
//    em.persist(new Mock("three"));
//    em.persist(new Mock("four"));
//    em.persist(new Mock("five"));
//
//    em.getTransaction().commit();
//    em.getTransaction().begin();
//  }
//
//  //Remove any data after each test was run
//  @AfterEach
//  public void tearDown() {
//    em.getTransaction().commit();
//  }
//
//  /*
//      --- Persistence ---
//   */
//  @Test
//  @DisplayName("Test SELECT")
//  public void selectTest() {
//    List<Mock> mocks = em.createQuery("select m from Mock m").getResultList();
//    assertEquals(5, mocks.size(), "Expects 5 rows in the database");
//  }
//
//  @Test
//  @DisplayName("Test INSERT")
//  void InsertTest() {
//    DB.persist(new Mock("six"));
//    List<Mock> mocks = em.createQuery("select m from Mock m").getResultList();
//    assertEquals(6, mocks.size(), "Expects 6 rows in the database");
//  }
//
//  @Test
//  @DisplayName("Test REMOVE")
//  void RemoveTest() {
//    Mock mock = em.find(Mock.class, 1);
//    DB.remove(mock);
//    List<Mock> mocks = em.createQuery("select m from Mock m").getResultList();
//    assertEquals(4, mocks.size(), "Expects 0 rows in the database");
//  }
//
//  @Test
//  @DisplayName("Test MERGE")
//  void mergeTest() {
//    Mock mock = em.find(Mock.class, 1);
//    mock.setName("oneUpdated");
//    DB.merge(mock);
//    List<Mock> mc = em.createQuery("select m from Mock m where m.id = 1").getResultList();
//    assertEquals(mc.get(0).getName(), "oneUpdated");
//  }
//
//  @Test
//  @DisplayName("Test QUERY")
//  void queryTest() {
//    List<Mock> mc = em.createQuery("select m from Mock m where m.id = 2").getResultList();
//    assertEquals(mc.get(0).getName(), "two");
//  }
//
//  @Test
//  @DisplayName("Test NAMED QUERY")
//  void namedQueryTest() {
//    em.createNamedQuery("Mock.deleteAllRows").executeUpdate();
//    List<Mock> mocks = em.createNamedQuery("Mock.findAll").getResultList();
//    assertEquals(0, mocks.size(), "Expects 0 rows in the database");
//  }
//
//  @Test
//  @DisplayName("Test NAMED PARAM QUERY")
//  void paramNamedQuery2Test() {
//    Query query = em.createNamedQuery("Mock.ByID");
//    query.setParameter("id", 3);
//    List<Mock> mocks = query.getResultList();
//    assertEquals("three", mocks.get(0).getName(), "Expects to get the 3rd mock");
//  }
//
//  @Test
//  @DisplayName("Test EMPTY QUERY")
//  void emptyQueryTest() {
//    List<Mock> mocks = em.createQuery("select m from Mock m where m.id = 7").getResultList();
//    assertEquals(0, mocks.size(), "Expects 0 rows in the database");
//  }
//
//  @Test
//  @DisplayName("Test NAMED PARAM QUERY")
//  void paramNamedQueryTest() {
//    Query query = em.createNamedQuery("Mock.ByName");
//    query.setParameter("name", "four");
//    List<Mock> mocks = query.getResultList();
//    assertEquals(4, mocks.get(0).getId(), "Expects to get the 4th mock");
//  }
//
//  @Test
//  @DisplayName("Test NULL QUERY")
//  void nullQueryTest() {
//    Mock mock = em.find(Mock.class, 6);
//    assertNull(mock);
//  }
//
//  @Test
//  @DisplayName("Test FAILED MERGE")
//  void failedMergeTest() {
//    Mock mock = em.find(Mock.class, 6);
//    Exception exception = assertThrows(IllegalArgumentException.class, () -> em.merge(mock));
//
//    String expectedMessage = "attempt to create merge event with null entity";
//    String actualMessage = exception.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  /*
//  --- Native ---
//  */
//  @Test
//  @DisplayName("Test SELECT")
//  void dbSelectTest() {
//    /* insert values */
//    Query q = em.createNativeQuery("SELECT a.id, a.name FROM Testing a");
//    List<Object[]> testing = q.getResultList();
//      for (Object[] a : testing) {
//          System.out.println("Testing ~ " + a[0] + ":" + a[1]);// Show Result
//      }
//    assertEquals(5, testing.size(), "Expects 5 rows in the database");
//  }
//
//  @Test
//  @DisplayName("Test INSERT")
//  void dbInsertTest() {
//    /* insert values */
//    assertEquals(1, em.createNativeQuery("INSERT INTO Testing VALUE (6,'six')").executeUpdate());
//    Query q = em.createNativeQuery("SELECT a.id, a.name FROM Testing a");
//    List<Object[]> testing = q.getResultList();
//    assertEquals(6, testing.size(), "Expects 6 rows in the database");
//  }
//
//  @Test
//  @DisplayName("Test REMOVE")
//  void dbRemoveTest() {
//    assertEquals(1, em.createNativeQuery("DELETE FROM Testing WHERE id = 2").executeUpdate());
//    Query q = em.createNativeQuery("SELECT a.id, a.name FROM Testing a");
//    List<Object[]> testing = q.getResultList();
//    assertEquals(4, testing.size(), "Expects 4 rows in the database");
//  }
//
//
//  @Test
//  @DisplayName("Test UPDATE")
//  void dbUpdateTest() {
//    assertEquals(1,
//        em.createNativeQuery("UPDATE Testing SET name='one updated' WHERE id = 1").executeUpdate());
//  }
//
//  @Test
//  @DisplayName("Test QUERY")
//  void dbQueryTest() {
//    List<Mock> mc = em.createQuery("select m from Mock m where m.id = 2").getResultList();
//    assertEquals(mc.get(0).getName(), "two");
//  }
}