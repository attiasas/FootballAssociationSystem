package DL.Game;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Team.Assets.Stadium;
import DL.Team.Team;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Description:     This testClass tests the Match class.
 * ID:              18
 **/
public class MatchTest {

    /**
     * Tests the default ctor.
     */
    @Test
    public void defaultCtorTest() {
        Match match = new Match();
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
        assertNull(match.getHomeTeam());
        assertNull(match.getAwayTeam());
        assertNull(match.getStadium());
        assertNull(match.getStartTime());
        assertNull(match.getEndTime());
    }

    /**
     * Tests ctor with parameters - should be okay
     */
    @Test
    public void ctorWithParamTest() {
        Date d1 = new Date(120);
        Team t1 = new Team("Test1", false, false);
        Team t2 = new Team("Test2", false, false);
        LeagueSeason ls = new LeagueSeason();
        Stadium st = new Stadium();
        Match match = new Match(d1, t1, t2, ls, st);
        assertEquals(d1, match.getStartTime());
        assertEquals(t1, match.getHomeTeam());
        assertEquals(t2, match.getAwayTeam());
        assertEquals(st, match.getStadium());
    }

    /**
     * Tests ctor with parameters - should be Null
     */
    @Test
    public void ctorWithSameTeamTest() {
        Team t1 = new Team();
        Match match = new Match(null, t1, t1, null, null);
        assertNull(match.getHomeTeam());
        assertNull(match.getAwayTeam());
    }

    /**
     * Tests the setter of main referee in a match
     */
    @Test
    public void setMainRefereeTest() {
        Match match = new Match();
        Referee r = new Referee();
        match.setReferee(r);
        assertEquals(r, match.getReferees().get(0));
    }

    /**
     * Tests the setter of main referee in a match while the referee is null
     */
    @Test
    public void setMainRefereeNullParamTest() {
        Match match = new Match();
        match.setReferee(null);
        assertEquals(0, match.getReferees().size());
    }

    /**
     * Tests the setter of LinesMan referee in a match while the referee is null
     */
    @Test
    public void setSameRefereesTest() {
        Match match = new Match();
        Referee r = new Referee();
        r.setId(1);
        Referee r1 = new Referee();
        r1.setId(1);
        match.setReferee(r);
        assertEquals(1, match.getReferees().size());
        match.setReferee(r1);
        assertEquals(1, match.getReferees().size());
    }

    /**
     * Tests the setter of LinesMan referee in a match
     */
    @Test
    public void setTwoRefereesTest() {
        Match match = new Match();
        Referee r = new Referee(null, "Test1", null, false);
        Referee r2 = new Referee(null, "Test2", null, false);
        r.setId(1);
        r2.setId(2);
        match.setReferee(r);
        match.setReferee(r2);
        assertEquals(2, match.getReferees().size());
    }

    /**
     * Tests the setter of LinesMan referee in a match with same referees - should be false
     */
    @Test
    public void setMoreThanThreeRefereesTest() {
        Match match = new Match();
        Referee r = new Referee(null, "Test1", null, false);
        Referee r1 = new Referee(null, "Test2", null, false);
        Referee r2 = new Referee(null, "Test3", null, false);
        Referee r3 = new Referee(null, "Test4", null, false);
        r.setId(1);
        r1.setId(2);
        r2.setId(3);
        r3.setId(4);
        match.setReferee(r);
        match.setReferee(r1);
        match.setReferee(r2);
        match.setReferee(r3);
        assertEquals(3, match.getReferees().size());
    }

//    /**
//     * Tests the setters of linesMan referee and mainReferee while the main referee equals to the linesMan
//     */
//    @Test
//    public void setMainRefereeEqualsToLinesManTest() {
//        Match match = new Match();
//        Referee r = new Referee(null, "Test1", null, false);
//        Referee r1 = new Referee(null, "Test2", null, false);
//        match.setLinesManReferees(r, r1);
//        match.setMainReferee(r);
//        assertNull(match.getMainReferee());
//    }
//
//    /**
//     * Tests the setters of linesMan referee and mainReferee while the linesMan equals to the main referee
//     */
//    @Test
//    public void setToLinesManEqualsMainRefereeTest() {
//        Match match = new Match();
//        Referee r = new Referee();
//        Referee r1 = new Referee();
//        match.setMainReferee(r);
//        match.setLinesManReferees(r, r1);
//        assertNull(match.getFirstLineManReferee());
//        assertNull(match.getSecondLineManReferee());
//    }

