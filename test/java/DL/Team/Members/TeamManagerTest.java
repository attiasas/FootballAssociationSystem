package DL.Team.Members;

import DL.Team.Team;
import DL.Users.Fan;
import org.junit.Assert;
import org.junit.Test;

/**
 * Description:  Test suite for TeamManager class   X
 * ID:              X
 **/
public class TeamManagerTest {

    @Test
    public void testValidConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        Team team = new Team("Real Madrid", true, false);
        TeamOwner teamOwner = new TeamOwner(team);
        TeamManager teamManager = new TeamManager("Folrentinio Perez", true, fan, team, teamOwner);
        Assert.assertNotNull(teamManager);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFanConstructor() {

        Team team = new Team("Real Madrid", true, false);
        TeamOwner teamOwner = new TeamOwner(team);
        new TeamManager("Folrentinio Perez", true, null, team, teamOwner);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTeamOwnerConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        Team team = new Team("Real Madrid", true, false);
        new TeamManager("Folrentinio Perez", true, fan, team, null);

    }

}