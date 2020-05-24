package BL.Communication;

import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:     Represents a request from the client to the server
 * <p>
 * Types:           * Delete - delete object form the DB * Insert - insert object into the DB *
 * Update - update objects from the DB base on a query * Query - query the DB for info base on a
 * query
 **/
public class SystemRequest implements Serializable
{
    public enum Type
    {
        Delete,Insert,Update,Query,Transaction,Login,Logout
    }

    public final Type type;
    public String queryName;
    public Object data;

    /**
     * Constructor for UPDATE/QUERY
     *
     * @param type      - type of request
     * @param queryName - name of the query for update/query
     * @param data      - for Update/Query a Map<String,Object> for Insert/Delete an object.
     */
    public SystemRequest(Type type, String queryName, Object data) {
        this.type = type;
        this.queryName = queryName;
        this.data = data;
    }

    /**
     * Generate A Insertion Request
     * @param data - data to insert into the DB
     * @return SystemRequest to insert the given data
     */
    public static SystemRequest insert(Object data)
    {
        return new SystemRequest(Type.Insert,"INSERT",data);
    }

    /**
     * Generate A Deletion Request
     * @param data - data to delete from the DB
     * @return SystemRequest to delete the given data
     */
    public static SystemRequest delete(Object data)
    {
        return new SystemRequest(Type.Delete,"DELETE",data);
    }

    /**
     * Generate an Update Request
     * @param queryName - update query name to execute
     * @param parameters - parameters of the query
     * @return SystemRequest to update the DB by executing the given query
     */
    public static SystemRequest update(String queryName, Map<String,Object> parameters)
    {
        return new SystemRequest(Type.Update,queryName,parameters);
    }

    public static Object login(String username, String password)
    {
        HashMap parameters = new HashMap();
        parameters.put("username",username);
        parameters.put("password",password);
        return new SystemRequest(Type.Login,"LOGIN",parameters);
    }

    public static void main(String[] args) {
        File f = new File("D:\\University\\ThirdYear\\Semester 6\\Computer & Informaion Security\\Exercises\\Labs\\Lab2\\test.txt");
        try(FileOutputStream outputStream = new FileOutputStream(f))
        {
            String pre = "05";
            String mid = "-65";
            for(char c ='0'; c <= '9'; c++)
            {

            }
        }
        catch (Exception e){}
    }
}
