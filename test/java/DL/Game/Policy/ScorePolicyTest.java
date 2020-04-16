package DL.Game.Policy;

import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.Match;
import DL.Team.Team;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Description:     This testClass tests the scorePolicy class.
 **/
public class ScorePolicyTest {
    /**
     * Tests scorePolicy constructor with right param
     */
    @Test
    public void scorePolicyCtorTest() {
        ScorePolicy scorePolicy = new ScorePolicy(2, 1, 0);
        Assert.assertEquals(2, scorePolicy.getWinPoints());
        Assert.assertEquals(1, scorePolicy.getDrawPoints());
        Assert.assertEquals(0, scorePolicy.getLosePoints());
    }

    /**
     * Tests default constructor of scorePolicy
     */
    @Test
    public void scorePolicyDefaultCtorTest() {
        ScorePolicy scorePolicy = new ScorePolicy();
        Assert.assertEquals(3, scorePolicy.getWinPoints());
        Assert.assertEquals(1, scorePolicy.getDrawPoints());
        Assert.assertEquals(0, scorePolicy.getLosePoints());
    }

    /**
     * Test equals function - should be the same
     */
    @Test
    public void scorePolicyEqualsTest() {
        ScorePolicy firstScorePolicy = new ScorePolicy(3, 1, 0);
        ScorePolicy secondScorePolicy = new ScorePolicy(3, 1, 0);
        assertEquals(true, firstScorePolicy.equals(secondScorePolicy));
    }

    /**
     * Test equals function - should not be the same because of rounds
     */
    @Test
    public void scorePolicyNotEqualsDrawTest() {
        ScorePolicy firstScorePolicy = new ScorePolicy(3, 1, 0);
        ScorePolicy secondScorePolicy = new ScorePolicy(3, 2, 0);
        assertEquals(false, firstScorePolicy.equals(secondScorePolicy));
    }


    /**
     * Test Hash code of equals scorePolicies
     */
    @Test
    public void scorePolicyHashCodeEqualsTest() {
        ScorePolicy firstScorePolicy = new ScorePolicy(3, 1, 0);
        ScorePolicy secondScorePolicy = new ScorePolicy(3, 1, 0);
        assertEquals(true, firstScorePolicy.hashCode() == secondScorePolicy.hashCode());
    }

    /**
     * Test Hash code of different scorePolicies
     */
    @Test
    public void scorePolicyHashCodeDifferentTest() {
        ScorePolicy firstScorePolicy = new ScorePolicy(3, 1, 0);
        ScorePolicy secondScorePolicy = new ScorePolicy(3, 2, 0);
        assertEquals(false, firstScorePolicy.hashCode() == secondScorePolicy.hashCode());
    }

    /**
     * Tests the calculation of the league table
     */
    @Test
    public void calculateLeagueTableTest() {
        ScorePolicy sp = new ScorePolicy(3, 1, 0);
        LeagueSeason leagueSeason = new LeagueSeason(null, null, null, sp, null);
        List<Match> matches = new ArrayList<>();
        Team t = new Team("t", true, true, null);
        Team t1 = new Team("t1", true, true, null);
        Team t2 = new Team("t2", true, true, null);
        Team t3 = new Team("t3", true, true, null);
        Match a = new Match(null, t, t1, null, null);
        a.setScore(2, 0);
        Match a1 = new Match(null, t2, t3, null, null);
        a1.setScore(0, 3);
        Match a2 = new Match(null, t, t2, null, null);
        a2.setScore(1, 1);
        Match a3 = new Match(null, t1, t3, null, null);
        a3.setScore(2, 6);
        Match a4 = new Match(null, t, t3, null, null);
        a4.setScore(1, 0);
        Match a5 = new Match(null, t1, t2, null, null);
        a5.setScore(0, 3);
        Match a6 = new Match(null, t1, t3, null, null);
        a6.setScore(2, 2);
        matches.add(a);
        matches.add(a1);
        matches.add(a2);
        matches.add(a3);
        matches.add(a4);
        matches.add(a5);
        matches.add(a6);
        leagueSeason.setGames(matches);
        List<Map.Entry<Team, Integer[]>> leagueTable = sp.calculateLeagueTable(leagueSeason);
        for (Map.Entry<Team, Integer[]> e : leagueTable) {
            System.out.println("Team: " + e.getKey().getName() + ", Points: " + e.getValue()[0] + ", Goals: " + e.getValue()[1]);
        }
        assertEquals(t3, leagueTable.get(0).getKey());
        assertEquals(t, leagueTable.get(1).getKey());
        assertEquals(t2, leagueTable.get(2).getKey());
        assertEquals(t1, leagueTable.get(3).getKey());
        assertEquals(7, (int) leagueTable.get(0).getValue()[0]);
        assertEquals(6, (int) leagueTable.get(0).getValue()[1]);
        assertEquals(7, (int) leagueTable.get(1).getValue()[0]);
        assertEquals(3, (int) leagueTable.get(1).getValue()[1]);
        assertEquals(4, (int) leagueTable.get(2).getValue()[0]);
        assertEquals(0, (int) leagueTable.get(2).getValue()[1]);
        assertEquals(1, (int) leagueTable.get(3).getValue()[0]);
        assertEquals(-9, (int) leagueTable.get(3).getValue()[1]);


    }

    @Test
    public void calculateLeagueTableNullParamTest() {
        ScorePolicy sp = new ScorePolicy(3, 1, 0);
        assertNull(sp.calculateLeagueTable(null));
    }

    @Test
    public void calculateLeagueTableWithoutTeamsTest() {
        ScorePolicy sp = new ScorePolicy(3, 1, 0);
        LeagueSeason leagueSeason = new LeagueSeason();
        assertNull(sp.calculateLeagueTable(leagueSeason));
    }

}