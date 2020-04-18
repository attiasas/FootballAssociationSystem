package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import BL.Communication.CommunicationLeagueSeasonAndPoliciesStub;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Team;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


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

    /**
     * inits the communication of leagueSeasonUnit
     */
    @Before
    public void init() {
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
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeason(season).get(0);

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
    public void addNewLeagueTest() {
        List<League> leagueListExpected = new ArrayList<>();
        assertTrue(leagueSeasonUnitEmpty.addNewLeague("CheckLeague"));
        leagueListExpected.add(new League("CheckLeague"));
        assertEquals(leagueListExpected, leagueSeasonUnitEmpty.getLeagues());
    }

    /**
     * Test the creation of a new league with bad arguments - should return false
     */
    @Test
    public void addNewLeagueLeagueNullTest() {
        List<League> leagueListExpected = new ArrayList<>();
        assertFalse(leagueSeasonUnitEmpty.addNewLeague(null));
        Assert.assertEquals(leagueListExpected, leagueSeasonUnitEmpty.getLeagues());
    }

    /**
     * Test the creation of a new league with bad arguments - should return false
     */
    @Test
    public void addNewLeagueLeagueEmptyStringTest() {
        List<League> leagueListExpected = new ArrayList<>();
        assertFalse(leagueSeasonUnitEmpty.addNewLeague(""));
        Assert.assertEquals(leagueListExpected, leagueSeasonUnitEmpty.getLeagues());
    }

    /**
     * Test the creation of a new season
     */
    @Test
    public void addNewSeasonTest() {
        List<Season> seasonListExpected = new ArrayList<>();
        assertTrue(leagueSeasonUnitEmpty.addNewSeason(2020));
        seasonListExpected.add(new Season(2020));
        assertEquals(seasonListExpected, leagueSeasonUnitEmpty.getSeasons());
    }

    /**
     * Test the creation of a new Season with bad arguments - should return false
     */
    @Test
    public void addNewSeasonNegativeArgTest() {
        List<League> seasonListExpected = new ArrayList<>();
        assertFalse(leagueSeasonUnitEmpty.addNewSeason(-1));
        Assert.assertEquals(seasonListExpected, leagueSeasonUnitEmpty.getSeasons());
    }

    /**
     * Tests the creation of leagueSeason with bad arguments
     */
    @Test
    public void addLeagueSeasonNullTest() {
        List<League> leagueSeasonListExpected = new ArrayList<>();
        assertFalse(leagueSeasonUnit.addLeagueSeason(null, null, null, null, null));
        assertNull(leagueSeasonUnit.getLeagueSeason(null));
    }

    /**
     * Tests the creation of leagueSeason with right arguments
     */
    @Test
    public void addLeagueSeasonTest() {
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
        assertEquals(leagueSeasonListExpected, leagueSeasonUnitEmpty.getLeagueSeason(season));
    }

    /**
     * Tests the change of scorePolicy in a given leagueSeason - should be okay
     */
    @Test
    public void changeScorePolicyTest() {
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeason(season).get(0);
        policiesUnit.addNewScorePolicy(5,4,3);
        ScorePolicy sp = policiesUnit.getScorePolicies().get(1);
        assertTrue(leagueSeasonUnit.changeScorePolicy(ls,sp));
        assertEquals(sp,ls.getScorePolicy());
    }

    /**
     * Tests the change of scorePolicy in a given leagueSeason with null parameters  - should be false
     */
    @Test
    public void changeScorePolicyNullTest() {
        assertFalse(leagueSeasonUnit.changeScorePolicy(null,null));
        ScorePolicy sp = policiesUnit.getScorePolicies().get(0);
        assertFalse(leagueSeasonUnit.changeScorePolicy(null, sp));
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeason(season).get(0);
        assertFalse(leagueSeasonUnit.changeScorePolicy(ls,null));
    }

    /**
     * Tests the setter of referee in a given leagueSeason with null parameters - should be false
     */
    @Test
    public void setRefereeInLeagueSeasonNullTest(){
        assertFalse(leagueSeasonUnit.setRefereeInLeagueSeason(null,null));
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeason(season).get(0);
        assertFalse(leagueSeasonUnit.setRefereeInLeagueSeason(ls,null));
        Referee r = new Referee(null,"Test3",null,true);
        assertFalse(leagueSeasonUnit.setRefereeInLeagueSeason(null,r));
    }

    /**
     * Tests the setter of referee in a given leagueSeason - should be okay
     */
    @Test
    public void setRefereeInLeagueSeasonTest(){
        Referee r = new Referee(null,"Test3",null,true);
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeason(season).get(0);
        assertTrue(leagueSeasonUnit.setRefereeInLeagueSeason(ls,r));
        assertEquals(r,ls.getReferees().get(0));
        assertEquals(ls,r.getLeagueSeasons().get(0));
    }

    /**
     * Tests the setter of team in a given leagueSeason with null parameters
     */
    @Test
    public void addTeamToLeagueSeasonNullTest(){
        assertFalse(leagueSeasonUnit.addTeamToLeagueSeason(null,null));
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeason(season).get(0);
        assertFalse(leagueSeasonUnit.addTeamToLeagueSeason(ls,null));
        Team t = new Team("Test1",true,false);
        assertFalse(leagueSeasonUnit.addTeamToLeagueSeason(null,t));
    }

    /**
     * Tests the setter of team in a given leagueSeason
     */
    @Test
    public void addTeamToLeagueSeasonTest(){
        Team t = new Team("Test1",true,false);
        Season season = leagueSeasonUnit.getSeasons().get(0);
        LeagueSeason ls = leagueSeasonUnit.getLeagueSeason(season).get(0);
        assertTrue(leagueSeasonUnit.addTeamToLeagueSeason(ls,t));
        assertEquals(t,ls.getTeamsParticipate().get(3));
        assertEquals(ls,t.getLeagueSeasons().get(0));
    }
}

