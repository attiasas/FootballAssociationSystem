package BL.Client.Handlers;

import BL.Communication.CommunicationMatchEventUnitStub;
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
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Description:     X
 * ID:              26
 **/
public class MatchEventUnitTest
{
    private MatchEventUnit unit;
    private List<Referee> referees;
    private List<Match> matches;

    private Team noMatchTeam;
    private Player noMatchPlayer;
    private Match endedMatch;

    private List<User> users;

    @Before
    public void setUp()
    {
        // init data to test with
        Fan f1 = new Fan("Assaf","test@mail.com","abcd");
        Fan f2 = new Fan("Amir","test@mail.com","abcd");
        Fan f3 = new Fan("Amit","test@mail.com","abcd");
        Fan f4 = new Fan("Dvir","test@mail.com","abcd");
        Fan f5 = new Fan("Avihai","test@mail.com","abcd");
        Fan f6 = new Fan("Dana","test@mail.com","abcd");
        Fan f7 = new Fan("Yosi","test@mail.com","abcd");

        users = new ArrayList<>();
        users.add(f1);
        users.add(f2);
        users.add(f3);
        users.add(f4);
        users.add(f5);
        users.add(f6);
        users.add(f7);

        Referee r1 = new Referee("main","Referee-A",f1,true);
        Referee r2 = new Referee("main","Referee-B",f2,true);
        Referee r3 = new Referee("main","Referee-C",f3,true);

        referees = new ArrayList<>();
        referees.add(r1);
        referees.add(r2);
        referees.add(r3);

        League league = new League("Super League");
        Season season = new Season(2020);
        LeagueSeason leagueSeason = new LeagueSeason(league,season,new GamePolicy(),new ScorePolicy(),new Date(2020,1,1));

        Team t1 = new Team("TeamA",true,false);
        Team t2 = new Team("TeamB",true,false);
        noMatchTeam = new Team("TeamC",true,false);

        Player p1 = new Player("PlayerA", true,f4,new Date(1980,1,1),"roleA",t1);
        Player p2 = new Player("PlayerB", true,f5,new Date(1980,1,1),"roleB",t1);
        Player p3 = new Player("PlayerC", true,f6,new Date(1980,1,1),"roleC",t2);
        noMatchPlayer = new Player("PlayerD", false,f6,new Date(1980,1,1),"roleD",noMatchTeam);

        t1.getPlayers().add(p1);
        t1.getPlayers().add(p2);
        t2.getPlayers().add(p3);
        noMatchTeam.getPlayers().add(noMatchPlayer);

        Stadium stadium = new Stadium("Stadium",100,t1);

        Match m1 = new Match(new Date(2010,1,1),t1,t2,leagueSeason,stadium);
        m1.setMainReferee(r1);
        r1.addMainMatch(m1);
        t1.setHomeMatches(m1);
        t2.setAwayMatches(m1);
        Match m2 = new Match(new Date(2010,1,2),t1,t2,leagueSeason,stadium);
        m2.setMainReferee(r1);
        r1.addMainMatch(m2);
        t1.setHomeMatches(m2);
        t2.setAwayMatches(m2);
        Match m3 = new Match(new Date(2010,1,3),t1,t2,leagueSeason,stadium);
        m3.setMainReferee(r2);
        r2.addMainMatch(m3);
        m3.setLinesManReferees(r1,r3);
        r1.addLinesManMatch(m3);
        t1.setHomeMatches(m3);
        t2.setAwayMatches(m3);
        Match m4 = new Match(new Date(2010,1,4),t1,t2,leagueSeason,stadium);
        m4.setMainReferee(r1);
        r1.addMainMatch(m4);
        t1.setHomeMatches(m4);
        t2.setAwayMatches(m4);
        endedMatch = new Match(new Date(2010,1,5),t1,t2,leagueSeason,stadium);
        endedMatch.setMainReferee(r1);
        r1.addMainMatch(endedMatch);
        t1.setHomeMatches(endedMatch);
        t2.setAwayMatches(endedMatch);
        endedMatch.setEndTime(new Date(2018,1,6));

        matches = new ArrayList<>();
        matches.add(m1);
        matches.add(m2);
        matches.add(m3);
        matches.add(m4);

        unit = new MatchEventUnit(new CommunicationMatchEventUnitStub(matches,referees));
    }

