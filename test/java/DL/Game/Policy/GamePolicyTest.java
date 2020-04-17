package DL.Game.Policy;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.Match;
import DL.Team.Assets.Stadium;
import DL.Team.Team;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Description:     This test class tests the gamePolicy class.
 * ID:              19
 **/
public class GamePolicyTest {

    private LeagueSeason leagueSeason;
    private LeagueSeason leagueSeason2;
    private GamePolicy gamePolicy;
    private GamePolicy gamePolicy2;

    @Before
    public void initTests() {
        gamePolicy = new GamePolicy(1, 3);
        gamePolicy2 = new GamePolicy(2, 2);
        Date startLeagueDate = new Date(120, 1, 1, 3, 30);
        leagueSeason = new LeagueSeason(null, null, gamePolicy, null, startLeagueDate);
        leagueSeason2 = new LeagueSeason(null, null, gamePolicy2, null, startLeagueDate);
    }

    /**
     * Tests gamePolicy constructor with right parameters
     */
    @Test
    public void gamePolicyCtorTest() {
        GamePolicy gamePolicy = new GamePolicy(2, 7);
        Assert.assertEquals(2, gamePolicy.getNumberOfRounds());
        Assert.assertEquals(7, gamePolicy.getGamesPerDay());
    }

    /**
     * Tests default constructor of gamePolicy
     */
    @Test
    public void gamePolicyDefaultCtorTest() {
        GamePolicy gamePolicy = new GamePolicy();
        Assert.assertEquals(1, gamePolicy.getNumberOfRounds());
        Assert.assertEquals(1, gamePolicy.getGamesPerDay());
    }

    /**
     * Tests gamePolicy Constructor negative rounds
     */
    @Test
    public void gamePolicyNegativeRoundsTest() {
        GamePolicy gamePolicy = new GamePolicy(-1, 8);
        Assert.assertEquals(1, gamePolicy.getNumberOfRounds());
        Assert.assertEquals(1, gamePolicy.getGamesPerDay());
    }

    /**
     * Test equals function - should be the same
     */
    @Test
    public void gamePolicyEqualsTest() {
        GamePolicy firstGamePolicy = new GamePolicy(1, 2);
        GamePolicy secondGamePolicy = new GamePolicy(1, 2);
        assertEquals(true, firstGamePolicy.equals(secondGamePolicy));
    }

    /**
     * Test equals function - should not be the same
     */
    @Test
    public void gamePolicyNotEqualsRoundsTest() {
        GamePolicy firstGamePolicy = new GamePolicy(2, 2);
        GamePolicy secondGamePolicy = new GamePolicy(1, 2);
        GamePolicy thirdGamePolicy = new GamePolicy(1, 1);
        assertEquals(false, firstGamePolicy.equals(secondGamePolicy));
        assertEquals(false, secondGamePolicy.equals(thirdGamePolicy));
    }

    /**
     * Test Hash code of equls gamePolicies
     */
    @Test
    public void gamePolicyHashCodeEqualsTest() {
        GamePolicy firstGamePolicy = new GamePolicy(1, 3);
        GamePolicy secondGamePolicy = new GamePolicy(1, 3);
        assertEquals(true, firstGamePolicy.hashCode() == secondGamePolicy.hashCode());
    }

    /**
     * Test Hash code of different gamePolicies
     */
    @Test
    public void gamePolicyHashCodeDifferentTest() {
        GamePolicy firstGamePolicy = new GamePolicy(1, 2);
        GamePolicy secondGamePolicy = new GamePolicy(1, 3);
        GamePolicy thirdGamePolicy = new GamePolicy(1, 2);
        assertEquals(false, firstGamePolicy.hashCode() == secondGamePolicy.hashCode());
        assertEquals(false, secondGamePolicy.hashCode() == thirdGamePolicy.hashCode());
    }

    /**
     * Tests the scheduleMatches function with null parameter - shall return null
     */
    @Test
    public void scheduleMatchesNullParam() {
        List<Match> matches = gamePolicy.scheduleMatches(null);
        assertNull(matches);
    }

