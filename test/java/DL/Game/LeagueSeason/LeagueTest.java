package DL.Game.LeagueSeason;

import org.junit.Assert;
import org.junit.Test;


/**
 * Description:     This testClass tests the League class.
 * ID:              22
 **/
public class LeagueTest {

    /**
     * checks ctor with null arg
     */
    @Test
    public void parameterCtorNullValueTest(){
        League league = new League(null);
        Assert.assertEquals(league.getName(),"DefaultName");
    }

    /**
     * checks ctor with empty string
     */
    @Test
    public void parameterCtorEmptyStringTest(){
        League league = new League("");
        Assert.assertEquals(league.getName(),"DefaultName");
    }

    /**
     * Checks ctor with right name
     */
    @Test
    public void parameterCtorTest(){
        League league = new League("CheckLeagueName");
        Assert.assertEquals(league.getName(),"CheckLeagueName");
    }

    /**
     * Checks default constructor
     */
    @Test
    public void defaultConstructor(){
        League league = new League();
        Assert.assertEquals(league.getName(),"DefaultName");
    }



}