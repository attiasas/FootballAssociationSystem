package DL.Team.Members;

import DL.Team.Team;
import DL.Users.Fan;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Description:  Test suite for Player class   X
 * ID:    10          X
 **/
public class PlayerTest {

    // ID: 10.1
    @Test
    public void testValidConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        Team team = new Team("Real Madrid", true, false);
        Date date = new Date(2020, 04, 14);
        Player player = new Player("Cristiano Ronaldo", true, fan, date, "Left Winger", team);
        Assert.assertNotNull(player);
    }

    // ID: 10.2
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRoleConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        Team team = new Team("Real Madrid", true, false);
        Date date = new Date(2020, 04, 14);
        new Player("Cristiano Ronaldo", true, fan, date, "ab 5", team);

    }

    // ID: 10.3
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDateConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        Date date = new Date(2020, 04, 14);
        new Player("Cristiano Ronaldo", true, fan, date, "ab 5", null);

    }

}