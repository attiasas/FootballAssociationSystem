package BL.Client.Handlers;

import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.Season;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.BL.Communication.ClientServerCommunicationStub;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:     This class Tests the leagueSeasonUnit
 **/
public class LeagueSeasonUnitTest {

    LeagueSeasonUnit leagueSeasonUnit;

    /**
     * inits the communication of leagueSeasonUnit
     */
    @Before
    public void init(){
        this.leagueSeasonUnit = new LeagueSeasonUnit(new ClientServerCommunicationStub());
    }

    /**
     * Test the creation of a new league
     */
    @Test
    public void createLeagueTest(){
        List<League> leagueListExpected = new ArrayList<>();
        Assert.assertEquals(true,leagueSeasonUnit.addNewLeague("CheckLeague"));
        leagueListExpected.add(new League("CheckLeague"));
        System.out.println(leagueListExpected.get(0));
        System.out.println(leagueSeasonUnit.getLeagues().get(0));
        Assert.assertEquals(leagueListExpected,leagueSeasonUnit.getLeagues());
    }

    /**
     * Test the creation of a new league with bad arguments - should return false
     */
    @Test
    public void createLeagueNullTest(){
        List<League> leagueListExpected = new ArrayList<>();
        Assert.assertEquals(false,leagueSeasonUnit.addNewLeague(null));
        Assert.assertEquals(leagueListExpected,leagueSeasonUnit.getLeagues());
    }

    /**
     * Test the creation of a new season
     */
    @Test
    public void createNewSeason(){
        List<Season> seasonListExpected = new ArrayList<>();
        Assert.assertEquals(true,leagueSeasonUnit.addNewSeason(2020));
        seasonListExpected.add(new Season(2020));
        System.out.println(seasonListExpected.get(0));
        System.out.println(leagueSeasonUnit.getSeasons().get(0));
        Assert.assertEquals(seasonListExpected,leagueSeasonUnit.getSeasons());
    }

    /**
     * Test the creation of a new Season with bad arguments - should return false
     */
    @Test
    public void createSeasonNegativeArgTest(){
        List<League> seasonListExpected = new ArrayList<>();
        Assert.assertEquals(false,leagueSeasonUnit.addNewSeason(-1));
        Assert.assertEquals(seasonListExpected,leagueSeasonUnit.getSeasons());
        Assert.assertEquals(0,leagueSeasonUnit.getSeasons().size());
    }







}

