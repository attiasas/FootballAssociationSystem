package BL.Communication;

import BL.Server.NotificationUnit;
import BL.Server.ServerSystem;
import BL.Server.utils.Configuration;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.net.InetAddress.getLocalHost;

/**
 * Description:     This class represent a server that can communicate with multiple clients and execute a given strategy
 **/
public class Server {
    private int port;
    private int listeningInterval;
    private IServerStrategy serverStrategy;
    private volatile boolean stop;
    private ExecutorService threadPoolExecutor;
    private int poolSize;

    public static void main(String[] args)
    {
        IServerStrategy serverSystem = new ServerSystem(ServerSystem.DbSelector.TEST, ServerSystem.Strategy.DROP_AND_CREATE, new NotificationUnit());
        Server server = new Server(Integer.parseInt(Configuration.getPropertyValue("server.port")), 3, 1000, serverSystem);
        server.start();
    }



    /**
     * Constructor
     * @param port - port to listen for clients
     * @param poolSize - number of threads handling the server
     * @param listeningInterval - interval before TimeOut
     * @param serverStrategy - strategy to execute
     */
    public Server(int port,int poolSize , int listeningInterval, IServerStrategy serverStrategy)
    {
        this.port = port;
        this.listeningInterval = listeningInterval;
        this.serverStrategy = serverStrategy;
        this.poolSize = poolSize;
    }

    /**
     * Boot server in a different thread and start to listen for clients
     */
    public void start() {
        try {
            System.out.println(getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            runServer();
        }).start();
    }

    /**
     * main server loop
     */
    private void runServer()
    {
        try
        {
            threadPoolExecutor = Executors.newFixedThreadPool(poolSize);

            // init
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningInterval);

            while (!stop)
            {
                try
                {
                    Socket clientSocket = serverSocket.accept(); // blocking call
                    threadPoolExecutor.execute(() -> handleClient(clientSocket));

                } catch (SocketTimeoutException e)
                {

                }
            }

            threadPoolExecutor.shutdown();
            threadPoolExecutor.awaitTermination(1, TimeUnit.HOURS);
            serverSocket.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Execute strategy
     * @param clientSocket - client socket
     */
    private void handleClient(Socket clientSocket)
    {
        try
        {
            System.out.println("Connected");
            serverStrategy.serverStrategy(clientSocket);
            clientSocket.close(); //will only happen in the server strategy

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Stop the server form running and shutdown
     */
    public void stop()
    {
        stop = true;
    }
}