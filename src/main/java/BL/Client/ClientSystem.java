package BL.Client;

import BL.Client.Handlers.MatchEventUnit;
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

import PL.CommunicationMatchEventUnitStub;

/**
 * Description:     X ID:              X
 **/
public class ClientSystem
{
    private static User loggedUser = null;
    private ClientServerCommunication communication;

    public MatchEventUnit matchEventUnit;

    public ClientSystem()
    {
        //communication = new ClientServerCommunication();
        //communication.startNotificationListener();
        initData();
    }

    private void initData()
    {
        // init data to test with
        Fan f1 = new Fan("Assaf","test@mail.com","abcd");
        Fan f2 = new Fan("Amir","test@mail.com","abcd");
        Fan f3 = new Fan("Amit","test@mail.com","abcd");
        Fan f4 = new Fan("Avihai","test@mail.com","abcd");
        Fan f5 = new Fan("Dvir","test@mail.com","abcd");
        ClientSystem.logIn(f1);

        List<Referee> referees = new ArrayList<>();
        Referee r1 = new Referee("main","Referee-A",f1,true);
        referees.add(r1);

        League league = new League("Super League");
        Season season = new Season(2020);
        LeagueSeason leagueSeason = new LeagueSeason(league,season,new GamePolicy(),new ScorePolicy(),new java.sql.Date(2020,1,1));

        Team t1 = new Team("TeamA",true,false);
        Team t2 = new Team("TeamB",true,false);

        Player p1 = new Player("playerA",true,f2,new Date(2000,1,1),"attacker",t1);
        Player p2 = new Player("playerB",true,f3,new Date(2000,1,1),"attacker",t1);
        Player p3 = new Player("playerC",true,f4,new Date(2000,1,1),"defender",t1);
        Player p4 = new Player("playerA",true,f5,new Date(2000,1,1),"attacker",t2);
        t1.addPlayer(p1);
        t1.addPlayer(p2);
        t1.addPlayer(p3);
        t2.addPlayer(p4);

        Stadium stadium = new Stadium("Stadium",100,t1);

        Match m1 = new Match(new java.sql.Date(2010,1,1),t1,t2,leagueSeason,stadium);
        m1.setMainReferee(r1);
        r1.addMainMatch(m1);
        t1.setHomeMatches(m1);
        t2.setAwayMatches(m1);
        Match m2 = new Match(new java.sql.Date(2010,1,2),t1,t2,leagueSeason,stadium);
        m2.setMainReferee(r1);
        r1.addMainMatch(m2);
        t1.setHomeMatches(m2);
        t2.setAwayMatches(m2);
        Match m3 = new Match(new java.sql.Date(2010,1,3),t1,t2,leagueSeason,stadium);
        r1.addLinesManMatch(m3);
        t1.setHomeMatches(m3);
        t2.setAwayMatches(m3);
        Match m4 = new Match(new Date(2010,1,4),t1,t2,leagueSeason,stadium);
        m4.setMainReferee(r1);
        r1.addMainMatch(m4);
        t1.setHomeMatches(m4);
        t2.setAwayMatches(m4);

        List<Match> matches = new ArrayList<>();
        matches.add(m1);
        matches.add(m2);
        matches.add(m3);
        matches.add(m4);

        communication = new CommunicationMatchEventUnitStub(matches,referees);
        matchEventUnit = new MatchEventUnit(communication);
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
