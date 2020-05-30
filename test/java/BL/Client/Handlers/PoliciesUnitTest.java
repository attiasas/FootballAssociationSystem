package BL.Client.Handlers;


import BL.Communication.ClientServerCommunication;
import BL.Communication.CommunicationNullStub;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Team;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


import BL.Communication.CommunicationLeagueSeasonAndPoliciesStub;
import org.junit.rules.ExpectedException;


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

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * inits the parameters for the tests
     */
    @Before
    public void init() throws Exception {
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
    public void addNewGamePolicyTest() throws Exception {
        assertTrue(policiesUnitEmpty.addNewGamePolicy(3, 1));
        GamePolicy gamePolicy = new GamePolicy(3, 1);
        assertEquals(gamePolicy, policiesUnitEmpty.getGamePolicies().get(0));
    }

    /**
     * Tests the addition of a new scorePolicy to the system
     */
    @Test
    public void addNewScorePolicyTest() throws Exception {
        assertTrue(policiesUnitEmpty.addNewScorePolicy(2, 1, 0));
        ScorePolicy scorePolicy = new ScorePolicy(2, 1, 0);
        assertEquals(scorePolicy, policiesUnitEmpty.getScorePolicies().get(0));
    }

    /**
     * Tests the scheduleMatches function should be okay - the league should contains matches.
     */
    @Test
    public void scheduleMatchesTest() throws Exception {
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(season).get(0);
        assertTrue(policiesUnit.scheduleMatches(ls));
        assertEquals(3, ls.getMatches().size());
    }

    /**
     * Tests the calculation of the leagueTable - should return the league table sorted by team points and goals
     */
    @Test
    public void calculateLeagueTableTest() throws Exception {
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
     * Sets referees in the leagueSeason matches
     */
    @Test
    public void setRefereeInMatchesTest() throws Exception {
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(season).get(0);
        ls.scheduleLeagueMatches();
        Referee r1 = new Referee(null, "Test", null, true);
        Referee r2 = new Referee(null, "Test1", null, true);
        Referee r3 = new Referee(null, "Test2", null, true);
        r1.setId(1);
        r2.setId(2);
        r3.setId(3);
        leagueSeasonUnit.setRefereeInLeagueSeason(ls, r1);
        leagueSeasonUnit.setRefereeInLeagueSeason(ls, r2);
        leagueSeasonUnit.setRefereeInLeagueSeason(ls, r3);
        assertTrue(policiesUnit.setRefereeInMatches(ls));
        List<Match> matches = ls.getMatches();
        int i = 0;
        for (Match match : matches) {
            assertEquals(r1, match.getReferees().get(0));
            assertEquals(match, r1.getMatches().get(i));
            assertEquals(r2, match.getReferees().get(1));
            assertEquals(match, r2.getMatches().get(i));
            assertEquals(r3, match.getReferees().get(2));
            assertEquals(match, r3.getMatches().get(i++));
        }
    }

    /**
     * Expected Exceptions Tests
     */

    /**
     * Trying to insert gamePolicy that already exists - should throw Exception: "Game policy already exists."
     */
    @Test
    public void addNewGamePolicyAlreadyExistsTest() throws Exception {
        assertTrue(policiesUnitEmpty.addNewGamePolicy(3, 1));
        assertEquals(1, policiesUnitEmpty.getGamePolicies().size());
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Game policy already exists.");
        policiesUnitEmpty.addNewGamePolicy(3, 1);
    }

    /**
     * Trying to insert gamePolicy with negative values - should throw Exception: "Parameters must be greater than 0."
     */
    @Test
    public void addNewGamePolicyNegativeValuesTest() throws Exception {
        assertEquals(0, policiesUnitEmpty.getGamePolicies().size());
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Parameters must be greater than 0.");
        policiesUnitEmpty.addNewGamePolicy(-3, 1);

    }

    /**
     * Trying to insert gamePolicy with more than 7 games per day - should throw Exception: "The maximum games per day is 7."
     */
    @Test
    public void addNewGamePolicyMoreThan7GamesPerDay() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("The maximum games per day is 7.");
        policiesUnitEmpty.addNewGamePolicy(1, 8);
    }

    /**
     * Trying to insert scorePolicy that already exists - should  throw Exception: "Score policy already exists."
     */
    @Test
    public void addNewScorePolicyAlreadyExistsTest() throws Exception {
        assertTrue(policiesUnitEmpty.addNewScorePolicy(2, 1, 0));
        assertEquals(1, policiesUnitEmpty.getScorePolicies().size());
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Score policy already exists.");
        policiesUnitEmpty.addNewScorePolicy(2, 1, 0);

    }

    /**
     * Trying to connect to the server and got Exception - should  throw Exception: "There was a problem with the connection to
     * the server. Please try again later"
     */
    @Test
    public void addNewScorePolicyServerErrorTest() throws Exception {
        policiesUnitEmpty = new PoliciesUnit(new CommunicationNullStub());
        expectedException.expect(Exception.class);
        expectedException.expectMessage("There was a problem with the connection to the server. Please try again later");
        policiesUnitEmpty.addNewScorePolicy(2, 1, 0);

    }

    /**
     * Trying to connect to the server and got Exception - should  throw Exception: "There was a problem with the connection to
     * the server. Please try again later"
     */
    @Test
    public void addNewGamePolicyServerErrorTest() throws Exception {
        policiesUnitEmpty = new PoliciesUnit(new CommunicationNullStub());
        expectedException.expect(Exception.class);
        expectedException.expectMessage("There was a problem with the connection to the server. Please try again later");
        policiesUnitEmpty.addNewGamePolicy(3,1);

    }

    /**
     * Testing the scheduleMatches function with null parameter - should  throw Exception: "LeagueSeason can not be empty. Please choose leagueSeason."
     */
    @Test
    public void scheduleMatchesNullTest() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("LeagueSeason can not be empty. Please choose leagueSeason.");
        assertFalse(policiesUnit.scheduleMatches(null));
    }

    /**
     * Tests the calculation of the leagueTable with null parameters - should  throw Exception: "LeagueSeason can not be empty. Please choose leagueSeason."
     */
    @Test
    public void calculateLeagueTableNullTest() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("LeagueSeason can not be empty. Please choose leagueSeason.");
        assertNull(policiesUnit.calculateLeagueTable(null));
    }

    /**
     * Tests the setReferee function with null parameters - should throw Exception: "LeagueSeason can not be empty. Please choose leagueSeason."
     */
    @Test
    public void setRefereeInMatchesNullTest() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("LeagueSeason can not be empty. Please choose leagueSeason.");
        policiesUnit.setRefereeInMatches(null);
    }

    /**
     * Tests the setReferee function without matches - should throw Exception: "LeagueSeason doesn't have matches.
     * Please schedule matches for this leagueSeason first."
     */
    @Test
    public void setRefereeInMatchesWithoutMatchesTest() throws Exception {
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(season).get(0);
        ls.setGames(new ArrayList<>());
        expectedException.expect(Exception.class);
        expectedException.expectMessage("LeagueSeason doesn't have matches. Please schedule matches for this leagueSeason first.");
        policiesUnit.setRefereeInMatches(ls);
    }

    /**
     * Tests the setReferee function without referees - should throw Exception: "LeagueSeason doesn't have enough referees.
     * Please add more referees to the leagueSeason."
     */
    @Test
    public void setRefereeInMatchesWithoutRefereesTest() throws Exception {
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(season).get(0);
        ls.scheduleLeagueMatches();
        expectedException.expect(Exception.class);
        expectedException.expectMessage("LeagueSeason doesn't have enough referees. Please add more referees to the leagueSeason.");
        policiesUnit.setRefereeInMatches(ls);
    }
}