    @Test
    public void testGetActiveMatches()
    {
        List<Match> matches1 = unit.getActiveMatches(users.get(0));
        assertNotNull(matches1);
        assertEquals(3,matches1.size());

        List<Match> matches2 = unit.getActiveMatches(users.get(1));
        assertNotNull(matches1);
        assertEquals(1,matches2.size());

        List<Match> matches3 = unit.getActiveMatches(users.get(2));
        assertNotNull(matches1);
        assertEquals(0,matches3.size());
    }

    @Test
    public void testGetActiveMatchesBadArguments()
    {
        // null arguments
        List<Match> matches = unit.getActiveMatches(null);
        assertNull(matches);
        // not referee argument
        matches = unit.getActiveMatches(users.get(3));
        assertNull(matches);
    }

    @Test
    public void testAddYellowCard()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match = matches.get(0);
        Player player = match.getHomeTeam().getPlayers().get(0);

        assertTrue(unit.addYellowCard(users.get(0),match,player,15));
        assertEquals(1,match.getMyEventLog().getEvents().size());
        //assertEquals(1,player.getPlayerEvents().size());
    }

    @Test
    public void testAddYellowCardBadArguments()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match1 = matches.get(0);
        Player player1 = match1.getHomeTeam().getPlayers().get(0);
        matches = unit.getActiveMatches(users.get(1));
        Match match2 = matches.get(0);

        // null
        assertFalse(unit.addYellowCard(null,null,null,-1));
        assertFalse(unit.addYellowCard(users.get(0),null,null,-1));
        assertFalse(unit.addYellowCard(users.get(0),match1,null,-1));
        assertFalse(unit.addYellowCard(users.get(0),match1,player1,-1));

        // not referee match
        assertFalse(unit.addYellowCard(users.get(0),match2,player1,10));
        // not player of team in match
        assertFalse(unit.addYellowCard(users.get(0),match1,noMatchPlayer,10));
        // not active match of referee
        assertFalse(unit.addYellowCard(users.get(0),endedMatch,player1,10));

        // check no changes
        assertEquals(0,match1.getMyEventLog().getEvents().size());
        assertEquals(0,match2.getMyEventLog().getEvents().size());
        //assertEquals(0,player1.getPlayerEvents().size());
        //assertEquals(0,noMatchPlayer.getPlayerEvents().size());
        assertEquals(0,endedMatch.getMyEventLog().getEvents().size());
    }

    @Test
    public void testAddRedCard()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match = matches.get(0);
        Player player = match.getHomeTeam().getPlayers().get(0);

        assertTrue(unit.addRedCard(users.get(0),match,player,15));
        assertEquals(1,match.getMyEventLog().getEvents().size());
        //assertEquals(1,player.getPlayerEvents().size());
    }

    @Test
    public void testAddRedCardBadArguments()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match1 = matches.get(0);
        Player player1 = match1.getHomeTeam().getPlayers().get(0);
        matches = unit.getActiveMatches(users.get(1));
        Match match2 = matches.get(0);

        // null
        assertFalse(unit.addRedCard(null,null,null,-1));
        assertFalse(unit.addRedCard(users.get(0),null,null,-1));
        assertFalse(unit.addRedCard(users.get(0),match1,null,-1));
        assertFalse(unit.addRedCard(users.get(0),match1,player1,-1));

        // not referee match
        assertFalse(unit.addRedCard(users.get(0),match2,player1,10));
        // not player of team in match
        assertFalse(unit.addRedCard(users.get(0),match1,noMatchPlayer,10));
        // not active match of referee
        assertFalse(unit.addRedCard(users.get(0),endedMatch,player1,10));

        // check no changes
        assertEquals(0,match1.getMyEventLog().getEvents().size());
        assertEquals(0,match2.getMyEventLog().getEvents().size());
        //assertEquals(0,player1.getPlayerEvents().size());
        //assertEquals(0,noMatchPlayer.getPlayerEvents().size());
        assertEquals(0,endedMatch.getMyEventLog().getEvents().size());
    }

    @Test
    public void testAddGoal()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match = matches.get(0);
        Player player = match.getHomeTeam().getPlayers().get(0);

        assertTrue(unit.addGoal(users.get(0),match,player,15));
        assertEquals(1,match.getMyEventLog().getEvents().size());
        //assertEquals(1,player.getPlayerEvents().size());
    }

    @Test
    public void testAddGoalBadArguments()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match1 = matches.get(0);
        Player player1 = match1.getHomeTeam().getPlayers().get(0);
        matches = unit.getActiveMatches(users.get(1));
        Match match2 = matches.get(0);

        // null
        assertFalse(unit.addGoal(null,null,null,-1));
        assertFalse(unit.addGoal(users.get(0),null,null,-1));
        assertFalse(unit.addGoal(users.get(0),match1,null,-1));
        assertFalse(unit.addGoal(users.get(0),match1,player1,-1));

        // not referee match
        assertFalse(unit.addGoal(users.get(0),match2,player1,10));
        // not player of team in match
        assertFalse(unit.addGoal(users.get(0),match1,noMatchPlayer,10));
        // not active match of referee
        assertFalse(unit.addGoal(users.get(0),endedMatch,player1,10));

        // check no changes
        assertEquals(0,match1.getMyEventLog().getEvents().size());
        assertEquals(0,match2.getMyEventLog().getEvents().size());
        //assertEquals(0,player1.getPlayerEvents().size());
        //assertEquals(0,noMatchPlayer.getPlayerEvents().size());
        assertEquals(0,endedMatch.getMyEventLog().getEvents().size());
    }

    @Test
    public void testAddInjury()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match = matches.get(0);
        Player player = match.getHomeTeam().getPlayers().get(0);

        assertTrue(unit.addInjury(users.get(0),match,player,15));
        assertEquals(1,match.getMyEventLog().getEvents().size());
        //assertEquals(1,player.getPlayerEvents().size());
    }

    @Test
    public void testAddInjuryBadArguments()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match1 = matches.get(0);
        Player player1 = match1.getHomeTeam().getPlayers().get(0);
        matches = unit.getActiveMatches(users.get(1));
        Match match2 = matches.get(0);

        // null
        assertFalse(unit.addInjury(null,null,null,-1));
        assertFalse(unit.addInjury(users.get(0),null,null,-1));
        assertFalse(unit.addInjury(users.get(0),match1,null,-1));
        assertFalse(unit.addInjury(users.get(0),match1,player1,-1));

        // not referee match
        assertFalse(unit.addInjury(users.get(0),match2,player1,10));
        // not player of team in match
        assertFalse(unit.addInjury(users.get(0),match1,noMatchPlayer,10));
        // not active match of referee
        assertFalse(unit.addInjury(users.get(0),endedMatch,player1,10));

        // check no changes
        assertEquals(0,match1.getMyEventLog().getEvents().size());
        assertEquals(0,match2.getMyEventLog().getEvents().size());
        //assertEquals(0,player1.getPlayerEvents().size());
        //assertEquals(0,noMatchPlayer.getPlayerEvents().size());
        assertEquals(0,endedMatch.getMyEventLog().getEvents().size());
    }

    @Test
    public void testAddOffside()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match = matches.get(0);
        Player player = match.getHomeTeam().getPlayers().get(0);

        assertTrue(unit.addOffside(users.get(0),match,player,15));
        assertEquals(1,match.getMyEventLog().getEvents().size());
        //assertEquals(1,player.getPlayerEvents().size());
    }

    @Test
    public void testAddOffsideBadArguments()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match1 = matches.get(0);
        Player player1 = match1.getHomeTeam().getPlayers().get(0);
        matches = unit.getActiveMatches(users.get(1));
        Match match2 = matches.get(0);

        // null
        assertFalse(unit.addOffside(null,null,null,-1));
        assertFalse(unit.addOffside(users.get(0),null,null,-1));
        assertFalse(unit.addOffside(users.get(0),match1,null,-1));
        assertFalse(unit.addOffside(users.get(0),match1,player1,-1));

        // not referee match
        assertFalse(unit.addOffside(users.get(0),match2,player1,10));
        // not player of team in match
        assertFalse(unit.addOffside(users.get(0),match1,noMatchPlayer,10));
        // not active match of referee
        assertFalse(unit.addOffside(users.get(0),endedMatch,player1,10));

        // check no changes
        assertEquals(0,match1.getMyEventLog().getEvents().size());
        assertEquals(0,match2.getMyEventLog().getEvents().size());
        //assertEquals(0,player1.getPlayerEvents().size());
        //assertEquals(0,noMatchPlayer.getPlayerEvents().size());
        assertEquals(0,endedMatch.getMyEventLog().getEvents().size());
    }

    @Test
    public void testAddFoul()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match = matches.get(0);
        Player player1 = match.getHomeTeam().getPlayers().get(0);
        Player player2 = match.getAwayTeam().getPlayers().get(0);

        assertTrue(unit.addFoul(users.get(0),match,player1,player2,15));
        assertEquals(1,match.getMyEventLog().getEvents().size());
        //assertEquals(1,player1.getPlayerEvents().size());
        //assertEquals(1,player2.getPlayerEvents().size());
    }

    @Test
    public void testAddFoulBadArguments()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match1 = matches.get(0);
        Player player1 = match1.getHomeTeam().getPlayers().get(0);
        Player player2 = match1.getAwayTeam().getPlayers().get(0);
        matches = unit.getActiveMatches(users.get(1));
        Match match2 = matches.get(0);

        // null
        assertFalse(unit.addFoul(null,null,null,null,-1));
        assertFalse(unit.addFoul(users.get(0),null,null,null,-1));
        assertFalse(unit.addFoul(users.get(0),match1,null,null,-1));
        assertFalse(unit.addFoul(users.get(0),match1,player1,null,-1));
        assertFalse(unit.addFoul(users.get(0),match1,player1,player2,-1));

        // same player
        assertFalse(unit.addFoul(users.get(0),match1,player1,player1,10));
        // not referee match
        assertFalse(unit.addFoul(users.get(0),match2,player1,player2,10));
        // not player of team in match
        assertFalse(unit.addFoul(users.get(0),match1,player1,noMatchPlayer,10));
        assertFalse(unit.addFoul(users.get(0),match1,noMatchPlayer,player1,10));
        // not active match of referee
        assertFalse(unit.addFoul(users.get(0),endedMatch,player1,player2,10));

        // check no changes
        assertEquals(0,match1.getMyEventLog().getEvents().size());
        assertEquals(0,match2.getMyEventLog().getEvents().size());
        //assertEquals(0,player1.getPlayerEvents().size());
        //assertEquals(0,player2.getPlayerEvents().size());
        //assertEquals(0,noMatchPlayer.getPlayerEvents().size());
        assertEquals(0,endedMatch.getMyEventLog().getEvents().size());
    }

    @Test
    public void testAddPlayerChange()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match = matches.get(0);
        Player player1 = match.getHomeTeam().getPlayers().get(0);
        Player player2 = match.getAwayTeam().getPlayers().get(0);

        assertTrue(unit.addPlayerChange(users.get(0),match,player1,player2,15));
        assertEquals(1,match.getMyEventLog().getEvents().size());
        //assertEquals(1,player1.getPlayerEvents().size());
        //assertEquals(1,player2.getPlayerEvents().size());
    }

    @Test
    public void testAddPlayerChangeBadArguments()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match1 = matches.get(0);
        Player player1 = match1.getHomeTeam().getPlayers().get(0);
        Player player2 = match1.getAwayTeam().getPlayers().get(0);
        matches = unit.getActiveMatches(users.get(1));
        Match match2 = matches.get(0);

        // null
        assertFalse(unit.addPlayerChange(null,null,null,null,-1));
        assertFalse(unit.addPlayerChange(users.get(0),null,null,null,-1));
        assertFalse(unit.addPlayerChange(users.get(0),match1,null,null,-1));
        assertFalse(unit.addPlayerChange(users.get(0),match1,player1,null,-1));
        assertFalse(unit.addPlayerChange(users.get(0),match1,player1,player2,-1));

        // same player
        assertFalse(unit.addPlayerChange(users.get(0),match1,player1,player1,10));
        // not referee match
        assertFalse(unit.addPlayerChange(users.get(0),match2,player1,player2,10));
        // not player of team in match
        assertFalse(unit.addPlayerChange(users.get(0),match1,player1,noMatchPlayer,10));
        assertFalse(unit.addPlayerChange(users.get(0),match1,noMatchPlayer,player1,10));
        // not active match of referee
        assertFalse(unit.addPlayerChange(users.get(0),endedMatch,player1,player2,10));

        // check no changes
        assertEquals(0,match1.getMyEventLog().getEvents().size());
        assertEquals(0,match2.getMyEventLog().getEvents().size());
        //assertEquals(0,player1.getPlayerEvents().size());
        //assertEquals(0,player2.getPlayerEvents().size());
        //assertEquals(0,noMatchPlayer.getPlayerEvents().size());
        assertEquals(0,endedMatch.getMyEventLog().getEvents().size());
    }

    @Test
    public void testAddPenaltyKick()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match = matches.get(0);

        assertTrue(unit.addPenaltyKick(users.get(0),match,15));
        assertEquals(1,match.getMyEventLog().getEvents().size());
    }

    @Test
    public void testAddPenaltyKickBadArguments()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match1 = matches.get(0);
        matches = unit.getActiveMatches(users.get(1));
        Match match2 = matches.get(0);

        // null
        assertFalse(unit.addPenaltyKick(null,null,-1));
        assertFalse(unit.addPenaltyKick(users.get(0),null,-1));
        assertFalse(unit.addPenaltyKick(users.get(0),match1,-1));

        // not referee match
        assertFalse(unit.addPenaltyKick(users.get(0),match2,10));
        // not active match of referee
        assertFalse(unit.addPenaltyKick(users.get(0),endedMatch,10));

        // check no changes
        assertEquals(0,match1.getMyEventLog().getEvents().size());
        assertEquals(0,match2.getMyEventLog().getEvents().size());
        assertEquals(0,endedMatch.getMyEventLog().getEvents().size());
    }

    @Test
    public void testAddStoppageTime()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match = matches.get(0);

        assertTrue(unit.addStoppageTime(users.get(0),match,15,5));
        assertEquals(1,match.getMyEventLog().getEvents().size());
    }

    @Test
    public void testAddStoppageTimeBadArguments()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match1 = matches.get(0);
        matches = unit.getActiveMatches(users.get(1));
        Match match2 = matches.get(0);

        // null
        assertFalse(unit.addStoppageTime(null,null,-1,-1));
        assertFalse(unit.addStoppageTime(users.get(0),null,-1,-1));
        assertFalse(unit.addStoppageTime(users.get(0),match1,-1,-1));
        assertFalse(unit.addStoppageTime(users.get(0),match1,10,-1));

        // not referee match
        assertFalse(unit.addStoppageTime(users.get(0),match2,10,10));
        // not active match of referee
        assertFalse(unit.addStoppageTime(users.get(0),endedMatch,10,10));

        // check no changes
        assertEquals(0,match1.getMyEventLog().getEvents().size());
        assertEquals(0,match2.getMyEventLog().getEvents().size());
        assertEquals(0,endedMatch.getMyEventLog().getEvents().size());
    }

    @Test
    public void testAddEndGame()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match = matches.get(0);

        assertTrue(unit.addEndGame(users.get(0),match,90));
        assertEquals(1,match.getMyEventLog().getEvents().size());
    }

    @Test
    public void testAddEndGameBadArguments()
    {
        List<Match> matches = unit.getActiveMatches(users.get(0));
        Match match1 = matches.get(0);
        matches = unit.getActiveMatches(users.get(1));
        Match match2 = matches.get(0);

        // null
        assertFalse(unit.addEndGame(null,null,-1));
        assertFalse(unit.addEndGame(users.get(0),null,-1));
        assertFalse(unit.addEndGame(users.get(0),match1,-1));

        // not referee match
        assertFalse(unit.addEndGame(users.get(0),match2,90));
        // not active match of referee
        assertFalse(unit.addEndGame(users.get(0),endedMatch,90));

        // check no changes
        assertEquals(0,match1.getMyEventLog().getEvents().size());
        assertEquals(0,match2.getMyEventLog().getEvents().size());
        assertEquals(0,endedMatch.getMyEventLog().getEvents().size());
    }

}