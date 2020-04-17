package DL.Game.LeagueSeason;

import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Team;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Description:     This testClass tests the leagueSeason class.
 * ID:              23
 **/
public class LeagueSeasonTest {

    /**
     * Tests default ctor
     */
    @Test
    public void defaultCtorTest() {
        LeagueSeason leagueSeason = new LeagueSeason();
    }

    /**
     * Tests ctor with null param
     */
    @Test
    public void ctorWithNullParamTest() {
        LeagueSeason leagueSeason = new LeagueSeason(null,null,null,null,null);
    }

    /**
     * Test constructor with parameters
     */
    @Test
    public void ctorWithParam() {
        League league = new League("Test");
        Season season = new Season(2020);
        GamePolicy gp = new GamePolicy(1, 1);
        ScorePolicy sp = new ScorePolicy();
        Date startDate = new Date(2020, 1, 1, 10, 0);
        LeagueSeason leagueSeason = new LeagueSeason(league, season, gp, sp, startDate);
        assertEquals(league, leagueSeason.getLeague());
        assertEquals(season, leagueSeason.getSeason());
        assertEquals(startDate.getYear(), leagueSeason.getStartDate().getYear());
        assertEquals(0, leagueSeason.getMatches().size());
        assertEquals(0, leagueSeason.getTeamsParticipate().size());
        assertEquals(0, leagueSeason.getReferees().size());
        startDate = new Date(2021, 1, 1, 10, 0);
        assertNotEquals(startDate.getYear(), leagueSeason.getStartDate().getYear());
    }

    /**
     * Tests if the team added to the leagueSeason and if the leagueSeason added to the team
     */
    @Test
    public void addTeamTest() {
        LeagueSeason ls = new LeagueSeason();
        Team t1 = new Team();
        assertTrue(ls.addTeam(t1));
        assertEquals(t1, ls.getTeamsParticipate().get(0));
        assertEquals(1, ls.getTeamsParticipate().size());
        assertEquals(ls, t1.getLeagueSeasons().get(0));
    }

    /**
     * Tests if same teams can be added together - should return false.
     */
    @Test
    public void addSameTeamTest() {
        LeagueSeason ls = new LeagueSeason();
        Team t1 = new Team();
        assertTrue(ls.addTeam(t1));
        assertFalse(ls.addTeam(t1));
        assertEquals(1, ls.getTeamsParticipate().size());
    }

    /**
     * Tests addTeam function with null parameter
     */
    @Test
    public void addNullTeamTest() {
        LeagueSeason ls = new LeagueSeason();
        assertFalse(ls.addTeam(null));
        assertEquals(0, ls.getTeamsParticipate().size());
    }

    /**
     * Tests if the team added to the leagueSeason and if the leagueSeason added to the team
     */
    @Test
    public void addRefereeTest() {
        LeagueSeason ls = new LeagueSeason();
        Referee r1 = new Referee();
        assertTrue(ls.addReferee(r1));
        assertEquals(r1, ls.getReferees().get(0));
        assertEquals(1, ls.getReferees().size());
        assertEquals(ls, r1.getLeagueSeasons().get(0));
    }

    /**
     * Tests if same teams can be added together - should return false.
     */
    @Test
    public void addSameRefereeTest() {
        LeagueSeason ls = new LeagueSeason();
        Referee r1 = new Referee();
        assertTrue(ls.addReferee(r1));
        assertFalse(ls.addReferee(r1));
        assertEquals(r1, ls.getReferees().get(0));
        assertEquals(1, ls.getReferees().size());
    }

    /**
     * System should not exit.
     */
    @Test
    public void setGamesNullTest(){
        LeagueSeason ls = new LeagueSeason();
        ls.setGames(null);
    }

    /**
     * Tests the function of setGames to LeagueSeason
     */
    @Test
    public void setGamesTest(){
        LeagueSeason ls = new LeagueSeason();
        List<Match> matchList = new ArrayList<>();
        Match m1 = new Match();
        Match m2 = new Match();
        matchList.add(m1);
        matchList.add(m2);
        ls.setGames(matchList);
        assertEquals(2,ls.getMatches().size());
        assertEquals(m1,ls.getMatches().get(0));
        assertEquals(m2,ls.getMatches().get(1));
    }

    /**
     * Tests addTeam function with null parameter
     */
    @Test
    public void addNullRefereeTest() {
        LeagueSeason ls = new LeagueSeason();
        assertFalse(ls.addReferee(null));
        assertEquals(0, ls.getReferees().size());
    }

    /**
     * Tests setScorePolicy after the league was started  - not possible should return false.
     */
    @Test
    public void setScorePolicyAfterLeagueStartedTest() {
        LeagueSeason ls = new LeagueSeason();
        Date startLeague = new Date(120, 1, 1, 10, 0);
        Match match = new Match(startLeague, null, null, ls, null);
        List<Match> matchesList = new ArrayList<>();
        matchesList.add(match);
        ScorePolicy sp = new ScorePolicy();
        ls.setGames(matchesList);
        assertFalse(ls.setScorePolicy(sp));
    }

