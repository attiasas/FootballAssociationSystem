package BL.Server;

import BL.Server.utils.DB;
import BL.Server.utils.Settings;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Properties;


public class ServerSystem {

    private DB sportifyDB;
    protected static int accepPort = 8080;

    public ServerSystem() {
        sportifyDB = DB.getFacade(createEntityManagerFactory(DbSelector.DEV, Strategy.DROP_AND_CREATE));
    }

    /**
     * Create an EntityManagerFactory using values set in 'config.properties'
     * @return The new EntityManagerFactory
     */
    public static EntityManagerFactory createEntityManagerFactory(DbSelector dbType,Strategy strategy){
        String puName = "sportify"; //Only legal name
        String connection_str;
        String user;
        String pw;
        if (dbType == DbSelector.DEV) {
            connection_str = Settings.getDEV_DBConnection();
            user = Settings.getPropertyValue("db.user");
            pw = Settings.getPropertyValue("db.password");
            System.clearProperty("IS_TEST");
        } else {
            connection_str = Settings.getTEST_DBConnection();
            //Will ensure REST code "switches" to this DB, even when running on a separate JVM
            System.setProperty("IS_TEST", connection_str);
            user = Settings.getPropertyValue("dbtest.user") != null ? Settings.getPropertyValue("dbtest.user") : Settings.getPropertyValue("db.user");
            pw = Settings.getPropertyValue("dbtest.password") != null ? Settings.getPropertyValue("dbtest.password") : Settings.getPropertyValue("db.password");
        }
        return createEntityManagerFactory(puName,connection_str,user,pw,strategy);
    }

    /**
     * Create an EntityManagerFactory using supplied values
     * @return  The new EntityManagerFactory
     */
    public static EntityManagerFactory createEntityManagerFactory(
            String puName,
            String connection_str,
            String user,
            String pw,
            Strategy strategy) {

        Properties props = new Properties();

        //A test running on a different JVM can alter values to use via this property
        if (System.getProperty("IS_INTEGRATION_TEST_WITH_DB") != null) {
            System.out.println("--------- IS_INTEGRATION_TEST_WITH_DB (Integration Test With DataBase --------------- ");
            connection_str = Settings.getTEST_DBConnection();
            user = System.getProperty("USER") != null ? System.getProperty("USER") : user;
            pw = System.getProperty("PW") != null ? System.getProperty("PW") : pw;
        }

        //A deployment server MUST set the following values which will override the defaults
        boolean isDeployed = (System.getenv("DEPLOYED") != null);
        if (isDeployed) {
            user = System.getenv("USER");
            pw = System.getenv("PW");
            connection_str = System.getenv("CONNECTION_STR");
        }
        /*
        On your server in /opt/tomcat/bin/setenv.sh   add the following WITH YOUR OWN VALUES

        export DEPLOYED="DEV_ON_DIGITAL_OCEAN"
        export USER="dev"
        export PW="ax2"
        export CONNECTION_STR="jdbc:mysql://localhost:3306/mydb"

        Then save the file, and restart tomcat: sudo systemctl restart tomcat
        */

        System.out.println("USER -------------> "+user);
        System.out.println("PW ---------------> "+pw);
        System.out.println("CONNECTION URL----> "+connection_str);
        System.out.println("PU-Strategy ------> "+strategy.toString());

        props.setProperty("javax.persistence.jdbc.user", user);
        props.setProperty("javax.persistence.jdbc.password", pw);
        props.setProperty("javax.persistence.jdbc.url", connection_str);
        if (strategy != Strategy.NONE) {
            props.setProperty("javax.persistence.schema-generation.database.action", strategy.toString());
        }
        return Persistence.createEntityManagerFactory(puName, props);
    }

    //Simple manual test
    public static void main(String[] args) {
        createEntityManagerFactory(DbSelector.DEV, Strategy.DROP_AND_CREATE);
        if (args.length > 0) {
            // allow changing accept port
            accepPort = Integer.parseInt(args[0]);
        }
        ServerSystem server = new ServerSystem();
        try {
            server.runServer();
        } catch(InterruptedException e) {
            // Exit - Ctrl -C pressed
        }
    }

    public void runServer() throws InterruptedException {
        ServerSocketChannel acceptSocket = null;
        try {
            acceptSocket = ServerSocketChannel.open();
            acceptSocket.configureBlocking(false);  // non-blocking to allow loop to handle other things
            acceptSocket.socket().bind(new InetSocketAddress(accepPort));
        } catch(IOException e) {
            System.out.println(String.format("ERROR accepting at port %d", accepPort));
            return;
        }

        while(true) {
            SocketChannel connectionSocket = null;
            try {
                connectionSocket = acceptSocket.accept();
                if (connectionSocket != null) {
                    /*
                     *
                     *  TODO -  ADD CLIENT CONNECTION
                     *
                     */
                }
            } catch(IOException e) {
                System.out.println(String.format("ERROR accept:\n  %s", e.toString()));
                continue;
            }
            Thread.sleep(50);
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
