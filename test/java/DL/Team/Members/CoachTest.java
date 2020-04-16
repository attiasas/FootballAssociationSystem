package DL.Team.Members;

import DL.Team.Team;
import DL.Users.Fan;
import org.junit.Assert;
import org.junit.Test;

/**
 * Description:  Test suite for Coach Class   X
 * ID:              X
 **/
public class CoachTest {

    @Test
    public void testValidConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        Team team = new Team("Real Madrid", true, false);
        Coach coach = new Coach("Barak Bachar", true, fan, "UEFA Coaches Academy", "Primary Coach", team);
        Assert.assertNotNull(coach);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRoleConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        Team team = new Team("Real Madrid", true, false);
        new Coach("Barak Bachar", true, fan, "UEFA Coaches Academy", "ab12", team);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTeamConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        new Coach("Barak Bachar", true, fan, "UEFA Coaches Academy", "ab12", null);


    }

}