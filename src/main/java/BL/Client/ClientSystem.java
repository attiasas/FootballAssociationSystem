package BL.Client;

import BL.Client.Handlers.*;
import BL.Communication.ClientServerCommunication;
import DL.Users.User;


/**
 * Description:     X ID:              X
 **/
public class ClientSystem
{
    private static User loggedUser = null;
    public ClientServerCommunication communication;

    public MatchEventUnit matchEventUnit;
    public LeagueSeasonUnit leagueSeasonUnit;
    public PoliciesUnit policiesUnit;

    public ClientSystem()
    {
        communication = new ClientServerCommunication();
        leagueSeasonUnit = new LeagueSeasonUnit(communication);
        policiesUnit = new PoliciesUnit(communication);
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
