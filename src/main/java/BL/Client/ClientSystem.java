package BL.Client;

import BL.Client.Handlers.HandleUserUnit;
import BL.Client.Handlers.MatchEventUnit;
import BL.Communication.ClientServerCommunication;
import DL.Users.User;

//import PL.RefereeController;

/**
 * Description:     X ID:              X
 **/
public class ClientSystem
{
    private static User loggedUser = null;
    private ClientServerCommunication communication;

    public MatchEventUnit matchEventUnit;
    public HandleUserUnit userUnit;

    public ClientSystem()
    {
        communication = new ClientServerCommunication();
        communication.startNotificationListener();
    }

    public static User getLoggedUser()
    {
        return loggedUser;
    }

    public static boolean logIn(User user)
    {
        if(user == null)
        {
            return false;
        }

        loggedUser = user;

        return true;
    }


    /**
     * log out from the logged user
     * @return true if the logged user was exist and now is logged out
     */
    public static boolean logOut()
    {
        if(loggedUser == null)
        {
            return false;
        }

        loggedUser = null;
        return true;
    }

    public void close()
    {
        communication.stopListener();
    }
}
