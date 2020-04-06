package BL.Communication;

import java.io.Serializable;

/**
 * Description:     Represents a request from the client to the server
 * <p>
 * Types:           * Delete - delete object form the DB * Insert - insert object into the DB *
 * Update - update objects from the DB base on a query * Query - query the DB for info base on a
 * query
 **/
public class SystemRequest implements Serializable {

    public enum Type {
        Delete, Insert, Update, Query
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
}
