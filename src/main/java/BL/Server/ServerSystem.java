
package BL.Server;

import BL.Communication.IServerStrategy;
import BL.Communication.Server;
import BL.Communication.SystemRequest;
import BL.Communication.SystemRequest.Type;
import BL.Server.ExternalSystems.FinancialSystem;
import BL.Server.ExternalSystems.TaxSystem;
import BL.Server.utils.Configuration;
import BL.Server.utils.DB;
import DL.Administration.SystemManager;
import DL.Users.Notifiable;
import DL.Users.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Level;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Properties;


/**
 * <p>
 * An utility class for managing an {@code EntityManagerFactory}.
 * </p>
 * <p>
 * Through the static methods provided by this class a reference to the default {@code
 * EntityManagerFactory} can be obtained. All code on the server that needs to access the database
 * can obtain a factory through this class.
 * <p>
 * MySQL_URI:  {@code jdbc:mysql://<HOST>:<PORT>/<DATABASE_NAME>}
 * </p>
 *
 * @author Serfati
 * @version Id: 1.0.
 */
@Log4j(topic = "event")
@Setter
@Getter
public class ServerSystem implements IServerStrategy {

//    public final static Logger elog = LogManager.getLogger("error");
    static final String PERSISTENCE_UNIT_NAME = "sportify";  /* The name of the persistence unit*/
    @PersistenceUnit
    static EntityManagerFactory emf;  /* The central, shared entity manager factory instance. */
    static Server server;
    private DB dataBase;

    //notification handling objects
    private NotificationUnit notificationUnit;
    private FinancialSystem financialSystem;
    private TaxSystem taxSystem;

    public static void main(String[] args) {
        ServerSystem serverSystem = new ServerSystem(DbSelector.TEST, Strategy.NONE, new NotificationUnit());
        try {
            serverSystem.initializeServer();
        } catch (Exception e) {
            log.error("Error when connection to the server" + e.getMessage());
        }
    }

    /**
     * c`tor
     *
     * @param dbType   database type , choose between dev (deploy) and test (junit)
     * @param strategy database persistence strategy
     */
    public ServerSystem(DbSelector dbType, Strategy strategy, NotificationUnit notificationUnit) {
        final EntityManagerFactory entityManagerFactory = createEntityManagerFactory(dbType, strategy);
        dataBase = DB.getDataBaseInstance(entityManagerFactory);
        final String sysQueryName = "SystemManagers";
        final boolean systemManagers =
                createEM().createNamedQuery(sysQueryName).getFirstResult() == 0;
        if (systemManagers) {
            signUp("admin", "admin@admin.com", "admin");
            log.info("admin user has been created");
        }

        this.notificationUnit = notificationUnit;
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
            connectionStr = Configuration.getDEV_DBConnection();
            user = Configuration.getPropertyValue("db.local.user");
            pw = Configuration.getPropertyValue("db.local.password");
        } else {
            connectionStr = Configuration.getTEST_DBConnection();
            user = Configuration.getPropertyValue("db.local.user") != null ? Configuration
                    .getPropertyValue("db.local.user") : Configuration.getPropertyValue("db.local.user");
            pw = Configuration.getPropertyValue("db.local.password") != null ? Configuration
                    .getPropertyValue("db.local.password") : Configuration.getPropertyValue("db.local.password");
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
        log.log(Level.INFO, "CONNECTION URL ----> " + connectionStr);
        log.log(Level.INFO, "PU-Strategy ------> " + strategy.toString());
        props.setProperty("javax.persistence.jdbc.user", user);
        props.setProperty("javax.persistence.jdbc.password", pw);
        props.setProperty("javax.persistence.jdbc.url", connectionStr);
        if (strategy != Strategy.NONE) {
            props.setProperty("javax.persistence.schema-generation.database.action",
                    strategy.toString());
        }
        emf = Persistence.createEntityManagerFactory(puName, props);
        return emf;
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
            log.warn("invalid username or password");
            return null;
        }
        final boolean userExist = createEM().find(User.class, userName) != null;
        if (userExist) {
            log.warn("username is already exist");
            return null;
        }
        //create the systemManager User
        String hashedPassword = DigestUtils.sha1Hex(password);
        SystemManager systemManager = new SystemManager(userName, email, hashedPassword);

