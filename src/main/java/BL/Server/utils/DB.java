package BL.Server.utils;

import BL.Server.ServerSystem;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Serfati
 * Description: This class contains the methods for connecting to the database, getting data,
 * updating the database, preparing statements, executing prepared statements,
 * starting transactions, committing transactions, and rolling back transactions
 * @version Id: 1.0
 **/
public class DB implements Serializable {

    @PersistenceUnit
    protected static EntityManagerFactory emf;
    private static final long serialVersionUID = 1L;
    public final static Logger logger = Logger.getLogger(DB.class);
    private static DB instance;

    /**
     * @param _emf- Entity Manager Factory object
     * @return the instance of this facade.
     */
    public static DB getDataBaseInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DB();
            logger.log(Level.INFO, "DBFacade launched");
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
    public static void persist(Object entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(entity);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Persists the list of objects in the parameter to the database.
     *
     * @param entities the list tof entities to persist.
     */
    public static void persistAll(List<?> entities) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            entities.forEach(em::persist);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Remove the object passes as parameter from the database.
     *
     * @param entity the entity to remove.
     */
    public static void remove(Object entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Object mergedEntity = em.merge(entity);
            em.remove(mergedEntity);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }


    /**
     * Updates the object passes as parameter from the database.
     *
     * @param entity the entity to update.
     */
    public static Object merge(Object entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            entity = em.merge(entity);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
        return entity;
    }

    // Query methods -------------------------------------------------------------------------------

    /**
     * Builds a fixed query from variables
     *
     * @param ql   - sql query with parameters fields
     * @param args - the parameters
     * @return fixed query
     */
    private static Query newQuery(String ql, Object... args) {
        Query query = emf.createEntityManager().createQuery(ql);
        IntStream.range(0, args.length).forEach(i -> query.setParameter(i, args[i]));
        return query;
    }

    /**
     * Remove the list of objects from the database.
     *
     * @param entities the list of objects to remove from the database.
     */
    public static void removeAll(List<?> entities) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            for(Object entity : entities) em.remove(entity);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /* Simple manual tests ------------------------------------------------------------------------------- */
    // tested - CREATE TRUNCATE INSERT SELECT DROP CONNECTION; left - UPDATE namedQuery Parameters
    public static void main(String[] args) {
        emf = ServerSystem.createEntityManagerFactory(ServerSystem.DbSelector.DEV, ServerSystem.Strategy.DROP_AND_CREATE);
        EntityTransaction txn;
        EntityManager em = emf.createEntityManager();
        txn = em.getTransaction();
        txn.begin();
        em.createNativeQuery("CREATE TABLE IF NOT EXISTS Standings (id INTEGER PRIMARY KEY, name VARCHAR(50) NOT NULL)").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE Standings").executeUpdate(); // clears the table content
        em.createNativeQuery("INSERT INTO Standings VALUES (1,'one'),(2,'two'),(3,'three'),(4,'four')").executeUpdate();
        Query q = em.createNativeQuery("SELECT a.id, a.name FROM Standings a");
        // Show Result
        List<Object[]> ids = q.getResultList();
        for(Object[] a : ids) System.out.println("Standings ~ "+a[0]+":"+a[1]);
        em.createNativeQuery("DROP TABLE IF EXISTS Standings").executeUpdate(); // drops the table
        txn.commit();

        em.close();
        emf.close();
    }

    /**
     * @param type
     * @return
     */
    public int getNumberOfItems(Object type) {
        EntityManager em = emf.createEntityManager();
        TypedQuery q = em.createQuery("select o from"+type.getClass()+"o", type.getClass());
        em.close();
        return q.getResultList().size();
    }
}