package BL.Server;

import BL.Client.ClientSystem;
import BL.Communication.ClientServerCommunication;
import BL.Communication.IServerStrategy;
import BL.Communication.Server;
import BL.Communication.SystemRequest;
import BL.Server.utils.DB;
import BL.Server.utils.Settings;
import DL.Team.Assets.Stadium;
import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;


/**
 * <p>
 * An utility class for managing an {@code EntityManagerFactory}.
 * </p>
 * <p>
 * Through the static methods provided by this class a reference to the default {@code
 * EntityManagerFactory} can be obtained. All code on the server that needs to access the database
 * can obtain a factory through this class. MySQL: jdbc:mysql://<HOST>:<PORT>/<DATABASE_NAME>
 * </p>
 *
 * @author Serfati
 * @version Id: 1.0
 */
public class ServerSystem implements IServerStrategy {

  private static final String PERSISTENCE_UNIT_NAME = "sportify";  /* The name of the persistence unit*/
  @PersistenceUnit
  private static EntityManagerFactory emf;  /* The central, shared entity manager factory instance. */
  public Server server;
  private DB dataBase;

  private ServerSystem(DbSelector dbType, Strategy strategy) {
    dataBase =
        DB.getDataBaseInstance(createEntityManagerFactory(dbType, strategy));
    int serverPort = Integer.parseInt(Settings.getPropertyValue("server.port"));
    int poolSize = Integer.parseInt(Settings.getPropertyValue("server.poolSize"));
    int listeningInterval = Integer.parseInt(Settings.getPropertyValue("server.listeningInterval"));
    server = new Server(serverPort, poolSize, listeningInterval, this);
    server.start();
  }

  /**
   * Create the reference to the central {@code EntityManagerFactory} using the values set in
   * 'config.properties'
   *
   * @return the {@code EntityManagerFactory}
   */
  public static EntityManagerFactory createEntityManagerFactory(DbSelector dbType,
      Strategy strategy) {
    String connection_str;
    String user;
    String pw;
    if (dbType == DbSelector.DEV) {
      connection_str = Settings.getDEV_DBConnection();
      user = Settings.getPropertyValue("db.user");
      pw = Settings.getPropertyValue("db.password");
    } else {
      connection_str = Settings.getTEST_DBConnection();
      user = Settings.getPropertyValue("dbtest.user") != null ? Settings
          .getPropertyValue("dbtest.user") : Settings.getPropertyValue("db.user");
      pw = Settings.getPropertyValue("dbtest.password") != null ? Settings
          .getPropertyValue("dbtest.password") : Settings.getPropertyValue("db.password");
    }
    return createEntityManagerFactory(PERSISTENCE_UNIT_NAME, connection_str, user, pw, strategy);
  }

  /**
   * Create the reference to the central {@code EntityManagerFactory} using the values set in
   * 'config.properties'
   *
   * @return the {@code EntityManagerFactory}
   */
  public static EntityManagerFactory createEntityManagerFactory(
      String puName,
      String connection_str,
      String user,
      String pw,
      Strategy strategy) {
    Properties props = new Properties();
    System.out.println("USER -------------> " + user);
    System.out.println("PW ---------------> " + pw);
    System.out.println("CONNECTION URL----> " + connection_str);
    System.out.println("PU-Strategy ------> " + strategy.toString());
    props.setProperty("javax.persistence.jdbc.user", user);
    props.setProperty("javax.persistence.jdbc.password", pw);
    props.setProperty("javax.persistence.jdbc.url", connection_str);
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

  /* Simple manual tests ------------------------------------------------------------------------------- */
  public static void main(String[] args) {
    //createEntityManagerFactory(DbSelector.DEV, Strategy.DROP_AND_CREATE); /* should print db log */
    ServerSystem server = new ServerSystem(DbSelector.TEST, Strategy.NONE);
    //Initializing servers
    try {
      int serverport = Integer.parseInt(Settings.getPropertyValue("server.port"));
      ClientSystem client = new ClientSystem(InetAddress.getLocalHost(), serverport,
          (inFromServer, outToServer) -> {
            try {
              Stadium teddy = new Stadium("Teddy", 16);
//                    Stadium anfield = new Stadium("anfield", 60);
//                    ClientServerCommunication.insert(anfield); //send maze dimensions to server
              ClientServerCommunication.insert(teddy); //send maze dimensions to server

            } catch (Exception e) {
              e.printStackTrace();
            }
          });
      client.communicateWithServer();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Server getServer() {
    return server;
  }

  @Override
  public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
    try {
      ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
      ObjectOutputStream toClientObject = new ObjectOutputStream(outToClient);
      EntityManager em = emf.createEntityManager();
      EntityTransaction txn;
      Object ret;
      SystemRequest sys = (SystemRequest) fromClient.readObject();
      switch (sys.type) {
        case Delete:
          DB.remove(sys.data);
          break;
        case Insert:
          DB.persist(sys.data);
          toClientObject.writeObject(true);
          toClientObject.flush();
          break;
        case Update:
          ret = DB.update(sys.queryName, sys.data);
          toClientObject.writeObject(ret);
          toClientObject.flush();
          break;
        case Query:
          List toClient = DB.query(sys.queryName, sys.data);
          toClientObject.writeObject(toClient);
          toClientObject.flush();
          break;
      }

    } catch (EOFException ignored) {
    } catch (Exception e) {
      System.out.println("Exception in ServerStrategy: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public DB getDataBase() {
    return dataBase;
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