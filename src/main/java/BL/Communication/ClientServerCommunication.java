package BL.Communication;

import static java.net.InetAddress.getLocalHost;

import BL.Client.ClientSystem;
import BL.Server.utils.Configuration;
import DL.Users.Notification;
import DL.Users.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Description:     This Class Defines a CRUD Interface for communication with DB
 *                  In Addition this class will communicate with TCP to a server that holds the DB
 *
 * Operations:      * Insert (Create - CR)
 *                  * Update (U)
 *                  * Delete (D)
 *                  * Query - query the DB base on a namedQuery
 *                  * Transaction - Make multiple Insert/Update/Delete requests in the same communication and
 *                                  make all of them in the same transaction (all success or nothing)
 **/
public class ClientServerCommunication {

    private static InetAddress serverIP;
    private static final int serverPort = Integer.parseInt(Configuration.getPropertyValue("server.port"));

    static {
        try {
            serverIP = getLocalHost();
        } catch (UnknownHostException ignored) {
        }
    }

    private Thread listenerThread;
    private volatile boolean listen;

    public boolean startNotificationListener()
    {
        if(listenerThread != null && listenerThread.isAlive()) return false;

        listen = true;
        listenerThread = new Thread(() -> notificationDemon());
        return true;
    }

    public void notificationDemon()
    {
        int listenPort = Integer.parseInt(Configuration.getPropertyValue("clientNotification.port"));

        try(ServerSocket listenSocket = new ServerSocket(listenPort))
        {
            // init
            while (listen)
            {
                Socket serverSocket = listenSocket.accept(); // blocking call
                User loggedUser = ClientSystem.getLoggedUser();
                if(loggedUser != null)
                {
                    try(ObjectInputStream fromServer = new ObjectInputStream(serverSocket.getInputStream()))
                    {
                        Object objFromServer = fromServer.readObject();
                        if(objFromServer instanceof Notification) loggedUser.addNotification((Notification)objFromServer);
                    } catch (SocketTimeoutException e) { }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stopListener()
    {
        listen = false;
    }

    /**
     * Query the DB in the server and get the results
     *
     * @param queryName  - NamedQuery in persistence to query the data base
     * @param parameters - map of parameters of the named query
     * @return list of objects that matches the query, null if something went wrong with the
     * connection
     */
    public List query(String queryName, Map<String, Object> parameters)
    {
        try(Socket serverSocket = new Socket(serverIP,serverPort))
        {
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());

            out.writeObject(new SystemRequest(SystemRequest.Type.Query, queryName, parameters));
            out.flush();

            List answer = (List) in.readObject();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Update the data base in the server base on a named query
     *
     * @param queryName  - NamedQuery in persistence to query the data base
     * @param parameters - map of parameters of the named query
     * @return true if the update completed in success, false other wise
     */
    public boolean update(String queryName, Map<String, Object> parameters)
    {
        try(Socket serverSocket = new Socket(serverIP,serverPort))
        {
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());

            out.writeObject(SystemRequest.update(queryName,parameters));
            out.flush();

            boolean answer = (boolean) in.readObject();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Insert new Object to the data base in the server base on a named query
     * @param toInsert - object to insert into the data base
     * @return true if the insertion completed in success, false other wise
     */
    public boolean insert(Object toInsert)
    {
        try(Socket serverSocket = new Socket(serverIP,serverPort))
        {
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());

            out.writeObject(SystemRequest.insert(toInsert));
            out.flush();

            boolean answer = (boolean) in.readObject();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Delete object from the data base in the server base on a named query
     * @param toDelete - object to delete from the data base
     * @return true if the delete completed in success, false other wise
     */
    public boolean delete(Object toDelete)
    {
        try(Socket serverSocket = new Socket(serverIP,serverPort))
        {
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());

            out.writeObject(SystemRequest.delete(toDelete));
            out.flush();

            boolean answer = (boolean) in.readObject();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List login(String username,String password)
    {
        try(Socket serverSocket = new Socket(serverIP,serverPort))
        {
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());

            out.writeObject(SystemRequest.login(username,password));
            out.flush();

            List answer = (List) in.readObject();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * this method will make sure that all of the given request will be preformed with the same connection and atomic
     * @param requests - insert,delete and update requests
     * @return true if all the requests are successes false other wise
     */
    public boolean transaction(List<SystemRequest> requests)
    {
        // validate no query/transaction requests
        for(SystemRequest request : requests) if(request.type.equals(SystemRequest.Type.Query) || request.type.equals(SystemRequest.Type.Transaction) || request.type.equals(SystemRequest.Type.Login)) return false;

        try(Socket serverSocket = new Socket(serverIP,serverPort))
        {
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());

            out.writeObject(new SystemRequest(SystemRequest.Type.Transaction,"TRANSACTION",requests));
            out.flush();

            boolean answer = (boolean) in.readObject();
            return answer;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}