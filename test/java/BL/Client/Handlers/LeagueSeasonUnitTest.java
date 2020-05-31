package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import BL.Communication.CommunicationLeagueSeasonAndPoliciesStub;
import BL.Communication.CommunicationNullStub;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Team;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Description:     This testClass tests the leagueSeasonUnit class.
 * ID:              25
 **/
public class LeagueSeasonUnitTest {

    LeagueSeasonUnit leagueSeasonUnit;
    LeagueSeasonUnit leagueSeasonUnitEmpty;
    PoliciesUnit policiesUnit;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * inits the communication of leagueSeasonUnit
     */
    @Before
    public void init() throws Exception {
        ClientServerCommunication communication = new CommunicationLeagueSeasonAndPoliciesStub();
        this.leagueSeasonUnitEmpty = new LeagueSeasonUnit(new CommunicationLeagueSeasonAndPoliciesStub());
        this.leagueSeasonUnit = new LeagueSeasonUnit(communication);
        policiesUnit = new PoliciesUnit(communication);

        leagueSeasonUnit.addNewSeason(2020);
        Season season = leagueSeasonUnit.getSeasons().get(0);
        leagueSeasonUnit.addNewLeague("Test");
        League league = leagueSeasonUnit.getLeagues().get(0);
        policiesUnit.addNewGamePolicy(1, 1);
        GamePolicy gp = policiesUnit.getGamePolicies().get(0);
        policiesUnit.addNewScorePolicy(3, 2, 1);
        ScorePolicy sp = policiesUnit.getScorePolicies().get(0);
        Date startDate = new Date(121);

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
     * Test the creation of a new league
     */
    @Test
    public void addNewLeagueTest() throws Exception {
        List<League> leagueListExpected = new ArrayList<>();
        assertTrue(leagueSeasonUnitEmpty.addNewLeague("CheckLeague"));
        leagueListExpected.add(new League("CheckLeague"));
        assertEquals(leagueListExpected, leagueSeasonUnitEmpty.getLeagues());
    }

    /**
     * Test the creation of a new season
     */
    @Test
    public void addNewSeasonTest() throws Exception {
        List<Season> seasonListExpected = new ArrayList<>();
        assertTrue(leagueSeasonUnitEmpty.addNewSeason(2020));
        seasonListExpected.add(new Season(2020));
        assertEquals(seasonListExpected, leagueSeasonUnitEmpty.getSeasons());
    }

    /**
     * Tests the creation of leagueSeason with right arguments
     */
    @Test
    public void addLeagueSeasonTest() throws Exception {
        List<LeagueSeason> leagueSeasonListExpected = new ArrayList<>();
        leagueSeasonUnitEmpty.addNewLeague("TestLeague");
        League league = leagueSeasonUnit.getLeagues().get(0);
        leagueSeasonUnitEmpty.addNewSeason(2020);
        Season season = leagueSeasonUnitEmpty.getSeasons().get(0);
        GamePolicy gp = new GamePolicy();
        ScorePolicy sp = new ScorePolicy();
        Date startDate = new Date();
        assertTrue(leagueSeasonUnitEmpty.addLeagueSeason(league, season, gp, sp, startDate));
        leagueSeasonListExpected.add(new LeagueSeason(league, season, gp, sp, startDate));
        assertEquals(leagueSeasonListExpected, leagueSeasonUnitEmpty.getLeagueSeasons(season));
    }

    /**
     * Tests the change of scorePolicy in a given leagueSeason - should be okay
     */
    @Test
    public void changeScorePolicyTest() throws Exception {
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(season).get(0);
        ls.setLeagueSeasonID(1);
        policiesUnit.addNewScorePolicy(5, 4, 3);
        ScorePolicy sp = policiesUnit.getScorePolicies().get(1);
        assertTrue(leagueSeasonUnit.changeScorePolicy(ls, sp));
        assertEquals(sp, ls.getScorePolicy());
    }

    /**
     * Tests the setter of referee in a given leagueSeason - should be okay
     */
    @Test
    public void setRefereeInLeagueSeasonTest() throws Exception {
        Referee r = new Referee(null, "Test3", null, true);
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(season).get(0);
        assertTrue(leagueSeasonUnit.setRefereeInLeagueSeason(ls, r));
        assertEquals(r, ls.getReferees().get(0));
        assertEquals(ls, r.getLeagueSeasons().get(0));
    }

    /**
     * Tests the setter of team in a given leagueSeason
     */
    @Test
    public void addTeamToLeagueSeasonTest() throws Exception {
        Team t = new Team("Test1", true, false);
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(season).get(0);
        assertTrue(leagueSeasonUnit.addTeamToLeagueSeason(ls, t));
        assertEquals(t, ls.getTeamsParticipate().get(3));
        assertEquals(ls, t.getLeagueSeasons().get(0));
    }

    /**
     * Exceptions tests
     */

    /**
     * Test the creation of a new league with bad arguments - should throw Exception: "League name can not be empty. Please try again."
     */
    @Test
    public void addNewLeagueNullTest() throws Exception {
        List<League> leagueListExpected = new ArrayList<>();
        Assert.assertEquals(leagueListExpected, leagueSeasonUnitEmpty.getLeagues());
        expectedException.expect(Exception.class);
        expectedException.expectMessage("League name can not be empty.");
        leagueSeasonUnitEmpty.addNewLeague(null);

    }

    /**
     * Test the creation of a new league with bad arguments - should throw Exception: "League name can not be empty. Please try again."
     */
    @Test
    public void addNewLeagueEmptyStringTest() throws Exception {
        List<League> leagueListExpected = new ArrayList<>();
        Assert.assertEquals(leagueListExpected, leagueSeasonUnitEmpty.getLeagues());
        expectedException.expect(Exception.class);
        expectedException.expectMessage("League name can not be empty.");
        leagueSeasonUnitEmpty.addNewLeague("");
    }

    /**
     * Test the creation of a new league when the system already has that league name -
     * should throw Exception: "League with that name already exists. Please insert different name."
     */
    @Test
    public void addNewLeagueAlreadyExistsTest() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("League with that name already exists. Please insert different name.");
        leagueSeasonUnit.addNewLeague("Test");


    }

