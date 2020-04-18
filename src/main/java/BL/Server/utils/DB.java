package BL.Server.utils;

import BL.Server.ServerSystem;
import DL.Team.Assets.Stadium;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Level;

/**
 * @author Serfati Description: This class contains the methods for connecting to the database,
 * getting data, updating the database, preparing statements, executing prepared statements,
 * starting transactions, committing transactions, and rolling back transactions
 * @version Id: 1.0
 **/
@Log4j /* install lombok plugin in intellij */
public class DB implements Serializable {

//    @PersistenceUnit
//    protected static EntityManagerFactory emf;
//    private static DB instance;
//
//    /**
//     * @param _emf- Entity Manager Factory object
//     * @return the instance of this facade.
//     */
//    public static DB getDataBaseInstance(EntityManagerFactory _emf) {
//        if (instance == null) {
//            log.removeAllAppenders();
//            emf = _emf;
//            instance = new DB();
//            log.log(Level.INFO,
//                "Database launched and alive on jdbc:mysql://localhost:3306/sportify");
//        }
//        return instance;
//    }
//
//    /*
//     --------------------------------------------------------------------
//
//                        persistence GENERAL methods
//
//      --------------------------------------------------------------------
//    */
//
//    /**
//     * Persists the object provided as a parameter to the database.
//     *
//     * @param entity the entity to persists.
//     */
//    public static boolean persist(Object entity) {
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        try {
//            em.persist(entity);
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            em.getTransaction().rollback();
//            log.log(Level.WARN, "persist failed");
//            return false;
//        } finally {
//            em.close();
//            log.log(Level.INFO, "object persisted");
//        }
//        return true;
//    }
//
//    /**
//     * Persists the list of objects in the parameter to the database.
//     *
//     * @param entities the list tof entities to persist.
//     */
//    public static boolean persistAll(List<?> entities) {
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        try {
//            entities.forEach(em::persist);
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            em.getTransaction().rollback();
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * Remove the object passes as parameter from the database.
//     *
//     * @param entity the entity to remove.
//     */
//    public static boolean remove(Object entity) {
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        try {
//            em.remove(entity);
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            em.getTransaction().rollback();
//            log.log(Level.WARN, "remove failed");
//            return false;
//        } finally {
//            em.close();
//            log.log(Level.INFO, "object removed");
//        }
//        return true;
//    }
//
//    /**
//     * Remove the list of objects from the database.
//     *
//     * @param entities the list of objects to remove from the database.
//     */
//    public static boolean removeAll(List<?> entities) {
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        try {
//            for (Object entity : entities) {
//                em.remove(entity);
//            }
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            em.getTransaction().rollback();
//            return false;
//        } finally {
//            em.close();
//        }
//        return true;
//    }
//
//
//    /**
//     * Updates the object passes as parameter from the database.
//     *
//     * @param entity the entity to update.
//     */
//    public static boolean merge(Object entity) {
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        try {
//            entity = em.merge(entity);
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            em.getTransaction().rollback();
//            log.log(Level.WARN, "merge failed");
//            return false;
//        } finally {
//            em.close();
//            log.log(Level.INFO, "object merged");
//        }
//        return true;
//    }
//
//    public static boolean update(String queryName, Object data) {
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        Map<String, Object> map = (Map<String, Object>) data;
//        Query fixed = em.createNamedQuery(queryName);
//        try {
////            for (Map.Entry<String, Object> entry : map.entrySet()) {
////                String k = entry.getKey();
////                Object v = entry.getValue();
////                fixed = em.createNamedQuery(queryName).setParameter(k,v);
////            }
//            map.forEach(fixed::setParameter);
//            fixed.executeUpdate();
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            em.getTransaction().rollback();
//            log.log(Level.WARN, "update failed");
//            return false;
//        } finally {
//            em.close();
//            log.log(Level.INFO, "object updated");
//        }
//        return true;
//    }
//
//    public static List query(String queryName, Object data) {
//        EntityManager em = emf.createEntityManager();
//        List resultList = null;
//        em.getTransaction().begin();
//        Map<String, Object> map = (Map<String, Object>) data;
//        Query fixed = em.createNamedQuery(queryName);
//        try {
//            map.forEach(fixed::setParameter);
//            System.out.println("\n\nrunning query: " + fixed);
//            resultList = fixed.getResultList();
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            em.getTransaction().rollback();
//            log.log(Level.WARN, "query failed");
//            return null;
//        } finally {
//            em.close();
//            log.log(Level.INFO, "query results returned");
//        }
//        return resultList;
//    }
//
//    // Query methods -------------------------------------------------------------------------------
//
//    /**
//     * Builds a fixed query from variables
//     *
//     * @param ql   - sql query with parameters fields
//     * @param args - the parameters
//     * @return fixed query
//     */
//    public static Query newQuery(String ql, Object... args) {
//        Query query = emf.createEntityManager().createQuery(ql);
//        IntStream.range(0, args.length).forEach(i -> query.setParameter(i, args[i]));
//        return query;
//    }
//
}