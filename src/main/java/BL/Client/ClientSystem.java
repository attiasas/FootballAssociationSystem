package BL.Client;

import BL.Client.Handlers.LeagueSeasonUnit;
import BL.Client.Handlers.MatchEventUnit;
import BL.Client.Handlers.PoliciesUnit;
import BL.Communication.ClientServerCommunication;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Assets.Stadium;
import DL.Team.Members.Player;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

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
        communication.startNotificationListener();
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
