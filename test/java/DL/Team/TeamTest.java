package DL.Team;

import org.junit.Assert;
import org.junit.Test;

/**
 * Description:  Test suite for Team Class   X
 * ID:              X
 **/
public class TeamTest {

    @Test
    public void testValid1Constructor() {

        Team team = new Team("Real Madrid", true, false);
        Assert.assertNotNull(team);
    }

    @Test
    public void testValid2Constructor() {

        Team team = new Team("Hanover 09", true, false);
        Assert.assertNotNull(team);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNameConstructor() {

        new Team("Hanov!!er 09", true, false);

    }

}