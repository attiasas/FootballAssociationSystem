package DL.Team.Members;

import DL.Team.Team;
import org.junit.Assert;
import org.junit.Test;

/**
 * Description:  Test suite for TeamOwner class   X
 * ID:              X
 **/
public class TeamOwnerTest {

    @Test
    public void testValidConstructor() {

        Team team = new Team("Real Madrid", true, false);
        TeamOwner teamOwner = new TeamOwner(team);
        Assert.assertNotNull(teamOwner);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTeamConstructor() {

        new TeamOwner(null);
    }


}