    /**
     * Test the creation of a new league when there is a server error -
     * should throw Exception: "There was a problem with the connection to the server. Please try again later"
     */
    @Test
    public void addNewLeagueServerErrorTest() throws Exception {
        leagueSeasonUnit = new LeagueSeasonUnit(new CommunicationNullStub());
        expectedException.expect(Exception.class);
        expectedException.expectMessage("There was a problem with the connection to the server. Please try again later");
        leagueSeasonUnit.addNewLeague("Test");
    }

    /**
     * Test the creation of a new Season with bad arguments - should throw Exception:
     * "Year must be greater than 1950. Please try again."
     */
    @Test
    public void addNewSeasonNegativeArgTest() throws Exception {
        List<Season> seasonListExpected = new ArrayList<>();
        Assert.assertEquals(seasonListExpected, leagueSeasonUnitEmpty.getSeasons());
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Year must be greater than 1950. Please try again.");
        assertFalse(leagueSeasonUnitEmpty.addNewSeason(-1));
    }

    /**
     * Test the creation of a new Season that already exists - should return true
     */
    @Test
    public void addNewSeasonAlreadyExistsTest() throws Exception {
        List<Season> seasonListExpected = new ArrayList<>();
        seasonListExpected.add(new Season(2020));
        Assert.assertEquals(seasonListExpected, leagueSeasonUnit.getSeasons());
        assertTrue(leagueSeasonUnit.addNewSeason(2020));
    }

    /**
     * Test the creation of a new Season when there is a server error
     * "There was a problem with the connection to the server. Please try again later"
     */
    @Test
    public void addNewSeasonServerErrorTest() throws Exception {
        leagueSeasonUnit = new LeagueSeasonUnit(new CommunicationNullStub());
        expectedException.expect(Exception.class);
        expectedException.expectMessage("There was a problem with the connection to the server. Please try again later");
        leagueSeasonUnit.addNewSeason(2020);
    }

