package BL.Server;

import BL.Communication.IServerStrategy;
import BL.Communication.Server;
import BL.Communication.SystemRequest;
import BL.Communication.SystemRequest.Type;
import BL.Server.utils.DB;
import BL.Server.utils.Settings;
import DL.Administration.SystemManager;
import DL.Users.User;
import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Level;


/**
 * <p>
 * An utility class for managing an {@code EntityManagerFactory}.
 * </p>
 * <p>
 * Through the static methods provided by this class a reference to the default {@code
 * EntityManagerFactory} can be obtained. All code on the server that needs to access the database
 * can obtain a factory through this class.
 * <p>
 * MySQL: {@code jdbc:mysql://<HOST>:<PORT>/<DATABASE_NAME>}
 * </p>
 *
 * @author Serfati
 * @version Id: 1.0
 */
@Log4j
@Setter
@Getter
public class ServerSystem implements IServerStrategy {

  static final String PERSISTENCE_UNIT_NAME = "sportify";  /* The name of the persistence unit*/
  @PersistenceUnit
  static EntityManagerFactory emf;  /* The central, shared entity manager factory instance. */
  static Server server;
  private DB dataBase;

  /**
   * c`tor
   *
   * @param dbType   database type , choose between dev (deploy) and test (junit)
   * @param strategy database persistence strategy
   */
  public ServerSystem(DbSelector dbType, Strategy strategy) {
    final EntityManagerFactory entityManagerFactory = createEntityManagerFactory(dbType, strategy);
    dataBase = DB.getDataBaseInstance(entityManagerFactory);
    final boolean systemManagers =
        createEM().createNamedQuery("SystemManagers").getFirstResult() == 0;
    if (systemManagers) {
      signUp("admin", "admin@admin.com", "admin");
    }
  }

  /**
   * inserts a new user to db sends email to user generate token for user
   *
   * @param userName username of the user
   * @param email    email of the user
   * @param password password of the user
   * @return the system manager user
   */
  public User signUp(String userName, String email, String password) {

    if (password == null || email == null || userName == null || userName.equals("") || email
        .equals("") || password.equals("")) {
      log.log(Level.WARN, "invalid username or password");
      return null;
    }
    final boolean userExist = createEM().find(User.class, userName) != null;
    if (userExist) {
      log.log(Level.WARN, "username is already exist");
      return null;
    }
    //create the systemManager User
    String hashedPassword = DigestUtils.sha1Hex(password);
    SystemManager systemManager = new SystemManager(userName, email, hashedPassword);

    //insert the systemManager user to the DB
    DB.persist(systemManager);
    log.log(Level.INFO, "system manager created: " + userName);
    return systemManager;
  }

  /**
   * Demo the connection to external systems like Finance, Tax etc.
   */
  @SuppressWarnings("unused")
  private void initializeExternalSystems() {
    log.log(Level.INFO, "external systems integration completed");
  }

  /**
   * Initializes the given server and builds the endpoint Read default login servers from {@code
   * config.properties} Default url {@code https://localhost:5400}
   *
   * @throws java.io.IOException I/O exception
   */
  @SuppressWarnings("unused")
  private void initializeServer() throws java.io.IOException {
    int serverPort = Integer.parseInt(Settings.getPropertyValue("server.port"));
    int poolSize = Integer.parseInt(Settings.getPropertyValue("server.poolSize"));
    int listeningInterval = Integer.parseInt(Settings.getPropertyValue("server.listeningInterval"));
    server = new Server(serverPort, poolSize, listeningInterval, this);
    server.start();
    log.log(Level.INFO, "server is up and listen on port: " + serverPort);
  }

  /**
   * Create the reference to the central {@code EntityManagerFactory} using the values set in
   * 'config.properties'
   *
   * @return the {@code EntityManagerFactory}
   */
  public static EntityManagerFactory createEntityManagerFactory(DbSelector dbType,
      Strategy strategy) {
    String connectionStr;
    String user;
    String pw;
    if (dbType == DbSelector.DEV) {
      connectionStr = Settings.getDEV_DBConnection();
      user = Settings.getPropertyValue("db.user");
      pw = Settings.getPropertyValue("db.password");
    } else {
      connectionStr = Settings.getTEST_DBConnection();
      user = Settings.getPropertyValue("dbtest.user") != null ? Settings
          .getPropertyValue("dbtest.user") : Settings.getPropertyValue("db.user");
      pw = Settings.getPropertyValue("dbtest.password") != null ? Settings
          .getPropertyValue("dbtest.password") : Settings.getPropertyValue("db.password");
    }
    return createEntityManagerFactory(PERSISTENCE_UNIT_NAME, connectionStr, user, pw, strategy);
  }

