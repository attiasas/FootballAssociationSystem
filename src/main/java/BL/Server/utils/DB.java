package BL.Server.utils;

import BL.Server.ServerSystem;
import DL.Team.Members.Player;
import DL.Team.Team;
import DL.Users.User;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Serfati
 * Description:    This class contains the methods for connecting to the database, getting data,
 *                 updating the database, preparing statements, executing prepared statements,
 *                 starting transactions, committing transactions, and rolling back transactions
 *
 **/

public class DB implements Serializable {

    private static final long serialVersionUID = 1L;
    public final static Logger logger = Logger.getLogger(DB.class);

    private static DB instance;

    @PersistenceUnit
    protected static EntityManagerFactory emf;


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


    /*---------------- GENERALLY -----------------------*/
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

    public Object update(Object aggregate) {
        EntityManager em = emf.createEntityManager();
        Object update = em.find(aggregate.getClass(),aggregate);
        em.getTransaction().begin();
        update = aggregate;
        em.getTransaction().commit();
        em.close();
        return update;
    }


    public boolean updateAll(Collection<Object> aggregates) {
        aggregates.forEach(this::update);
        return true;
    }

    public boolean deleteAll(Collection<Object> aggregates) {
        aggregates.forEach(DB::remove);
        return true;
    }

    public int getNumberOfItems(Object type) {
        EntityManager em = emf.createEntityManager();
        TypedQuery q = em.createQuery("select o from"+ type.getClass()+ "o", type.getClass());
        em.close();
        return q.getResultList().size();
    }

    public static EntityManager createEM() {
        return emf.createEntityManager();
    }

    public void close() {
        EntityManager em = emf.createEntityManager();
        em.close();
        emf.close();
    }

    /*---------------- USERS -----------------------*/
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

//    public boolean existUser(User u) {
//        EntityManager em = emf.createEntityManager();
//        User found = em.find(User.class, u.getUsername());
//        em.close();
//        return found != null;
//    }

//    public List<User> selectAllUsers() {
//        return emf.createEntityManager().createNamedQuery("User.findAll").getResultList();
//    }

    public User selectUserByName(String s) {
        return emf.createEntityManager().find(User.class, s);
    }

    /*---------------- TEAMS -----------------------*/
    public boolean existTeam(Team t) {
        EntityManager em = emf.createEntityManager();
        User found =null;
        //User found = em.find(Team.class, t.getName());
        em.close();
        return found != null;
    }

//    public static Team findTeam(int aliasName) {
//        EntityManager em = emf.createEntityManager();
//        List<Team> Teams = (List<Team>) em
//                .createNamedQuery("Team.findByAlias")
//                .setParameter("alias", aliasName)
//                .getResultList();
//        em.close();
//        return Teams.size() == 0 ? null : Teams.get(0);
//    }

    public boolean insertTeam(Team t){
        if(!existTeam(t)){
            EntityManager em = emf.createEntityManager();
            em.persist(t);
            em.close();
            return true;
        }
        return false;
    }

    public Team findTeamById(long id) {
        EntityManager em = emf.createEntityManager();
        return em.find(Team.class, id);
    }

    /*---------------- PLAYER -----------------------*/
    public User findPlayerByName(Player aggregate) {
        EntityManager entityManager = emf.createEntityManager();
        Player found = null;
        //TODO
        entityManager.close();
        return found;
    }

    //Simple manual test
    public static void main(String[] args) {
        getFacade(ServerSystem.createEntityManagerFactory(ServerSystem.DbSelector.DEV, ServerSystem.Strategy.DROP_AND_CREATE));
    }
}