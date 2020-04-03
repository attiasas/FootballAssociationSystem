package BL.Communication;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created By:      Assaf, on 06/03/2020.
 * Description:     This class represents an interface for server strategy when communicating with a client
 **/
public interface IServerStrategy
{
    /**
     * Strategy to execute when communicating with a client
     * @param inFromClient - InputStream of a socket
     * @param outToClient - OutputStream of a socket
     */
    void serverStrategy(InputStream inFromClient, OutputStream outToClient);
}
