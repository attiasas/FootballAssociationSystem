package BL.Client;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Description:     X ID:              X
 **/
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
      System.out.println("Client::Connected to server!");
      clientStrategy.clientStrategy(theServer.getInputStream(), theServer.getOutputStream());
      theServer.close();
    } catch (IOException e) {
      System.out.println("Exception in Client::communicateWithServer");
    }
  }
}
