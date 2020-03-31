package BL.Server;

import BL.Server.utils.DB;
import BL.Server.utils.Settings;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ServerSystem {

    private DB sportifyDB;

    public ServerSystem(){
        sportifyDB = DB.getFacade(createEntityManagerFactory(DbSelector.DEV, Strategy.DROP_AND_CREATE));
    }

    /**
     * Create an EntityManagerFactory using values set in 'config.properties'
     * @return The new EntityManagerFactory
     */
    public static EntityManagerFactory createEntityManagerFactory(DbSelector dbType,Strategy strategy){
        String puName="sportify"; //Only pu legal name
        String connection_str;
        String user;
        String pw;
        if(dbType != DbSelector.TEST){
            connection_str = Settings.getDEV_DBConnection();
            user = Settings.getPropertyValue("db.user");
            pw = Settings.getPropertyValue("db.password");
        } else{
            connection_str = Settings.getTEST_DBConnection();
            user = Settings.getPropertyValue("dbtest.user");
            pw = Settings.getPropertyValue("dbtest.password");
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
        System.out.println("USER ------------> "+user);
        System.out.println("PW --------------> "+pw);
        System.out.println("CONNECTION STR---> "+connection_str);
        System.out.println("PU-Strategy---> "+strategy.toString());

        props.setProperty("javax.persistence.jdbc.user", user);
        props.setProperty("javax.persistence.jdbc.password", pw);
        props.setProperty("javax.persistence.jdbc.url", connection_str);
        if (strategy != Strategy.NONE)
            props.setProperty("javax.persistence.schema-generation.database.action", strategy.toString());
        return Persistence.createEntityManagerFactory(puName, props);
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
    };

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

    //Simple manual test
    public static void main(String[] args) {
        createEntityManagerFactory(DbSelector.DEV,Strategy.DROP);
    }
}
