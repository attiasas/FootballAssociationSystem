package BL.Client.Handlers;


import BL.Communication.ClientServerCommunication;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Team;
import org.junit.Before;
import org.junit.Test;


import BL.Communication.CommunicationLeagueSeasonAndPoliciesStub;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Description:     This testClass tests the policiesUnit Class.
 * ID:              24
 */
public class PoliciesUnitTest {

    PoliciesUnit policiesUnit;
    PoliciesUnit policiesUnitEmpty;
    LeagueSeasonUnit leagueSeasonUnit;

    /**
     * inits the parameters for the tests
     */
    @Before
    public void init() {
        ClientServerCommunication communication = new CommunicationLeagueSeasonAndPoliciesStub();
        policiesUnit = new PoliciesUnit(communication);
        policiesUnitEmpty = new PoliciesUnit(new CommunicationLeagueSeasonAndPoliciesStub());
        leagueSeasonUnit = new LeagueSeasonUnit(communication);

        leagueSeasonUnit.addNewSeason(2020);
        Season season = leagueSeasonUnit.getSeasons().get(0);
        leagueSeasonUnit.addNewLeague("Test");
        League league = leagueSeasonUnit.getLeagues().get(0);
        policiesUnit.addNewGamePolicy(1, 1);
        GamePolicy gp = policiesUnit.getGamePolicies().get(0);
        policiesUnit.addNewScorePolicy(3, 2, 1);
        ScorePolicy sp = policiesUnit.getScorePolicies().get(0);
        Date startDate = new Date();

        leagueSeasonUnit.addLeagueSeason(league, season, gp, sp, startDate);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(season).get(0);

        Team t1 = new Team("Test", true, false);
        Team t2 = new Team("Test2", true, false);
        Team t3 = new Team("Test3", true, false);

        leagueSeasonUnit.addTeamToLeagueSeason(ls, t1);
        leagueSeasonUnit.addTeamToLeagueSeason(ls, t2);
        leagueSeasonUnit.addTeamToLeagueSeason(ls, t3);

    }

    /**
     * Tests the addition of a new gamePolicy to the system
     */
    @Test
    public void addNewGamePolicyTest() {
        assertTrue(policiesUnitEmpty.addNewGamePolicy(3, 1));
        GamePolicy gamePolicy = new GamePolicy(3, 1);
        assertEquals(gamePolicy, policiesUnitEmpty.getGamePolicies().get(0));
    }

    /**
     * Trying to insert gamePolicy that already exists - should be false
     */
    @Test
    public void addNewGamePolicyAlreadyExistsTest() {
        assertTrue(policiesUnitEmpty.addNewGamePolicy(3, 1));
        assertFalse(policiesUnitEmpty.addNewGamePolicy(3, 1));
        assertEquals(1, policiesUnitEmpty.getGamePolicies().size());
    }

    /**
     * Trying to insert gamePolicy with negative values - should be false
     */
    @Test
    public void addNewGamePolicyNegativeValuesTest() {
        assertFalse(policiesUnitEmpty.addNewGamePolicy(-3, 1));
        assertEquals(0, policiesUnitEmpty.getGamePolicies().size());
    }

    /**
     * Tests the addition of a new scorePolicy to the system
     */
    @Test
    public void addNewScorePolicyTest() {
        assertTrue(policiesUnitEmpty.addNewScorePolicy(2, 1, 0));
        ScorePolicy scorePolicy = new ScorePolicy(2, 1, 0);
        assertEquals(scorePolicy, policiesUnitEmpty.getScorePolicies().get(0));
    }

    /**
     * Trying to insert scorePolicy that already exists - should be false
     */
    @Test
    public void addNewScorePolicyAlreadyExistsTest() {
        assertTrue(policiesUnitEmpty.addNewScorePolicy(2, 1, 0));
        assertFalse(policiesUnitEmpty.addNewScorePolicy(2, 1, 0));
        assertEquals(1, policiesUnitEmpty.getScorePolicies().size());
    }

    /**
     * Testing the scheduleMatches function with null parameter - should be false
     */
    @Test
    public void scheduleMatchesNullTest() {
        assertFalse(policiesUnit.scheduleMatches(null));
    }

    /**
     * Tests the scheduleMatches function should be okay - the league should contains matches.
     */
    @Test
    public void scheduleMatchesTest() {
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(season).get(0);
        assertTrue(policiesUnit.scheduleMatches(ls));
        assertEquals(3, ls.getMatches().size());
    }

    /**
     * Tests the calculation of the leagueTable - should return the league table sorted by team points and goals
     */
    @Test
    public void calculateLeagueTableTest() {
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(season).get(0);
        ls.scheduleLeagueMatches();
        List<Map.Entry<Team, Integer[]>> leagueTable = policiesUnit.calculateLeagueTable(ls);
        assertEquals(3, leagueTable.size());
        assertEquals(4, (int) leagueTable.get(0).getValue()[0]);//points
        assertEquals(4, (int) leagueTable.get(0).getValue()[0]);//points
        assertEquals(4, (int) leagueTable.get(0).getValue()[0]);//points
        assertEquals(0, (int) leagueTable.get(0).getValue()[1]);//goals
        assertEquals(0, (int) leagueTable.get(0).getValue()[1]);//goals
        assertEquals(0, (int) leagueTable.get(0).getValue()[1]);//goals

    }

    /**
     * Tests the calculation of the leagueTable with null parameters - should be null
     */
    @Test
    public void calculateLeagueTableNullTest() {
        assertNull(policiesUnit.calculateLeagueTable(null));
    }

    /**
     * Sets referees in the leagueSeason matches
     */
    @Test
    public void setRefereeInMatchesTest() {
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(season).get(0);
        ls.scheduleLeagueMatches();
        Referee r1 = new Referee(null, "Test", null, true);
        Referee r2 = new Referee(null, "Test1", null, true);
        Referee r3 = new Referee(null, "Test2", null, true);
        leagueSeasonUnit.setRefereeInLeagueSeason(ls, r1);
        leagueSeasonUnit.setRefereeInLeagueSeason(ls, r2);
        leagueSeasonUnit.setRefereeInLeagueSeason(ls, r3);
        assertTrue(policiesUnit.setRefereeInMatches(ls));
        List<Match> matches = ls.getMatches();
        int i=0;
        for (Match match : matches){
            assertEquals(r1,match.getMainReferee());
            assertEquals(match,r1.getMainMatches().get(i));
            assertEquals(r2,match.getFirstLineManReferee());
            assertEquals(match,r2.getLinesManMatches().get(i));
            assertEquals(r3,match.getSecondLineManReferee());
            assertEquals(match,r3.getLinesManMatches().get(i++));
        }
    }

    /**
     * Tests the setReferee function with null parameters
     */
    @Test
    public void setRefereeInMatchesNullTest() {
        assertFalse(policiesUnit.setRefereeInMatches(null));
    }

    /**
     * Tests the setReferee function without matches
     */
    @Test
    public void setRefereeInMatchesWithoutMatchesTest() {
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(season).get(0);
        ls.setGames(new ArrayList<>());
        assertFalse(policiesUnit.setRefereeInMatches(ls));
    }

}