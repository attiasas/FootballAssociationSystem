package BL.Server.utils;

import BL.Server.ServerSystem;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.*;
import java.util.Collection;
import java.util.List;

/**
 * @author Serfati
 * Description:    This class contains the methods for connecting to the database, getting data,
 * updating the database, preparing statements, executing prepared statements,
 * starting transactions, committing transactions, and rolling back transactions
 **/

public class DB implements Serializable {

    @PersistenceUnit
    protected static EntityManagerFactory emf;
    private static final long serialVersionUID = 1L;
    public final static Logger logger = Logger.getLogger(DB.class);
    private static DB instance;
    private static Connection conn;

    /**
     * Private constructor.
     */
    private DB() {
        // empty
    }

    /**
     * @param _emf- Entity Manager Factory object
     * @return the instance of this facade.
     */
    public static DB getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DB();
            logger.log(Level.INFO, "DBFacade launched");
        }
        return instance;
    }

    /*---------------- persistence GENERAL methods -----------------------*/
    public static void persist(Object entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
    }

    public static void remove(Object entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Object mergedEntity = em.merge(entity);
        em.remove(mergedEntity);
        em.getTransaction().commit();
        em.close();
    }

    public static Object merge(Object entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        entity = em.merge(entity);
        em.getTransaction().commit();
        em.close();
        return entity;
    }

    public static Object update(Object aggregate) {
        EntityManager em = emf.createEntityManager();
        Object update = em.find(aggregate.getClass(), aggregate);
        em.getTransaction().begin();
        update = aggregate;
        em.getTransaction().commit();
        em.close();
        return update;
    }

    /**
     * MySQL: jdbc:mysql://<HOST>:<PORT>/<DATABASE_NAME>
     *
     * @param dbType
     * @return
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

    public boolean deleteAll(Collection<Object> aggregates) {
        aggregates.forEach(DB::remove);
        return true;
    }

    public int getNumberOfItems(Object type) {
        EntityManager em = emf.createEntityManager();
        TypedQuery q = em.createQuery("select o from"+type.getClass()+"o", type.getClass());
        em.close();
        return q.getResultList().size();
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

    public static EntityManager createEM() {
        return emf.createEntityManager();
    }

    public void close() {
        EntityManager em = emf.createEntityManager();
        em.close();
        emf.close();
    }

    /*---------------- native database GENERALLY methods-----------------------*/

    /**
     * Clears given table.
     *
     * @param tableName the table to clear
     * @throws Exception to JUnit
     */
    public static void clearTable(String tableName) throws Exception {
        executeUpdate("TRUNCATE TABLE "+tableName);
    }

    public static void resetAllTables(boolean flag) throws Exception {
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

    //Simple manual test
    public static void main(String[] args) throws Exception {
        DB db = getFacade(ServerSystem.createEntityManagerFactory(ServerSystem.DbSelector.DEV, ServerSystem.Strategy.DROP_AND_CREATE));
        EntityTransaction txn = null;
        EntityManager entityManager = createEM();
        txn = entityManager.getTransaction();
        txn.begin();
        entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS Standings (`id` INTEGER PRIMARY KEY)").executeUpdate();

        entityManager.createNativeQuery("INSERT INTO  Standings VALUE (15)").executeUpdate();
        txn.commit();

        txn.commit();
        entityManager.createNativeQuery("DROP TABLE IF EXISTS Standings").executeUpdate();
        txn.begin();


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

    public boolean updateAll(Collection<Object> aggregates) {
        aggregates.forEach(DB::update);
        return true;
    }

    public List executeNamedQuery(String namedQuery) {
        return emf.createEntityManager().createNamedQuery(namedQuery).getResultList();
    }

    public List getQueryResult(String sqlQuery) {
        return emf.createEntityManager().createQuery(sqlQuery).getResultList();
    }
}


/* -------------------------------------------------------------------- //

                               DRAFT

  -------------------------------------------------------------------- */


//    public boolean loginUser(String du_username, String du_password) {
//        EntityManager em = emf.createEntityManager();
//        User found = em.find(User.class, du_username);
//        if (found != null && du_password.equals(found.getPassword())) {
//            logger.log(Level.INFO, "User authentication - pass");
//            return true;
//        }
//        em.close();
//        return false;
//    }
//
////    public boolean existUser(User u) {
////        EntityManager em = emf.createEntityManager();
////        User found = em.find(User.class, u.getUsername());
////        em.close();
////        return found != null;
////    }
//
////    public List<User> selectAllUsers() {
////        return emf.createEntityManager().createNamedQuery("User.findAll").getResultList();
////    }
//
//    public User selectUserByName(String s) {
//        return emf.createEntityManager().find(User.class, s);
//    }
//
//    /*---------------- TEAMS -----------------------*/
//    public boolean existTeam(Team t) {
//        EntityManager em = emf.createEntityManager();
//        Team found =null;
//        //Team found = em.find(Team.class, t.getName());
//        em.close();
//        return found != null;
//    }
//
////    public static Team findTeam(int aliasName) {
////        EntityManager em = emf.createEntityManager();

//List<Team> Teams = (List<Team>) em.createNamedQuery("Team.findByAlias").setParameter("alias", aliasName).getResultList();
////        em.close();
////        return Teams.size() == 0 ? null : Teams.get(0);
////    }
//
//    public boolean insertTeam(Team t){
//        if(!existTeam(t)){
//            EntityManager em = emf.createEntityManager();
//            em.persist(t);
//            em.close();
//            return true;
//        }
//        return false;
//    }
//
//    public Team findTeamById(long id) {
//        EntityManager em = emf.createEntityManager();
//        return em.find(Team.class, id);
//    }
//
//    /*---------------- PLAYER -----------------------*/
//    public User findPlayerByName(Player aggregate) {
//        EntityManager entityManager = emf.createEntityManager();
//        Player found = null;
//
//        entityManager.close();
//        return found;
//    }