    /**
     * Tests setScorePolicy before the league has matches  - should return true.
     */
    @Test
    public void setScorePolicyLeagueWithoutMatchesTest() {
        LeagueSeason ls = new LeagueSeason();
        ScorePolicy sp = new ScorePolicy();
        assertTrue(ls.setScorePolicy(sp));
    }

    /**
     * Tests setScorePolicy before the league startDate   - should return true.
     */
    @Test
    public void setScorePolicyBeforeLeagueStartedTest() {
        LeagueSeason ls = new LeagueSeason();
        Date startLeague = new Date(120, 5, 1, 10, 0);
        Match match = new Match(startLeague, null, null, ls, null);
        List<Match> matchesList = new ArrayList<>();
        matchesList.add(match);
        ScorePolicy sp = new ScorePolicy();
        ls.setGames(matchesList);
        assertTrue(ls.setScorePolicy(sp));
    }

    /**
     * Tests scheduleLeagueMatches with game policy equals to null
     */
    @Test
    public void scheduleLeagueMatchesNullGamePolicyTest(){
        LeagueSeason ls = new LeagueSeason();
        assertFalse(ls.scheduleLeagueMatches());
    }

    /**
     * Tests scheduleLeagueMatches without Teams
     */
    @Test
    public void scheduleLeagueMatchesNoTeamsTest(){
        LeagueSeason ls = new LeagueSeason(null,null,new GamePolicy(),null,null);
        assertFalse(ls.scheduleLeagueMatches());
    }

    /**
     * Tests scheduleLeagueMatches - should be okay
     */
    @Test
    public void scheduleLeagueMatchesTest(){
        LeagueSeason ls = new LeagueSeason(null,null,new GamePolicy(),null,null);
        Team t1 = new Team("Test1",false,false);
        Team t2 = new Team("Test2",false,false);
        Team t3 = new Team("Test3",false,false);
        ls.addTeam(t1);
        ls.addTeam(t2);
        ls.addTeam(t3);
        assertTrue(ls.scheduleLeagueMatches());
    }

    /**
     * Tests setRefereeInMatches while there is no matches - should return false
     */
    @Test
    public void setRefereeInMatchesWithoutMatchesTest(){
        LeagueSeason ls = new LeagueSeason(null,null,new GamePolicy(),null,null);
        assertFalse(ls.setRefereesInMatches());
    }

    /**
     * Tests setRefereeInMatches while there is only 2 referees - should return false (minimum 3 referees)
     */
    @Test
    public void setRefereeInMatchesWithTwoRefereesTest(){
        LeagueSeason ls = new LeagueSeason(null,null,new GamePolicy(),null,null);
        Team t1 = new Team("Test1",false,false);
        Team t2 = new Team("Test2",false,false);
        Referee r1 = new Referee(null,"Test1",null,false);
        Referee r2 = new Referee(null,"Test2",null,false);
        ls.addTeam(t1);
        ls.addTeam(t2);
        ls.addReferee(r1);
        ls.addReferee(r2);
        ls.scheduleLeagueMatches();
        assertFalse(ls.setRefereesInMatches());
    }

    /**
     * Tests setRefereeInMatches while there is 3 referees  2 games - should return true
     */
    @Test
    public void setRefereeInMatchesTest(){
        LeagueSeason ls = new LeagueSeason(null,null,new GamePolicy(),null,null);
        Team t1 = new Team("Test1",false,false);
        Team t2 = new Team("Test2",false,false);
        Team t3 = new Team("Test3",false,false);
        Referee r1 = new Referee(null,"Test1",null,false);
        Referee r2 = new Referee(null,"Test2",null,false);
        Referee r3 = new Referee(null,"Test3",null,false);
        ls.addTeam(t1);
        ls.addTeam(t2);
        ls.addTeam(t3);
        ls.addReferee(r1);
        ls.addReferee(r2);
        ls.addReferee(r3);
        ls.scheduleLeagueMatches();
        assertTrue(ls.setRefereesInMatches());
        assertEquals(ls.getMatches().get(0),r1.getMainMatches().get(0));
        assertEquals(ls.getMatches().get(0),r2.getLinesManMatches().get(0));
        assertEquals(ls.getMatches().get(0),r3.getLinesManMatches().get(0));
        assertEquals(ls.getMatches().get(1),r1.getMainMatches().get(1));
        assertEquals(ls.getMatches().get(1),r2.getLinesManMatches().get(1));
        assertEquals(ls.getMatches().get(1),r3.getLinesManMatches().get(1));
        assertEquals(ls.getMatches().get(2),r1.getMainMatches().get(2));
        assertEquals(ls.getMatches().get(2),r2.getLinesManMatches().get(2));
        assertEquals(ls.getMatches().get(2),r3.getLinesManMatches().get(2));
    }


}