    /**
     * Tests the scheduleMatches function while the league doesn't have teams - shall return list of 0 matches
     */
    @Test
    public void scheduleMatchesTestWithoutTeamsOneRound() {
        List<Match> matches = gamePolicy.scheduleMatches(leagueSeason);
        assertEquals(0, matches.size());
    }

    /**
     * Tests the scheduleMatches function while the league has only one team - shall return list of 0 matches
     */
    @Test
    public void scheduleMatchesTestWithOnlyOneTeamOneRound() {
        Team t1 = new Team("Test1", false, false);
        leagueSeason.addTeam(t1);
        List<Match> matches = gamePolicy.scheduleMatches(leagueSeason);
        assertEquals(0, matches.size());
    }

    /**
     * Tests the scheduleMatches function while the league has two teams - shall return list of 1 matches
     */
    @Test
    public void scheduleMatchesTestWithOnlyTwoTeamsOneRound() {
        Team t1 = new Team("Test1", false, false);
        Team t2 = new Team("Test2", false, false);
        leagueSeason.addTeam(t1);
        leagueSeason.addTeam(t2);
        List<Match> matches = gamePolicy.scheduleMatches(leagueSeason);
        assertEquals(1, matches.size());
        Date d1 = new Date(120, 1, 1, 10, 0);
        assertEquals(d1, matches.get(0).getStartTime());
    }

    /**
     * Tests the scheduleMatches function while the league has three teams (odd number of teams) - shall return list of 2 matches
     */
    @Test
    public void scheduleMatchesTestWithOnlyThreeTeamsOneRound() {
        Team t1 = new Team("Test1", false, false);
        Team t2 = new Team("Test2", false, false);
        Team t3 = new Team("Test3", false, false);
        leagueSeason.addTeam(t1);
        leagueSeason.addTeam(t2);
        leagueSeason.addTeam(t3);
        List<Match> matches = gamePolicy.scheduleMatches(leagueSeason);
        assertEquals(3, matches.size());
        Date d1 = new Date(120, 1, 1, 10, 0);
        Date d2 = new Date(120, 1, 1, 12, 0);
        Date d3 = new Date(120, 1, 1, 14, 0);
        assertEquals(d1, matches.get(0).getStartTime());
        assertEquals(d2, matches.get(1).getStartTime());
        assertEquals(d3, matches.get(2).getStartTime());
        assertEquals(t1, matches.get(0).getHomeTeam());
        assertEquals(t2, matches.get(0).getAwayTeam());
        assertEquals(t3, matches.get(1).getHomeTeam());
        assertEquals(t1, matches.get(1).getAwayTeam());
        assertEquals(t2, matches.get(2).getHomeTeam());
        assertEquals(t3, matches.get(2).getAwayTeam());
    }

    /**
     * Tests the scheduleMatches function - checks if the dates split as required, while there is two rounds of matches.
     * shall be two matches at one day while the first game at 10 and the second at 12.
     */
    @Test
    public void scheduleMatchesTestDatesTwoRounds() {
        Team t1 = new Team("Test1", false, false);
        Team t2 = new Team("Test2", false, false);
        Team t3 = new Team("Test3", false, false);
        Team t4 = new Team("Test4", false, false);
        leagueSeason2.addTeam(t1);
        leagueSeason2.addTeam(t2);
        leagueSeason2.addTeam(t3);
        leagueSeason2.addTeam(t4);
        List<Match> matches = gamePolicy2.scheduleMatches(leagueSeason2);
        assertEquals(12, matches.size());
        //two games at one day
        Date d1 = new Date(120, 1, 1, 10, 0);
        Date d2 = new Date(120, 1, 1, 12, 0);
        Date d3 = new Date(120, 1, 2, 10, 0);
        Date d4 = new Date(120, 1, 2, 12, 0);
        Date d5 = new Date(120, 1, 3, 10, 0);
        Date d6 = new Date(120, 1, 3, 12, 0);
        Date d7 = new Date(120, 1, 4, 10, 0);
        Date d8 = new Date(120, 1, 4, 12, 0);
        Date d9 = new Date(120, 1, 5, 10, 0);
        Date d10 = new Date(120, 1, 5, 12, 0);
        Date d11 = new Date(120, 1, 6, 10, 0);
        Date d12 = new Date(120, 1, 6, 12, 0);
        assertEquals(d1, matches.get(0).getStartTime());
        assertEquals(d2, matches.get(1).getStartTime());
        assertEquals(d3, matches.get(2).getStartTime());
        assertEquals(d4, matches.get(3).getStartTime());
        assertEquals(d5, matches.get(4).getStartTime());
        assertEquals(d6, matches.get(5).getStartTime());
        assertEquals(d7, matches.get(6).getStartTime());
        assertEquals(d8, matches.get(7).getStartTime());
        assertEquals(d9, matches.get(8).getStartTime());
        assertEquals(d10, matches.get(9).getStartTime());
        assertEquals(d11, matches.get(10).getStartTime());
        assertEquals(d12, matches.get(11).getStartTime());
    }

