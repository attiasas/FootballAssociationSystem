package BL.Communication;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Description:     This class represents an interface for server strategy when communicating with a client
 **/
public interface IServerStrategy
{
//    /**
//     * Strategy to execute when communicating with a client
//     * @param inFromClient - InputStream of a socket
//     * @param outToClient - OutputStream of a socket
//     */
//    void serverStrategy(InputStream inFromClient, OutputStream outToClient);

    void serverStrategy(Socket clientSocket);
}