    /**
     * Test the setter of the score
     */
    @Test
    public void setScoreTest() {
        Match match = new Match();
        match.setScore(2, 0);
        assertEquals(2, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    /**
     * Test the setter of the score with negative values
     */
    @Test
    public void setScoreNegativeValuesTest() {
        Match match = new Match();
        match.setScore(-1, 2);
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    /**
     * Tests the setter ot the start time
     */
    @Test
    public void setStartTimeTest() {
        Match match = new Match();
        Date d = new Date(120);
        match.setStartTime(d);
        assertEquals(d, match.getStartTime());
    }

    /**
     * test the setter of the start time with null parameters - should be null
     */
    @Test
    public void setStartTimeNullTest() {
        Match match = new Match();
        match.setStartTime(null);
        assertNull(match.getStartTime());
    }

    /**
     * test the setter of the end time with null parameters - should be null
     */
    @Test
    public void setEndTimeNullTest() {
        Match match = new Match();
        match.setEndTime(null);
        assertNull(match.getEndTime());
    }

    /**
     * Tests the setter ot the end time
     */
    @Test
    public void setEndTimeTest() {
        Match match = new Match();
        Date d = new Date(120);
        match.setEndTime(d);
        assertEquals(d, match.getEndTime());
    }

    /**
     * test the setter of the stadium with null parameters - should be null
     */
    @Test
    public void setStadiumNullTest() {
        Match match = new Match();
        match.setStadium(null);
        assertNull(match.getStadium());
    }

    /**
     * test the setter of the stadium
     */
    @Test
    public void setStadiumTest() {
        Match match = new Match();
        Stadium st = new Stadium();
        match.setStadium(st);
        assertEquals(st, match.getStadium());
    }

    /**
     * Tests the equals function - two equals matches
     */
    @Test
    public void equalsMatchesTest() {
        Team t1 = new Team();
        Team t2 = new Team();
        Date d = new Date(120);
        Match m1 = new Match(d, t1, t2, null, null);
        Match m2 = new Match(d, t2, t1, null, null);
        assertEquals(true, m1.equals(m2));
    }

    /**
     * Tests the equals function - not equals matches
     */
    @Test
    public void notEqualsMatchesTest() {
        Team t1 = new Team();
        Team t2 = new Team();
        Date d = new Date(120);
        Date d1 = new Date(121);
        Match m1 = new Match(d, t1, t2, null, null);
        Match m2 = new Match(d1, t1, t2, null, null);
        assertEquals(false, m1.equals(m2));
    }

    /**
     * Tests the hashCode function - two equals matches
     */
    @Test
    public void hashCodeEqualsTest() {
        Team t1 = new Team();
        Team t2 = new Team();
        Date d = new Date(120);
        Match m1 = new Match(d, t1, t2, null, null);
        Match m2 = new Match(d, t2, t1, null, null);
        assertEquals(true, m1.hashCode() == m2.hashCode());
    }

    /**
     * Tests the hashCode function - not equals matches
     */
    @Test
    public void hashCodeNotEqualsTest() {
        Team t1 = new Team();
        Team t2 = new Team();
        Date d = new Date(120);
        Date d1 = new Date(121);
        Match m1 = new Match(d, t1, t2, null, null);
        Match m2 = new Match(d1, t1, t2, null, null);
        assertEquals(false, m1.hashCode() == m2.hashCode());
    }
}