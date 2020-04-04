package BL.Communication;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Description:     X
 **/
public class ClientServerCommunication
{
    private static InetAddress serverIP;
    private static int serverPort;

    /**
     * Query the DB in the server and get the results
     * @param queryName - NamedQuery in persistence to query the data base
     * @param parameters - map of parameters of the named query
     * @return list of objects that matches the query, null if something went wrong with the connection
     */
    public static List query(String queryName, Map<String, Object> parameters)
    {
        try(Socket serverSocket = new Socket(serverIP,serverPort))
        {
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());

            out.writeObject(new SystemRequest(SystemRequest.Type.Query,queryName,parameters));
            out.flush();

            List answer = (List) in.readObject();
            return answer;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Update the data base in the server base on a named query
     * @param queryName - NamedQuery in persistence to query the data base
     * @param parameters - map of parameters of the named query
     * @return true if the update completed in success, false other wise
     */
    public static boolean update(String queryName, Map<String, Object> parameters)
    {
        try(Socket serverSocket = new Socket(serverIP,serverPort))
        {
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());

            out.writeObject(new SystemRequest(SystemRequest.Type.Update,queryName,parameters));
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

    /**
     * Insert new Object to the data base in the server base on a named query
     * @param toInsert - object to insert into the data base
     * @return true if the insertion completed in success, false other wise
     */
    public static boolean insert(Object toInsert)
    {
        try(Socket serverSocket = new Socket(serverIP,serverPort))
        {
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());

            out.writeObject(new SystemRequest(SystemRequest.Type.Insert,"INSERT",toInsert));
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

    /**
     * Delete object from the data base in the server base on a named query
     * @param toDelete - object to delete from the data base
     * @return true if the delete completed in success, false other wise
     */
    public static boolean delete(Object toDelete)
    {
        try(Socket serverSocket = new Socket(serverIP,serverPort))
        {
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());

            out.writeObject(new SystemRequest(SystemRequest.Type.Delete,"DELETE",toDelete));
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
