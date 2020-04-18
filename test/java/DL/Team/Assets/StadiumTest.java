package DL.Team.Assets;

import DL.Team.Team;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:   Test suite for Stadium class X
 * ID:   8           X
 **/
public class StadiumTest {

    List<Team> teamList = new ArrayList<>();

    @Before
    public void initTeamList() {
        Team team = new Team("Real Madrid", true, false);
        teamList.add(team);
    }

    // ID: 8.1
    @Test
    public void testValidConstructor() {

        Stadium stadium = new Stadium("Santiago Bernabeu", 81000, teamList.get(0));
        Assert.assertNotNull(stadium);
    }

    // ID: 8.2
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCapacityConstructor() {

        new Stadium("Santiago Bernabeu", -10, teamList.get(0));

    }

    // ID: 8.3
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTeamConstructor() {

        new Stadium("Santiago Bernabeu", -10, null);

    }
}