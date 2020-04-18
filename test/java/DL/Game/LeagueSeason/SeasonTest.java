package DL.Game.LeagueSeason;

import org.junit.Assert;
import org.junit.Test;


/**
 * Description:     This testClass tests the seasonTest class.
 * ID:              21
 **/
public class SeasonTest {

    /**
     * checks that the lower year that possible is 1950
     */
    @Test
    public void parameterCtorLowValueTest(){
        Season season = new Season(0);
        Assert.assertEquals(season.getYear(),1950);
    }

    /**
     * checks the ctor that it works as needed
     */
    @Test
    public void parameterCtorGoodValuesTest(){
        Season season = new Season(2011);
        Assert.assertEquals(season.getYear(),2011);
    }

    /**
     * Checks the default ctor the year needs to be 1950
     */
    @Test
    public void defaultCtor(){
        Season season = new Season();
        Assert.assertEquals(season.getYear(),1950);
    }

}