package BL.Server.utils;

import BL.Server.ServerSystem;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.*;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Serfati
 * Description:    This class contains the methods for connecting to the database, getting data,
 * updating the database, preparing statements, executing prepared statements,
 * starting transactions, committing transactions, and rolling back transactions
 * @version Id: 1
 **/
public class DB implements Serializable {

    @PersistenceUnit
    protected static EntityManagerFactory emf;
    private static final long serialVersionUID = 1L;
    public final static Logger logger = Logger.getLogger(DB.class);
    private static DB instance;
    private static Connection conn;


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
        }
    }

    //Simple manual tests
    public static void main(String[] args) {
        ServerSystem.createEntityManagerFactory(ServerSystem.DbSelector.DEV, ServerSystem.Strategy.DROP_AND_CREATE);
        EntityTransaction txn = null;
        EntityManager em = emf.createEntityManager();
        txn = em.getTransaction();
        txn.begin();
        em.createNativeQuery("CREATE TABLE IF NOT EXISTS Standings (`id` INTEGER PRIMARY KEY)").executeUpdate();
        em.createNativeQuery("INSERT INTO  Standings VALUE (17)").executeUpdate();

        //em.createNativeQuery("DROP TABLE IF EXISTS Standings").executeUpdate();

        txn.commit();


//        DB db = new DB();
//        createConnection(ServerSystem.DbSelector.DEV);
//        clearTable("Users");
//        createConnection(ServerSystem.DbSelector.TEST);
//        String sql  = "INSERT INTO Users VALUES ('avihaipp@gmail.com','admin','Avihai','Serfati','root')";
//        System.out.println(executeInsert(sql));


//        createConnection(ServerSystem.DbSelector.DEV);
//        executeUpdate("USE sportify");
//        createConnection(ServerSystem.DbSelector.DEV);
//        String sql  = "CREATE TABLE IF NOT EXISTS TESTER (`id` INTEGER PRIMARY KEY,`Div` VARCHAR(5) NOT NULL)";
//        executeUpdate(sql);

//        createConnection(ServerSystem.DbSelector.DEV);
//        String sql2  = "drop table TESTER;";
//        executeUpdate(sql2);

    }

    public int getNumberOfItems(Object type) {
        EntityManager em = emf.createEntityManager();
        TypedQuery q = em.createQuery("select o from"+type.getClass()+"o", type.getClass());
        em.close();
        return q.getResultList().size();
    }

    /*
     --------------------------------------------------------------------

                       native database GENERAL methods

     --------------------------------------------------------------------
     */

    public static Connection createConnection(ServerSystem.DbSelector dbType) {
        String driver = Settings.getPropertyValue("db.driver");
        String connection_str;
        String user;
        String pw;
        if (dbType != ServerSystem.DbSelector.TEST) {
            connection_str = Settings.getDEV_DBConnection();
            user = Settings.getPropertyValue("db.user");
            pw = Settings.getPropertyValue("db.password");
        } else {
            connection_str = Settings.getTEST_DBConnection();
            user = Settings.getPropertyValue("dbtest.user");
            pw = Settings.getPropertyValue("dbtest.password");
        }
        if (instance == null) {
            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(connection_str, user, pw);
                logger.log(Level.INFO, "native database launched");
            } catch(ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }


    /**
     * Execute the given sql update command.
     *
     * @param command the command
     * @throws Exception to JUnit
     */
    public static void executeUpdate(String command) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(command);
            ps.executeUpdate();

        } finally {
            DB.dispose(conn, ps, rs);
        }
    }

    /**
     * Execute the given sql insert command.
     *
     * @param command the command
     * @return the last id.
     * @throws Exception to JUnit
     */
    public static int executeInsert(String command) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(command);
            return ps.executeUpdate();
        } finally {
            DB.dispose(conn, ps, rs);
        }
    }

    /**
     * Clears given table.
     *
     * @param tableName the table to clear
     * @throws Exception to JUnit
     */
    public static void clearTable(String tableName) throws Exception {
        executeUpdate("TRUNCATE TABLE "+tableName);
    }

    /**
     * Dispose JDBC resources.
     *
     * @param conn the connection.
     * @param ps   the prepared statement.
     * @param rs   the result set.
     */
    public static void dispose(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null) try {
            rs.close();
        } catch(Exception ignored) {
        }
        if (ps != null) try {
            ps.close();
        } catch(Exception ignored) {
        }
        if (conn != null) try {
            conn.close();
        } catch(Exception ignored) {
        }
    }

    public List executeNamedQuery(String namedQuery) {
        return emf.createEntityManager().createNamedQuery(namedQuery).getResultList();
    }
}
