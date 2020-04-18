package DL.Team;

import org.junit.Assert;
import org.junit.Test;

/**
 * Description:  Test suite for Team Class   X
 * ID:   13           X
 **/
public class TeamTest {

    // ID: 13.1
    @Test
    public void testValid1Constructor() {

        Team team = new Team("Real Madrid", true, false);
        Assert.assertNotNull(team);
    }

    // ID: 13.2
    @Test
    public void testValid2Constructor() {

        Team team = new Team("Hanover 09", true, false);
        Assert.assertNotNull(team);
    }

    // ID: 13.3
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNameConstructor() {

        new Team("Hanov!!er 09", true, false);

    }

}