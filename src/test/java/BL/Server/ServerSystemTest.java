package BL.Server;

import BL.Client.ClientSystem;
import BL.Communication.ClientServerCommunication;
import BL.Server.ServerSystem.DbSelector;
import BL.Server.ServerSystem.Strategy;
import BL.Server.utils.Mock;
import BL.Server.utils.Settings;
import java.net.InetAddress;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * Description:
 **/
public class ServerSystemTest {

  static ClientServerCommunication csc;
  static ClientSystem client;
  static int serverPort;
  ServerSystem serverSystem;
  Mock m2;
  Mock m3;
  Mock m4;


  @SneakyThrows
  @BeforeAll
  public static void setUpClass() {
  }

  @BeforeEach
  public void setUp() {
    System.out.println("build setup");
    m2 = new Mock("two");
    m3 = new Mock("three");
    m4 = new Mock("four");
  }

  @AfterEach
  public void tearDown() {
    System.out.println("build setup");
  }

  @SneakyThrows
  @Test
  public void testServerSystem() {
    Mock m1 = new Mock("One");
    serverPort = Integer.parseInt(Settings.getPropertyValue("server.port"));
    serverSystem = new ServerSystem(DbSelector.TEST, Strategy.DROP_AND_CREATE);
    csc = new ClientServerCommunication();
    client = new ClientSystem(InetAddress.getLocalHost(), serverPort,
        ((inFromServer, outToServer) ->
        {
          csc.insert(m1);
          csc.insert(new Mock("two"));
          csc.insert(new Mock("one"));
        }
        ));
    client.communicateWithServer();
  }
}