        //insert the systemManager user to the DB
        DB.persist(systemManager);
        log.info("system manager created: " + userName);
        return systemManager;
    }

    /**
     * Demo the connection to external systems like Finance, Tax etc.
     * Handles interaction with external systems such as the finance system, log and tax
     * this class will interact with external systems.
     */
    @SuppressWarnings("unused")
    private void initializeExternalSystems() {
        log.info("external systems integration started");

        financialSystem = new FinancialSystem();
        taxSystem = new TaxSystem();

        log.info("external systems integration completed");
    }

    /**
     * Initializes the given server and builds the endpoint Read default login servers from {@code
     * config.properties} Default url {@code https://localhost:5400}
     *
     * @throws java.io.IOException I/O exception
     */
    @SuppressWarnings("unused")
    private void initializeServer() throws java.io.IOException {
        int serverPort = Integer.parseInt(Configuration.getPropertyValue("server.port"));
        int poolSize = Integer.parseInt(Configuration.getPropertyValue("server.poolSize"));
        int listeningInterval = Integer.parseInt(Configuration.getPropertyValue("server.listeningInterval"));
        server = new Server(serverPort, poolSize, listeningInterval, this);
        server.start();
        log.info("server is up and listen on port: " + serverPort);
        initializeExternalSystems();
    }

    @Override
    //public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
    public void serverStrategy(Socket clientSocket) {

        try {
            InputStream inFromClient = clientSocket.getInputStream();
            OutputStream outToClient = clientSocket.getOutputStream();

            //TODO: notification subscription only if the client asked for a subscription to the notifications


            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClientObject = new ObjectOutputStream(outToClient);
            Object request = fromClient.readObject();
            SystemRequest systemRequest = (SystemRequest) request;
            if (systemRequest.type.equals(Type.Transaction) && systemRequest.data instanceof List) {
                List<SystemRequest> systemRequestList = (List<SystemRequest>) systemRequest.data;
                systemRequestList
                        .forEach(requestIterator -> handleRequest(toClientObject, requestIterator, clientSocket));
            } else {
                log.info("handling a request from client");
                handleRequest(toClientObject, systemRequest, clientSocket);
            }

            //TODO: if the request is unsubscription from notifications - close the socket
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
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

    /**
     * Strategy to execute when communicating with a client
     *
     * @param systemRequest  - request to handle
     * @param toClientObject - ObjectOutputStream of a socket to the client
     */
    public void handleRequest(ObjectOutputStream toClientObject, SystemRequest systemRequest, Socket clientSocket) {
        try {
            System.out.println("welcome");
            switch (systemRequest.type) {
                case Login:
                    log.info(systemRequest.type + " request has been recived!");
                    List userToClient = DB.query("UserByUsernameAndPassword", systemRequest.data); //get list that contains only the user by username and password
//                    List userToClient = new ArrayList();
//                    userToClient.add(new Fan("admin", "admin", "admin"));
//                    toClientObject.writeObject((Serializable)userToClient);

                    toClientObject.flush();

                    if(userToClient.size() > 0)
                    {//there is a user with these credentials
                        User loggingInUser = (User)userToClient.get(0);
                        notificationUnit.subscribeUser(loggingInUser.getUsername(), clientSocket.getInetAddress());

                        //after sending the user object with the notifications to the client, make all notifications changed to read
                        notificationUnit.markAllNotificationsOfUserAsRead(loggingInUser);
                    }

                    toClientObject.writeObject(userToClient);

                    break;
                case Logout:
                    log.info(systemRequest.type + " request has been recived!");
                    User loggingOutUser = (User)systemRequest.data;
                    notificationUnit.unsubscribeUser(loggingOutUser.getUsername());
                    toClientObject.writeObject(true);
                    break;
                case Notify:
                    Notifiable notifiable = (Notifiable)systemRequest.data;
                    notificationUnit.notify(notifiable);
                    toClientObject.writeObject(true);
                    break;
                case Delete:
                    log.info(systemRequest.type + " request has been recived!");
                    if (systemRequest.data instanceof List) {
                        toClientObject.writeObject(DB.removeAll((List) systemRequest.data));
                    } else {
                        toClientObject.writeObject(DB.remove(systemRequest.data));
                    }
                    toClientObject.flush();
                    break;
                case Insert:
                    log.info(systemRequest.type + " request has been recived!");
                    if (systemRequest.data instanceof List) {
                        toClientObject.writeObject(DB.persistAll((List) systemRequest.data));
                    } else {
                        toClientObject.writeObject(DB.persist(systemRequest.data));
                    }

                    if (systemRequest.data instanceof Notifiable) {
                        notificationUnit.notify((Notifiable) systemRequest.data);
                    }

                    toClientObject.flush();
                    break;
                case Update:
                    log.info(systemRequest.type + " request has been recived!");
                    toClientObject.writeObject(DB.update(systemRequest.queryName, systemRequest.data));
                    toClientObject.flush();

                    //handle notifications for update queries


                    break;
                case Merge:
                    log.info(systemRequest.type + " request has been recived!");
                    toClientObject.writeObject(DB.merge(systemRequest.data));
                    toClientObject.flush();
                    break;

                case Query:
                    log.info(systemRequest.type + " request has been recived!");
                    List toClient = DB.query(systemRequest.queryName, systemRequest.data);
                    toClientObject.writeObject(toClient);
                    toClientObject.flush();
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("error accourse in request handle" + ex.getMessage());
        }
    }

}
