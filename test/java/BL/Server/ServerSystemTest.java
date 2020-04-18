package BL.Server;

import BL.Client.ClientSystem;
import BL.Communication.ClientServerCommunication;
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
public class ServerSystemTest
{

  /*static ClientServerCommunication csc;
  static ClientSystem client;
  static int serverPort;
  ServerSystem serverSystem = new ServerSystem();
  Mock m3;
  Mock m4;


  @SneakyThrows
  @BeforeAll
  public static void setUpClass() {
  }

  @BeforeEach
  public void setUp() {
    System.out.println("build setup");

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
    Mock m2 = new Mock("two");
    serverPort = Integer.parseInt(Settings.getPropertyValue("server.port"));
    csc = new ClientServerCommunication();
    csc.insert(m1);
    csc.insert(m2);
  }

  @SneakyThrows
  public void testServerSystem2() {
    Mock m1 = new Mock("One");
    Mock m2 = new Mock("two");
    serverPort = Integer.parseInt(Settings.getPropertyValue("server.port"));
    csc = new ClientServerCommunication();
    ClientSystem client2 = new ClientSystem(InetAddress.getLocalHost(), serverPort,
        ((inFromServer, outToServer) -> csc.insert(m2)));
  }*/
}