    /**
     * Tests the scheduleMatches function - checks if the HomeAway technique splits as required, while there is two rounds of matches.
     * every team shall have 2 matches as homeTeam, and 2 matches as awayTeam
     */
    @Test
    public void scheduleMatchesTestHomeAwayTwoRounds() {
        Team t1 = new Team("Test1", false, false);
        Team t2 = new Team("Test2", false, false);
        Team t3 = new Team("Test3", false, false);
        leagueSeason2.addTeam(t1);
        leagueSeason2.addTeam(t2);
        leagueSeason2.addTeam(t3);
        List<Match> matches = gamePolicy2.scheduleMatches(leagueSeason2);
        assertEquals(6, matches.size());
        assertEquals(t1, matches.get(0).getHomeTeam());
        assertEquals(t2, matches.get(0).getAwayTeam());
        assertEquals(t3, matches.get(1).getHomeTeam());
        assertEquals(t1, matches.get(1).getAwayTeam());
        assertEquals(t2, matches.get(2).getHomeTeam());
        assertEquals(t3, matches.get(2).getAwayTeam());
        assertEquals(t2, matches.get(3).getHomeTeam());
        assertEquals(t1, matches.get(3).getAwayTeam());
        assertEquals(t1, matches.get(4).getHomeTeam());
        assertEquals(t3, matches.get(4).getAwayTeam());
        assertEquals(t3, matches.get(5).getHomeTeam());
        assertEquals(t2, matches.get(5).getAwayTeam());
    }

    /**
     * Tests the scheduleMatches function - checks the setter of the stadiums.
     * both of the teams have stadiums than it should set the match with these stadiums.
     */
    @Test
    public void scheduleMatchesTestStadiums() {
        Team t1 = new Team("Test1", false, false);
        Team t2 = new Team("Test2", false, false);
        Stadium st1 = new Stadium("st1", 1200, t1);
        Stadium st2 = new Stadium("st2", 1200, t2);
        t1.addStadium(st1);
        t2.addStadium(st2);
        leagueSeason2.addTeam(t1);
        leagueSeason2.addTeam(t2);
        List<Match> matches = gamePolicy2.scheduleMatches(leagueSeason2);
        assertEquals(st1, matches.get(0).getStadium());
        assertEquals(st2, matches.get(1).getStadium());
    }

    /**
     * Tests the scheduleMatches function - checks that the match added to the matchList of the teams.
     */
    @Test
    public void scheduleMatchesTestAddMatchToTeams() {
        Team t1 = new Team("Test1", false, false);
        Team t2 = new Team("Test2", false, false);
        leagueSeason2.addTeam(t1);
        leagueSeason2.addTeam(t2);
        List<Match> matches = gamePolicy2.scheduleMatches(leagueSeason2);
        assertEquals(matches.get(0), t1.getHomeMatches().get(0));
        assertEquals(matches.get(0), t2.getAwayMatches().get(0));
        assertEquals(matches.get(1), t2.getHomeMatches().get(0));
        assertEquals(matches.get(1), t1.getAwayMatches().get(0));
    }


}