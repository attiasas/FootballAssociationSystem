package BL.Communication;

import java.util.Map;

/**
 * Description:     Represents a request from the client to the server
 *
 * Types:           * Delete - delete object form the DB
 *                  * Insert - insert object into the DB
 *                  * Update - update objects from the DB base on a query
 *                  * Query - query the DB for info base on a query
 **/
public class SystemRequest
{
    public enum Type
    {
        Delete,Insert,Update,Query,Transaction
    }

    public final Type type;
    public String queryName;
    public Object data;

    /**
     * Constructor for UPDATE/QUERY
     * @param type - type of request
     * @param queryName - name of the query for update/query
     * @param data - for Update/Query a Map<String,Object> for Insert/Delete an object.
     */
    public SystemRequest(Type type, String queryName, Object data)
    {
        this.type = type;
        this.queryName = queryName;
        this.data = data;
    }

    public static SystemRequest insert(Object data)
    {
        return new SystemRequest(Type.Insert,"INSERT",data);
    }

    public static SystemRequest delete(Object data)
    {
        return new SystemRequest(Type.Delete,"DELETE",data);
    }

    public static SystemRequest update(String queryName, Map<String,Object> parameters)
    {
        return new SystemRequest(Type.Update,queryName,parameters);
    }
}