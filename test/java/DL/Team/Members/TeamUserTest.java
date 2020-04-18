package DL.Team.Members;

import DL.Team.Team;
import DL.Users.Fan;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Description:  Test suite for TeamUser class   X
 * ID:   12           X
 **/
public class TeamUserTest {

    Team team;

    @Before
    public void setUp() {
        team = new Team("barcelona", true, false);
    }

    // ID: 12.1
    @Test
    public void testValidConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        TeamUser teamUser = new TeamUser("Leo Messi", true, fan, team);
        Assert.assertNotNull(teamUser);
    }

    // ID: 12.2
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNameConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        new TeamUser(" ", true, fan, team);
    }

    // ID: 12.3
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFanConstructor() {

        new TeamUser("Leo Messi", true, null, team);

    }
}