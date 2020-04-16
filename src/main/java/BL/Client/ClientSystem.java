package BL.Client;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Level;

/**
 * Description:     X ID:              X
 **/
@Setter
@Log4j
public class ClientSystem {

  private InetAddress serverIP;
  private int serverPort;
  private IClientStrategy clientStrategy;

  public ClientSystem(InetAddress IP, int port, IClientStrategy clientStrategy) {
    this.serverIP = IP;
    this.serverPort = port;
    this.clientStrategy = clientStrategy;
  }

  public void communicateWithServer() {
    try {
      Socket theServer = new Socket(serverIP, serverPort);
      log.log(Level.INFO,
          "Client connected to server on " + serverIP.getHostAddress() + ":" + serverPort);
      clientStrategy.clientStrategy(theServer.getInputStream(), theServer.getOutputStream());
      theServer.close();
    } catch (IOException e) {
      log.log(Level.ERROR, e.getMessage());
    }
  }
}
