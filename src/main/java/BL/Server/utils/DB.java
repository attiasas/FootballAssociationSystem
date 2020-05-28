package BL.Server.utils;

import BL.Server.ServerSystem;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Description: This class contains the methods for connecting to the database, getting data,
 * updating the database, preparing statements, executing prepared statements, starting
 * transactions, committing transactions, and rolling back transactions.
 *
 * @author Serfati
 * @version Id: 1.0
 **/
@Log4j(topic = "event") /* install lombok plugin in intellij */
public class DB implements Serializable {

    @PersistenceUnit
    private static EntityManagerFactory emf;
    private static DB instance;
//    public final static Logger log = LogManager.getLogger("error");

    /**
     * @param _emf- Entity Manager Factory object
     * @return the instance of this facade.
     */
    public static DB getDataBaseInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            log.removeAllAppenders();
            emf = _emf;
            instance = new DB();
            log.info("Database launched and alive on: " + Configuration.getDEV_DBConnection());
        }
        return instance;
    }

    /*
     --------------------------------------------------------------------

                        persistence GENERAL methods

      --------------------------------------------------------------------
    */

    /**
     * Persists the object provided as a parameter to the database.
     *
     * @param entity the entity to persists.
     */
    public static boolean persist(Object entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(entity);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.warn("persist failed" + e.getMessage());
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
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            entities.forEach(em::persist);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.warn("error - rolling back" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Remove the object passes as parameter from the database.
     *
     * @param entity the entity to remove.
     */
    public static boolean remove(Object entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
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
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            for (Object entity : entities) {
                em.remove(entity);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.warn("error - rolling back" + e.getMessage());
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
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            entity = em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.info("merge failed");
            return false;
        } finally {
            em.close();
            log.info("object merged");
        }
        return true;
    }

    /**
     * Updates the object passes by the query.
     *
     * @param queryName the name of the namedQuery
     * @param data      the update parameters
     * @return true if object updated false otherwise
     */
    public static boolean update(String queryName, Object data) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        HashMap<String, Object> map = (HashMap<String, Object>) data;
        Query fixed = em.createNamedQuery(queryName);
        try {
            fixed = getParameteredQuery(fixed, map);
            fixed.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.warn("update failed");
            return false;
        } finally {
            em.close();
            log.info("object updated");
        }
        return true;
    }

    /**
     * @param queryName the name of the namedQuery
     * @param data      the query parameters
     * @return a results list
     */
    public static List query(String queryName, Object data) {
        List resultList;
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        HashMap<String, Object> map = (HashMap<String, Object>) data;
        Query fixed = em.createNamedQuery(queryName);
        try {
            fixed = getParameteredQuery(fixed, map);
            resultList = fixed.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.warn("query failed");
            return null;
        } finally {
            em.close();
            log.info("query results returned");
        }
        return resultList;
    }

    // Query methods -------------------------------------------------------------------------------

    /**
     * Create a query with set of parameters represents by a map
     *
     * @param q         - the query to be filled
     * @param filterMap - parameters map <attribute , value>
     * @return - fixed query filled with the @params
     */
    public static Query getParameteredQuery(Query q, HashMap<String, Object> filterMap) {
        if (filterMap != null && !filterMap.isEmpty()) {
            filterMap.keySet().forEach(column -> {
                if (filterMap.get(column) instanceof String) {
                    String value = (String) filterMap.get(column);
//                    q.setParameter(column, value.toUpperCase() + "%");
                    q.setParameter(column, value);
                } else {
                    q.setParameter(column, filterMap.get(column));
                }
            });
        }
        return q;
    }

    public static void main(String[] args) {
        emf = ServerSystem
                .createEntityManagerFactory(ServerSystem.DbSelector.TEST, ServerSystem.Strategy.NONE);
        EntityTransaction txn;
        EntityManager em = emf.createEntityManager();
        txn = em.getTransaction();
        txn.begin();

        HashMap<String, Object> param = new HashMap<>();
        param.put("username", "dvir");
//        System.out.println(em.createNamedQuery("UserByUserName").setParameter("username", "dvir").getResultList().size());

//        Coach c = new Coach("dvir",true,new Fan("dvir",))
//        Team t = new Team("team test",true,false);
//        em.persist(t);
//        HashMap<String,Object> para = new HashMap<>();
//        para.put("name","team test");
//        List l = em.createNamedQuery("Team").getResultList();
//        System.out.println(l.size() + " | " + (!l.isEmpty() ? l.get(0) : "Empty"));
//        em.createNativeQuery(
//                "CREATE TABLE IF NOT EXISTS Standings (id INTEGER PRIMARY KEY, name VARCHAR(50) NOT NULL)")
//                .executeUpdate();
//        em.createNativeQuery("TRUNCATE TABLE Standings")
//                .executeUpdate(); // clears the table content
//        em.createNativeQuery(
//                "INSERT INTO Standings VALUES (1,'one'),(2,'two'),(3,'three'),(4,'four')")
//                .executeUpdate(); // insert values
//        em.createNativeQuery("UPDATE Standings SET name='one updated' WHERE id = 1")
//                .executeUpdate();
//        Query q = em.createNativeQuery("SELECT a.id, a.name FROM Standings a");
//        List<Object[]> ids = q.getResultList();
//        for (Object[] a : ids)
//            log.log(Level.INFO, "Standings ~ " + a[0] + ":" + a[1]);// Show Result
        //em.createNativeQuery("DROP TABLE IF EXISTS Standings").executeUpdate(); // drops the table
        txn.commit();
        em.close();
        emf.close();
    }

    /**
     * Generic method to get a particular entity from the DB
     *
     * @param object String to identify which entity we need to get
     * @param id     The integer value id
     * @return a particular entity from the DB
     */
    public Object getEntity(String object, int id) {
        EntityManager em = emf.createEntityManager();
        //Begin Transaction
        em.getTransaction().begin();

        String queryString = "SELECT o FROM " + object
                + " o WHERE o.id = "
                + id;
        TypedQuery<Object> query = em.createQuery(queryString, Object.class);
        return query.getSingleResult();
    }

//  /* Simple manual tests ------------------------------------------------------------------------------- */
//  // tested - CREATE TRUNCATE INSERT SELECT DROP CONNECTION  UPDATE namedQuery Parameters

    /**
     * Calculate the number of records in a table (class)
     *
     * @param entityClass the table to be checked
     * @return number of records
     */
    public Long getCount(Class entityClass) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery(
                "select count(e) from " + entityClass.getName() + " e ");
        return (Long) q.getSingleResult();
    }
}