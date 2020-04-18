package DL.Team.Members;

import DL.Team.Team;
import DL.Users.Fan;
import org.junit.Assert;
import org.junit.Test;

/**
 * Description:  Test suite for TeamManager class   X
 * ID:    11          X
 **/
public class TeamManagerTest {

    // ID: 11.1
    @Test
    public void testValidConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        Team team = new Team("Real Madrid", true, false);
        TeamUser teamUser = new TeamUser("BB", true, fan, team);
        TeamOwner teamOwner = new TeamOwner(team, teamUser);
        TeamManager teamManager = new TeamManager("Folrentinio Perez", true, fan, team, teamOwner);
        Assert.assertNotNull(teamManager);
    }

    // ID: 11.2
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFanConstructor() {

        Team team = new Team("Real Madrid", true, false);
        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        TeamUser teamUser = new TeamUser("BB", true, fan, team);
        TeamOwner teamOwner = new TeamOwner(team, teamUser);
        new TeamManager("Folrentinio Perez", true, null, team, teamOwner);

    }

    // ID: 11.3
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTeamOwnerConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        Team team = new Team("Real Madrid", true, false);
        new TeamManager("Folrentinio Perez", true, fan, team, null);

    }

}