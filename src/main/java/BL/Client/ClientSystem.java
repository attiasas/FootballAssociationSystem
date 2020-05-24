package BL.Client;

import BL.Client.Handlers.*;
import BL.Communication.ClientServerCommunication;
import DL.Administration.AssociationMember;
import DL.Administration.SystemManager;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.Match;
import DL.Team.Assets.Stadium;
import DL.Team.Members.Coach;
import DL.Team.Members.Player;
import DL.Team.Members.TeamManager;
import DL.Team.Members.TeamOwner;
import DL.Team.Members.TeamUser;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;
import DL.Users.UserPermission;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * Description:     X ID:              X
 **/
public class ClientSystem
{
    private static User loggedUser = null;
    private ClientServerCommunication communication;

    public MatchEventUnit matchEventUnit;
    public LeagueSeasonUnit leagueSeasonUnit;
    public PoliciesUnit policiesUnit;

    public ClientSystem()
    {
        communication = new ClientServerCommunication();
        communication.startNotificationListener();
        this.leagueSeasonUnit = new LeagueSeasonUnit(communication);
        this.policiesUnit = new PoliciesUnit(communication);
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
