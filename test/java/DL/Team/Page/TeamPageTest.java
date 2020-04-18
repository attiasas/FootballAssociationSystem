package DL.Team.Page;

import DL.Team.Team;
import org.junit.Assert;
import org.junit.Test;

/**
 * Description:  Test suite for TeamPage class   X
 * ID:     14         X
 **/

public class TeamPageTest {

    // ID: 14.1
    @Test
    public void testValidConstructor() {

        Team team = new Team("Real Madrid", true, false);
        TeamPage teamPage = new TeamPage("content", team);
        Assert.assertNotNull(teamPage);
    }

    // ID: 14.2
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidContentConstructor() {

        Team team = new Team("Real Madrid", true, false);
        new TeamPage(null, team);
    }

    // ID: 14.3
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTeamUserConstructor() {

        new TeamPage("content", null);
    }

}
