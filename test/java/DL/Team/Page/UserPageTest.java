package DL.Team.Page;

import DL.Team.Members.Coach;
import DL.Team.Members.PageUser;
import DL.Team.Page.UserPage;
import DL.Team.Team;
import DL.Users.Fan;
import org.junit.Assert;
import org.junit.Test;

/**
 * Description:  Test suite for UserPage class   X
 * ID:     15         X
 **/

public class UserPageTest {

    // ID: 15.1
    @Test
    public void testValidConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        Team team = new Team("Real Madrid", true, false);
        PageUser pageUser = new Coach("Barak Bachar", true, fan, "UEFA Coaches Academy", "Primary Coach", team);
        UserPage userPage = new UserPage("content", pageUser);
        Assert.assertNotNull(userPage);
    }

    // ID: 15.2
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidContentConstructor() {

        Fan fan = new Fan("BB", "BB@gmail.com", "123456");
        Team team = new Team("Real Madrid", true, false);
        PageUser pageUser = new Coach("Barak Bachar", true, fan, "UEFA Coaches Academy", "Primary Coach", team);
        UserPage userPage = new UserPage(null, pageUser);
        Assert.assertNotNull(userPage);
    }

    // ID: 15.3
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPageUserConstructor() {

        new UserPage("content", null);

    }

}
