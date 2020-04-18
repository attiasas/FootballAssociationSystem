package DL.Team.Members;

import DL.Team.Page.TeamPage;
import DL.Team.Team;
import DL.Users.Fan;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Description:     Unit Test for Class TeamOwner
 * ID:              3
 **/
public class TeamOwnerTest
{
    private TeamOwner owner;
    private Team team;

    @Before
    public void setUp()
    {
        team = new Team("BestTeam",true,false);
        TeamUser teamUser = new TeamUser("Assaf",true,new Fan("Assaf","test@mail.com","avscd"),team);
        owner = new TeamOwner(team,teamUser);
        team.getTeamOwners().add(owner);
    }

    @Test
    public void testAddTeamOwnerNominee()
    {
        TeamUser nominee = new TeamUser("Amir",true,new Fan("Amir","test@mail.com","avscd"),team);
        TeamOwner nomineeOwner = owner.addTeamOwnerNominee(nominee);
        assertNotNull(nomineeOwner);
        assertEquals(2,team.getTeamOwners().size());
        assertEquals(0,team.getTeamManagers().size());
        assertEquals(1,owner.getOwnerNominees().size());
        assertEquals(0,owner.getManageNominees().size());
    }

    @Test
    public void testAddTeamOwnerNomineeBadArguments()
    {
        TeamOwner nomineeOwner = owner.addTeamOwnerNominee(null);
        assertNull(nomineeOwner);
        // check if state is the same
        assertEquals(1,team.getTeamOwners().size());
        assertEquals(0,owner.getOwnerNominees().size());
    }

    @Test
    public void testAddTeamManagerNominee()
    {
        Fan nominee = new Fan("Amir","test@mail.com","avscd");
        TeamManager nomineeManager = owner.addTeamManagerNominee(nominee,"Amir");
        assertNotNull(nomineeManager);
        assertEquals(1,team.getTeamOwners().size());
        assertEquals(1,team.getTeamManagers().size());
        assertEquals(0,owner.getOwnerNominees().size());
        assertEquals(1,owner.getManageNominees().size());
    }

    @Test
    public void testAddTeamManagerNomineeBadArguments()
    {
        Fan nominee = new Fan("Amir","test@mail.com","avscd");
        // null
        TeamManager nomineeOwner = owner.addTeamManagerNominee(null,"Amir");
        assertNull(nomineeOwner);
        nomineeOwner = owner.addTeamManagerNominee(nominee,null);
        assertNull(nomineeOwner);
        // name empty
        nomineeOwner = owner.addTeamManagerNominee(nominee,"");
        assertNull(nomineeOwner);

        // check if state is the same
        assertEquals(0,team.getTeamManagers().size());
        assertEquals(0,owner.getManageNominees().size());
    }

//    @Test
//    public void testValidConstructor() {
//
//        Team team = new Team("Real Madrid", true, false);
//        TeamOwner teamOwner = new TeamOwner(team new TeamUser());
//        assertNotNull(teamOwner);
//    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTeamConstructor() {

        new TeamOwner(null,null);
    }


}