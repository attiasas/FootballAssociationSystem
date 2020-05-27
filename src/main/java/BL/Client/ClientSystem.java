package BL.Client;

import BL.Client.Handlers.*;
import BL.Communication.ClientServerCommunication;
import DL.Administration.AssociationMember;
import DL.Administration.SystemManager;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Assets.Stadium;
import DL.Team.Members.*;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import PL.CommunicationMatchEventUnitStub;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Description:     X ID:              X
 **/
public class ClientSystem
{
    private static User loggedUser = null;
    public static ClientServerCommunication communication = new ClientServerCommunication();

    public AssociationManagementUnit associationManagementUnit;
    public ComplaintUnit complaintUnit;
    public FinancialUnit financialUnit;
    public HandleUserUnit userUnit;
    public LeagueSeasonUnit leagueSeasonUnit;
    public MatchEventUnit matchEventUnit;
    public NomineePermissionUnit nomineePermissionUnit;
    public PageUnit pageUnit;
    public PoliciesUnit policiesUnit;
    public RecommendationUnit recommendationUnit;
    public TeamAssetUnit teamAssetUnit;

    public ClientSystem()
    {
        initLocalData();

        matchEventUnit = new MatchEventUnit(communication);
        nomineePermissionUnit = new NomineePermissionUnit(communication);
        associationManagementUnit = new AssociationManagementUnit(communication);
        leagueSeasonUnit = new LeagueSeasonUnit(communication);
        teamAssetUnit = new TeamAssetUnit(communication);
        userUnit = new HandleUserUnit(communication,nomineePermissionUnit,teamAssetUnit,associationManagementUnit);
        complaintUnit = new ComplaintUnit(communication);
        policiesUnit = new PoliciesUnit(communication);
    }


    private void initLocalData()
    {
        // init data to test with
        List<User> users = new ArrayList<>();
        Fan f1 = new Fan("Assaf","test@mail.com", DigestUtils.sha1Hex("abcd"));
        Fan f2 = new Fan("Amir","test@mail.com",DigestUtils.sha1Hex("abcd"));
        Fan f3 = new Fan("Amit","test@mail.com",DigestUtils.sha1Hex("abcd"));
        Fan f4 = new Fan("Avihai","test@mail.com",DigestUtils.sha1Hex("abcd"));
        Fan f5 = new Fan("Dvir","test@mail.com",DigestUtils.sha1Hex("abcd"));
        users.add(f1);
        users.add(f2);
        users.add(f3);
        users.add(f4);
        users.add(f5);

        //ClientSystem.logIn(f1);

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

        Match m1 = new Match(new Date(new Date().getTime() + (5 * 59 * 1000)),t1,t2,leagueSeason,stadium);
        m1.setMainReferee(r1);
        r1.addMainMatch(m1);
        t1.setHomeMatches(m1);
        t2.setAwayMatches(m1);
        Match m2 = new Match(Date.from(Instant.from(LocalDate.of(2019,1,2).atStartOfDay(ZoneId.systemDefault()))),t1,t2,leagueSeason,stadium);
        m2.setMainReferee(r1);
        r1.addMainMatch(m2);
        t1.setHomeMatches(m2);
        t2.setAwayMatches(m2);
        Match m3 = new Match(Date.from(Instant.from(LocalDate.of(2019,1,3).atStartOfDay(ZoneId.systemDefault()))),t1,t2,leagueSeason,stadium);
        r1.addLinesManMatch(m3);
        t1.setHomeMatches(m3);
        t2.setAwayMatches(m3);
        Match m4 = new Match(Date.from(Instant.from(LocalDate.of(2019,1,4).atStartOfDay(ZoneId.systemDefault()))),t1,t2,leagueSeason,stadium);
        m4.setMainReferee(r1);
        r1.addMainMatch(m4);
        t1.setHomeMatches(m4);
        t2.setAwayMatches(m4);

        List<Match> matches = new ArrayList<>();
        matches.add(m1);
        matches.add(m2);
        matches.add(m3);
        matches.add(m4);

        communication = new CommunicationMatchEventUnitStub(matches,referees,users);
    }

    public static User getLoggedUser()
    {
        return loggedUser;
    }

    public enum UserTypes
    {
        None,Fan,Referee,Association,Admin,Manager, Player, Owner, Coach, OwnerManager,OwnerPlayer,OwnerCoach;
    }

    public static UserTypes getLoggedUserType()
    {
        if(loggedUser != null)
        {
            if(loggedUser instanceof AssociationMember) return UserTypes.Association;
            if(loggedUser instanceof SystemManager) return UserTypes.Admin;

            HashMap<String,Object> params = new HashMap<>();
            List queryResult;

            // check if referee
            params.put("user",loggedUser);
            queryResult = communication.query("activeRefereeByUser",params);
            if(queryResult != null && !queryResult.isEmpty()) return UserTypes.Referee;

            // check if team user
            params.clear();
            params.put("fan",loggedUser);
            queryResult = communication.query("activeTeamUserByFan",params);
            if(queryResult != null && !queryResult.isEmpty())
            {
                // check if team owner
                params.clear();
                params.put("user",loggedUser);
                queryResult = communication.query("TeamOwnerByUser",params);
                if(queryResult != null && !queryResult.isEmpty())
                {
                    TeamUser teamUser = (TeamUser)queryResult.get(0);
                    if(teamUser instanceof Player) return UserTypes.OwnerPlayer;
                    if(teamUser instanceof Coach) return UserTypes.OwnerCoach;
                    if(teamUser instanceof TeamManager) return UserTypes.OwnerManager;

                    return UserTypes.Owner;
                }
                else
                {
                    TeamUser teamUser = (TeamUser)queryResult.get(0);
                    if(teamUser instanceof Player) return UserTypes.Player;
                    if(teamUser instanceof Coach) return UserTypes.Coach;
                    if(teamUser instanceof TeamManager) return UserTypes.Manager;
                }
            }

            // default - Fan
            return UserTypes.Fan;
        }

        return UserTypes.None;
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
