package DL.Game;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Team.Assets.Stadium;
import DL.Team.Team;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Description:     X
 * ID:              X
 **/
public class MatchTest {

    /**
     * Tests ctor with parameters - should be okay
     */
    @Test
    public void ctorWithParamTest() {
        Date d1 = new Date(120);
        Team t1 = new Team();
        Team t2 = new Team();
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

    @Test
    public void setMainRefereeTest() {
        Match match = new Match();
        Referee r = new Referee();
        match.setMainReferee(r);
        assertEquals(r, match.getMainReferee());
    }

    @Test
    public void setMainRefereeNullParamTest() {
        Match match = new Match();
        match.setMainReferee(null);
        assertNull(match.getMainReferee());
    }

    @Test
    public void setLinesManRefereesNullTest() {
        Match match = new Match();
        match.setLinesManReferees(null, null);
        assertNull(match.getFirstLineManReferee());
        assertNull(match.getSecondLineManReferee());
    }

//    @Test
//    public void setLinesManRefereesTest() {
//        Match match = new Match();
//        Referee r = new Referee(null, "Test1", null, null);
//        Referee r2 = new Referee(null, "Test2", null, null);
//        match.setLinesManReferees(r, r2);
//        assertEquals(r, match.getFirstLineManReferee());
//        assertEquals(r2, match.getSecondLineManReferee());
//    }

//    @Test
//    public void setSameLinesManRefereesTest() {
//        Match match = new Match();
//        Referee r = new Referee(null, "Test1", null, null);
//        match.setLinesManReferees(r, r);
//        assertNull(match.getFirstLineManReferee());
//        assertNull(match.getSecondLineManReferee());
//    }

    @Test
    public void setMainRefereeEqualsToLinesManTest() {
        Match match = new Match();
        Referee r = new Referee();
        Referee r1 = new Referee();
        match.setLinesManReferees(r, r1);
        match.setMainReferee(r);
        assertNull(match.getMainReferee());
    }

    @Test
    public void setToLinesManEqualsMainRefereeTest() {
        Match match = new Match();
        Referee r = new Referee();
        Referee r1 = new Referee();
        match.setMainReferee(r);
        match.setLinesManReferees(r, r1);
        assertNull(match.getFirstLineManReferee());
        assertNull(match.getSecondLineManReferee());
    }

    @Test
    public void setScoreTest() {
        Match match = new Match();
        match.setScore(2, 0);
        assertEquals(2, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    public void setScoreNegativeValuesTest() {
        Match match = new Match();
        match.setScore(-1, 2);
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    public void setStartTimeTest() {
        Match match = new Match();
        Date d = new Date(120);
        match.setStartTime(d);
        d.setYear(d.getYear() - 1900);
        assertEquals(d.getYear(), match.getStartTime().getYear());
    }

    @Test
    public void setStartTimeNullTest() {
        Match match = new Match();
        match.setStartTime(null);
        assertNull(match.getStartTime());
    }

    @Test
    public void setEndTimeNullTest() {
        Match match = new Match();
        match.setEndTime(null);
        assertNull(match.getEndTime());
    }

    @Test
    public void setEndTimeTest() {
        Match match = new Match();
        Date d = new Date(120);
        match.setEndTime(d);
        d.setYear(d.getYear() - 1900);
        assertEquals(d.getYear(), match.getEndTime().getYear());
    }

    @Test
    public void setStadiumNullTest() {
        Match match = new Match();
        match.setStadium(null);
        assertNull(match.getStadium());
    }

    @Test
    public void setStadiumTest() {
        Match match = new Match();
        Stadium st = new Stadium();
        match.setStadium(st);
        assertEquals(st, match.getStadium());
    }

    @Test
    public void equalsMatchesTest() {
        Team t1 = new Team();
        Team t2 = new Team();
        Date d = new Date(120);
        Match m1 = new Match(d, t1, t2, null, null);
        Match m2 = new Match(d, t2, t1, null, null);
        assertEquals(true,m1.equals(m2));
    }

    @Test
    public void notEqualsMatchesTest() {
        Team t1 = new Team();
        Team t2 = new Team();
        Date d = new Date(120);
        Date d1 = new Date(121);
        Match m1 = new Match(d, t1, t2, null, null);
        Match m2 = new Match(d1, t1, t2, null, null);
        assertEquals(false,m1.equals(m2));
    }

    @Test
    public void hashCodeEqualsTest(){
        Team t1 = new Team();
        Team t2 = new Team();
        Date d = new Date(120);
        Match m1 = new Match(d, t1, t2, null, null);
        Match m2 = new Match(d, t2, t1, null, null);
        assertEquals(true,m1.hashCode() == m2.hashCode());
    }

    @Test
    public void hashCodeNotEqualsTest(){
        Team t1 = new Team();
        Team t2 = new Team();
        Date d = new Date(120);
        Date d1 = new Date(121);
        Match m1 = new Match(d, t1, t2, null, null);
        Match m2 = new Match(d1, t1, t2, null, null);
        assertEquals(false,m1.hashCode() == m2.hashCode());
    }
}