  /**
   * Create the reference to the central {@code EntityManagerFactory} using the values set in
   * 'config.properties'
   *
   * @return the {@code EntityManagerFactory}
   */
  public static EntityManagerFactory createEntityManagerFactory(
      String puName,
      String connectionStr,
      String user,
      String pw,
      Strategy strategy) {
    Properties props = new Properties();
    log.log(Level.INFO, "USER -------------> " + user);
    log.log(Level.INFO, "PW ------> " + pw + "  (should be empty)");
    log.log(Level.INFO, "CONNECTION URL----> " + connectionStr);
    log.log(Level.INFO, "PU-Strategy ------> " + strategy.toString());
    props.setProperty("javax.persistence.jdbc.user", user);
    props.setProperty("javax.persistence.jdbc.password", pw);
    props.setProperty("javax.persistence.jdbc.url", connectionStr);
    if (strategy != Strategy.NONE) {
      props.setProperty("javax.persistence.schema-generation.database.action",
          strategy.toString());
    }
    return emf = Persistence.createEntityManagerFactory(puName, props);
  }

  /**
   * Convenience method for creating an {@code EntityManager}. This method creates a new {@code
   * EntityManager} using the central {@code EntityManagerFactory}.
   *
   * @return the new {@code EntityManager}
   */
  public static EntityManager createEM() {
    return emf.createEntityManager();
  }

  @Override
  public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
    try {
      ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
      ObjectOutputStream toClientObject = new ObjectOutputStream(outToClient);
      Object request = fromClient.readObject();
      SystemRequest systemRequest = (SystemRequest) request;
      if (systemRequest.type.equals(Type.Transaction) && systemRequest.data instanceof List) {
        List<SystemRequest> systemRequestList = (List<SystemRequest>) systemRequest.data;
        systemRequestList
            .forEach(requestIterator -> handleRequest(toClientObject, requestIterator));
      } else {
        handleRequest(toClientObject, systemRequest);
      }
    } catch (Exception ignored) {
    }
  }

  /**
   * Strategy to execute when communicating with a client
   *
   * @param systemRequest  - request to handle
   * @param toClientObject - ObjectOutputStream of a socket to the client
   */
  public void handleRequest(ObjectOutputStream toClientObject, SystemRequest systemRequest) {
    try {
      switch (systemRequest.type) {
        case Delete:
          if (systemRequest.data instanceof List) {
            toClientObject.writeObject(DB.removeAll((List) systemRequest.data));
          } else {
            toClientObject.writeObject(DB.remove(systemRequest.data));
          }
          toClientObject.flush();
          break;
        case Insert:
          if (systemRequest.data instanceof List) {
            toClientObject.writeObject(DB.persistAll((List) systemRequest.data));
          } else {
            toClientObject.writeObject(DB.persist(systemRequest.data));
          }
          toClientObject.flush();
          break;
        case Update:
          toClientObject.writeObject(DB.update(systemRequest.queryName, systemRequest.data));
          toClientObject.flush();
          break;
        case Query:
          List toClient = DB.query(systemRequest.queryName, systemRequest.data);
          toClientObject.writeObject(toClient);
          toClientObject.flush();
          break;
        default:
          break;
      }
    } catch (EOFException ignored) {
    } catch (Exception e) {
      log.log(Level.ERROR, e.getMessage());
    }
  }

  public enum DbSelector {
    DEV {
      @Override
      public String toString() {
        return "db.database";
      }
    },
    TEST {
      @Override
      public String toString() {
        return "dbtest.database";
      }
    }
  }

  public enum Strategy {
    NONE {
      @Override
      public String toString() {
        return "none";
      }
    },
    CREATE {
      @Override
      public String toString() {
        return "create";
      }
    },
    DROP {
      @Override
      public String toString() {
        return "drop";
      }
    },
    DROP_AND_CREATE {
      @Override
      public String toString() {
        return "drop-and-create";
      }
    }
  }
}