    /**
     * Tests the creation of leagueSeason with bad arguments - should throw Exception:
     * "Parameters should not be null. Please try again"
     */
    @Test
    public void addLeagueSeasonNullTest() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Parameters should not be null. Please try again");
        leagueSeasonUnit.addLeagueSeason(null, null, null, null, null);
       // assertNull(leagueSeasonUnit.getLeagueSeasons(null));
    }

    /**
     * Test the creation of a new LeagueSeason when there is a server error
     * "There was a problem with the connection to the server. Please try again later"
     */
    @Test
    public void addNewLeagueSeasonServerErrorTest() throws Exception {
        leagueSeasonUnit = new LeagueSeasonUnit(new CommunicationNullStub());
        expectedException.expect(Exception.class);
        expectedException.expectMessage("There was a problem with the server. Please try again later");
        League l = new League("check");
        Season s = new Season(2002);
        ScorePolicy sp = new ScorePolicy(3,2,1);
        GamePolicy gp = new GamePolicy(3,3);
        Date sd = new Date();
        leagueSeasonUnit.addLeagueSeason(l,s,gp,sp,sd);
    }

    /**
     * Test the creation of a new leagueSeason that already exists - should throw Exception:
     * "LeagueSeason already exists. Please try with different year or name."
     */
    @Test
    public void addNewLeagueSeasonAlreadyExistsTest() throws Exception {
        Season season = leagueSeasonUnit.getSeasons().get(0);
        League league = leagueSeasonUnit.getLeagues().get(0);
        GamePolicy gp = policiesUnit.getGamePolicies().get(0);
        ScorePolicy sp = policiesUnit.getScorePolicies().get(0);
        Date startDate = new Date(121);
        expectedException.expect(Exception.class);
        expectedException.expectMessage("LeagueSeason already exists. Please try with different year or name.");
        leagueSeasonUnit.addLeagueSeason(league, season, gp, sp, startDate);
    }

    /**
     * Tests the change of scorePolicy in a given leagueSeason with null parameters  - should throw Exception:
     * "Parameters should not be null. Please try again"
     */
    @Test
    public void changeScorePolicyNullTest() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Parameters should not be null. Please try again");
        leagueSeasonUnit.changeScorePolicy(null, null);
    }

    /**
     * Tests the change of scorePolicy in a given leagueSeason that already running
     * "Sorry, you can not change the score policy because the league is already running."
     */
    @Test
    public void changeScorePolicyLeagueRunningTest() throws Exception {
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(new Season(2020)).get(0);
        ls.scheduleLeagueMatches();
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Sorry, you can not change the score policy because the league is already running.");
        ScorePolicy sp = new ScorePolicy(6,3,1);
        leagueSeasonUnit.changeScorePolicy(ls, sp);
    }

    /**
     * Tests the setter of referee in a given leagueSeason with null parameters - should throw Exception:
     * "Parameters should not be null. Please try again"
     */
    @Test
    public void setRefereeInLeagueSeasonNullTest() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Parameters should not be null. Please try again");
        leagueSeasonUnit.setRefereeInLeagueSeason(null, null);
    }

    /**
     * Tests the setter of team in a given leagueSeason with null parameters - should throw Exception:
     * "Parameters should not be null. Please try again"
     */
    @Test
    public void addTeamToLeagueSeasonNullTest() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Parameters should not be null. Please try again");
        leagueSeasonUnit.addTeamToLeagueSeason(null, null);
    }

    /**
     * Tests the setter of team in a given leagueSeason with not active team - should throw Exception:
     * "Parameters should not be null. Please try again"
     */
    @Test
    public void addTeamToLeagueSeasonNotActiveTest() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Sorry, the team is not active.");
        Team t = new Team("check",false,true);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeasons(new Season(2020)).get(0);
        leagueSeasonUnit.addTeamToLeagueSeason(ls, t);
    }
}

