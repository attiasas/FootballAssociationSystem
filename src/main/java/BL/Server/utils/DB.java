package BL.Server.utils;

import BL.Server.ServerSystem;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Level;

/**
 * Description: A JPA service that'll handle interacting with the database for all User
 * interactions. contains the methods for connecting to the database, getting data, updating the
 * database, preparing statements, executing prepared statements, starting transactions, committing
 * transactions, and rolling back transactions
 *
 * @author Serfati
 * @version Id: 1.0
 **/
@Log4j /* install lombok plugin in intellij */
public class DB implements Serializable {

  @PersistenceContext
  private static EntityManager em;
  private static DB instance;

  /**
   * Singleton instance for all jpa service
   *
   * @param emf- Entity Manager Factory object
   * @return the instance of this facade.
   */
  public static DB getDataBaseInstance(EntityManagerFactory emf) {
    if (instance == null && emf != null) {
      log.removeAllAppenders();
      em = emf.createEntityManager();
      instance = new DB();
      log.log(Level.INFO,
          "Database launched and alive on jdbc:mysql://localhost:3306/sportify");
    }
    return instance;
  }

    /*
     --------------------------------------------------------------------

                        persistence GENERAL methods

      --------------------------------------------------------------------
    */

  /**
   * Generic method to persist the given object in the DB
   *
   * @param entity Object entity
   * @return True if successful creation of object by JPA else false
   */
  public static boolean persist(Object entity) {
    em.getTransaction().begin();
    try {
      em.persist(entity);
      em.flush();

      //End Transaction
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
      log.log(Level.WARN, "persist failed");
      return false;
    } finally {
      em.close();
      log.log(Level.INFO, "object persisted");
    }
    return true;
  }

  /**
   * Persists the list of objects in the parameter to the database.
   *
   * @param entities the list tof entities to persist.
   */
  public static boolean persistAll(List<?> entities) {
    em.getTransaction().begin();
    try {
      entities.forEach(em::persist);
    } catch (Exception e) {
      em.getTransaction().rollback();
      return false;
    }
    return true;
  }

  /**
   * Generic method to delete an object from DB
   *
   * @param entity The object entity to be deleted
   * @return True iff successful deletion by JPA else false
   */
  public static boolean remove(Object entity) {
    em.getTransaction().begin();
    try {
      //Re-attach the entity if not attached
      if (!em.contains(entity)) {
        entity = em.merge(entity);
      }
      em.remove(entity);
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
      log.log(Level.WARN, "remove failed");
      return false;
    } finally {
      em.close();
      log.log(Level.INFO, "object removed");
    }
    return true;
  }

  /**
   * Remove the list of objects from the database.
   *
   * @param entities the list of objects to remove from the database.
   */
  public static boolean removeAll(List<?> entities) {
    em.getTransaction().begin();
    try {
      entities.forEach(em::remove);
    } catch (Exception e) {
      em.getTransaction().rollback();
      return false;
    } finally {
      em.close();
    }
    return true;
  }


  /**
   * Updates the object passes as parameter from the database.
   *
   * @param entity the entity to update.
   */
  public static boolean merge(Object entity) {
    em.getTransaction().begin();
    try {
      entity = em.merge(entity);
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
      log.log(Level.WARN, "merge failed");
      return false;
    } finally {
      em.close();
      log.log(Level.INFO, "object merged");
    }
    return true;
  }

  public static boolean update(String queryName, Object data) {
    em.getTransaction().begin();
    Map<String, Object> map = (Map<String, Object>) data;
    Query fixed = em.createNamedQuery(queryName);
    try {
      map.forEach(fixed::setParameter);
      fixed.executeUpdate();
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
      log.log(Level.WARN, "update failed");
      return false;
    } finally {
      em.close();
      log.log(Level.INFO, "object updated");
    }
    return true;
  }

  public static List query(String queryName, Object data) {
    List resultList = null;
    em.getTransaction().begin();
    HashMap<String, Object> map = (HashMap<String, Object>) data;
    Query fixed = em.createNamedQuery(queryName);
    try {
      fixed = getParameteredQuery(fixed, map);
      resultList = fixed.getResultList();
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
      log.log(Level.WARN, "query failed");
      return null;
    } finally {
      em.close();
      log.log(Level.INFO, "query results returned");
    }
    return resultList;
  }

  public static Query getParameteredQuery(Query q, HashMap<String, Object> filterMap) {
    if (filterMap != null && !filterMap.isEmpty()) {
      filterMap.keySet().forEach(column -> {
        if (filterMap.get(column) instanceof String) {
          String value = (String) filterMap.get(column);
          q.setParameter(column, value.toUpperCase() + "%");
        } else {
          q.setParameter(column, filterMap.get(column));
        }
      });
    }
    return q;
  }

  // Query methods -------------------------------------------------------------------------------

  //  /* Simple manual tests ------------------------------------------------------------------------------- */
//  // tested - CREATE TRUNCATE INSERT SELECT DROP CONNECTION  UPDATE namedQuery Parameters
  public static void main(String[] args) {
    ServerSystem
        .createEntityManagerFactory(ServerSystem.DbSelector.TEST, ServerSystem.Strategy.NONE);
    EntityTransaction txn;
    txn = em.getTransaction();
    txn.begin();
    em.createNativeQuery(
        "CREATE TABLE IF NOT EXISTS Standings (id INTEGER PRIMARY KEY, name VARCHAR(50) NOT NULL)")
        .executeUpdate();
    em.createNativeQuery("TRUNCATE TABLE Standings")
        .executeUpdate(); // clears the table content
    em.createNativeQuery(
        "INSERT INTO Standings VALUES (1,'one'),(2,'two'),(3,'three'),(4,'four')")
        .executeUpdate(); // insert values
    em.createNativeQuery("UPDATE Standings SET name='one updated' WHERE id = 1")
        .executeUpdate();
    Query q = em.createNativeQuery("SELECT a.id, a.name FROM Standings a");
    List<Object[]> ids = q.getResultList();
    for (Object[] a : ids) {
      System.out.println("Standings ~ " + a[0] + ":" + a[1]);// Show Result
    }
    em.createNativeQuery("DROP TABLE IF EXISTS Standings").executeUpdate(); // drops the table

//        Query q = em.createNativeQuery("SELECT s.name, s.capacity FROM Stadium s");
//        List<Object[]> ids = q.getResultList();
    for (Object[] s : ids) {
      System.out.println("Stadium ~ " + s[0] + ":" + s[1]);// Show Result
    }

//        HashMap<String,Object> map = new HashMap<>() ;
//        map.put("name", "anfield");
//        query("stadiumByName", map);

    txn.commit();

    em.close();
  }

  /**
   * Generic method to get a particular entity from the DB
   *
   * @param object String to identify which entity we need to get
   * @param id     The integer value id
   * @return a particular entity from the DB
   */
  public Object getEntity(String object, int id) {
    StringBuilder queryString = new StringBuilder("SELECT o FROM ");
    queryString.append(object);
    queryString.append(" o WHERE o.id = ");
    queryString.append(id);

    //Begin Transaction
    em.getTransaction().begin();

    TypedQuery<Object> query = em.createQuery(queryString.toString(), Object.class);
    return query.getSingleResult();
  }
}