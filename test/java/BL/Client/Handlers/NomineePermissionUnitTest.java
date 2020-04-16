package BL.Client.Handlers;

import BL.Communication.CommunicationNomineePermissionStub;
import DL.Team.Members.TeamManager;
import DL.Team.Members.TeamOwner;
import DL.Team.Members.TeamUser;
import DL.Team.Page.TeamPage;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;
import DL.Users.UserPermission;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Description:     Unit Test for Class NomineePermissionUnit
 * ID:              2
 **/
public class NomineePermissionUnitTest
{
    private List<User> users;
    private List<TeamUser> teamUsers;
    private List<TeamOwner> owners;
    private List<Team> teams;
    private NomineePermissionUnit unit;

    @Before
    public void setUp()
    {
        teams = new ArrayList<>();
        users = new ArrayList<>();
        owners = new ArrayList<>();
        teamUsers = new ArrayList<>();
        unit = new NomineePermissionUnit(new CommunicationNomineePermissionStub(users,owners,teamUsers));

        // Generate Default instances for tests
        Fan u1 = new Fan("Assaf","test@mail.com","abcde");
        Fan u2 = new Fan("Amir","test@mail.com","abcde");
        Fan u3 = new Fan("Amit","test@mail.com","abcde");
        Fan u4 = new Fan("Dvir","test@mail.com","abcde");
        Fan u5 = new Fan("Avihai","test@mail.com","abcde");
        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.add(u4);
        users.add(u5);

        Team t1 = new Team("BestTeam",true,false,new TeamPage());
        teams.add(t1);

        TeamUser tu1 = new TeamUser("Assaf",true,u1);
        TeamUser tu2 = new TeamUser("Amit",true,u3);
        teamUsers.add(tu1);
        teamUsers.add(tu2);

        TeamOwner o1 = new TeamOwner(t1,tu1,true);
        t1.teamOwners.add(o1); // initial Owner, not in unit responsibility
        TeamOwner o2 = o1.addTeamOwnerNominee(tu2);
        owners.add(o1);
        owners.add(o2);

        TeamManager tm1 = o1.addTeamManagerNominee(u2,"Amir");
        TeamManager tm2 = o2.addTeamManagerNominee(u4,"Dvir");
        teamUsers.add(tm1);
        teamUsers.add(tm2);
    }


    //TODO: Test insert manager and then delete and then insert again the same

    @Test
    public void testGetOwnerNominees()
    {
        List<TeamUser> nominees = unit.getOwnerNominees(users.get(0));
        assertNotNull(nominees);
        assertEquals(1,nominees.size());
    }

    @Test
    public void testGetOwnerNomineesBadArguments()
    {
        // user is not an owner
        List<TeamUser> nominees = unit.getOwnerNominees(users.get(1));
        assertNull(nominees);
        // null arguments
        nominees = unit.getOwnerNominees(null);
        assertNull(nominees);
    }

    @Test
    public void testGetManagerNominees()
    {
        List<TeamManager> nominees = unit.getManageNominees(users.get(0));
        assertNotNull(nominees);
        assertEquals(1,nominees.size());
    }

    @Test
    public void testGetManagerNomineesBadArguments()
    {
        // user is not an owner
        List<TeamManager> nominees = unit.getManageNominees(users.get(1));
        assertNull(nominees);
        // null arguments
        nominees = unit.getManageNominees(null);
        assertNull(nominees);
    }

    @Test
    public void testAddOwnerNominee()
    {
        assertTrue(unit.addOwnerNominee(users.get(0),"Avihai","Avihai"));
        assertEquals(2,unit.getOwnerNominees(users.get(0)).size());
        assertEquals(3,teams.get(0).teamOwners.size());
    }

    @Test
    public void testAddOwnerNomineeBadArguments()
    {
        // user null
        assertFalse(unit.addOwnerNominee(null,"Avihai","bla"));
        // user not an owner
        assertFalse(unit.addOwnerNominee(users.get(1),"Avihai","bla"));
        // username null
        assertFalse(unit.addOwnerNominee(users.get(0),null,"bla"));
        // username (user of the nominee) not exists
        assertFalse(unit.addOwnerNominee(users.get(0),"notExists","bla"));
        // name is null
        assertFalse(unit.addOwnerNominee(users.get(0),"Avihai",null));
        // name is empty
        assertFalse(unit.addOwnerNominee(users.get(0),"Avihai",""));

        // check no change
        assertEquals(1,unit.getOwnerNominees(users.get(0)).size());
        assertEquals(2,teams.get(0).teamOwners.size());
    }

    @Test
    public void testAddManagerNominee()
    {
        assertTrue(unit.addManagerNominee(users.get(0),"Avihai","Avihai"));
        assertEquals(1,unit.getOwnerNominees(users.get(0)).size());
        assertEquals(2,unit.getManageNominees(users.get(0)).size());
        assertEquals(3,teams.get(0).teamManagers.size());
    }

    @Test
    public void testAddManagerNomineeBadArguments()
    {
        // user null
        assertFalse(unit.addManagerNominee(null,"Avihai","bla"));
        // user not an owner
        assertFalse(unit.addManagerNominee(users.get(1),"Avihai","bla"));
        // username null
        assertFalse(unit.addManagerNominee(users.get(0),null,"bla"));
        // username (user of the nominee) not exists
        assertFalse(unit.addManagerNominee(users.get(0),"notExists","bla"));
        // name is null
        assertFalse(unit.addManagerNominee(users.get(0),"Avihai",null));
        // name is empty
        assertFalse(unit.addManagerNominee(users.get(0),"Avihai",""));
        // check no change
        assertEquals(1,unit.getManageNominees(users.get(0)).size());
        assertEquals(2,teams.get(0).teamManagers.size());
    }

    @Test
    public void testRemoveOwnerNominee()
    {
        assertTrue(unit.removeOwnerNominee(users.get(0),teamUsers.get(1)));
        // check nominee removed
        assertEquals(0,unit.getOwnerNominees(users.get(0)).size());
        // check nominee cannot access unit and his nominees removed to
        assertNull(unit.getManageNominees(users.get(2)));
        assertEquals(1,getNumActiveOwnersOfTeam());
        assertEquals(1,getNumActiveManagersOfTeam());
    }

    private int getNumActiveOwnersOfTeam()
    {
        int res = 0;
        for(TeamOwner owner : teams.get(0).teamOwners)
        {
            if(owner.isActive()) res++;
        }
        return res;
    }

    private int getNumActiveManagersOfTeam()
    {
        int res = 0;
        for(TeamManager manager : teams.get(0).teamManagers)
        {
            if(manager.isActive()) res++;
        }
        return res;
    }

    @Test
    public void testRemoveOwnerNomineeBadArguments()
    {
        // null
        assertFalse(unit.removeOwnerNominee(null,teamUsers.get(1)));
        assertFalse(unit.removeOwnerNominee(users.get(0),null));
        // not a nominee
        assertFalse(unit.removeOwnerNominee(users.get(0),teamUsers.get(3)));
        // check no change
        assertEquals(1,unit.getOwnerNominees(users.get(0)).size());
        assertEquals(2,teams.get(0).teamOwners.size());
    }

    @Test
    public void testRemoveManagerNominee()
    {
        assertTrue(unit.removeManagerNominee(users.get(0),(TeamManager)teamUsers.get(2)));
        assertTrue(unit.removeManagerNominee(users.get(2),(TeamManager)teamUsers.get(3)));
        // check changes
        assertEquals(0,unit.getManageNominees(users.get(0)).size());
        assertEquals(0,unit.getManageNominees(users.get(2)).size());
        assertEquals(2,getNumActiveOwnersOfTeam());
        assertEquals(0,getNumActiveManagersOfTeam());
    }

    @Test
    public void testRemoveManagerNomineeBadArguments()
    {
        // null
        assertFalse(unit.removeManagerNominee(null,(TeamManager)teamUsers.get(2)));
        assertFalse(unit.removeManagerNominee(users.get(0),null));
        // not a nominee of the user
        assertFalse(unit.removeManagerNominee(users.get(0),(TeamManager)teamUsers.get(3)));
        // check no change
        assertEquals(1,unit.getManageNominees(users.get(0)).size());
        assertEquals(2,teams.get(0).teamManagers.size());
    }

    @Test
    public void testChangeNomineePermission()
    {
        assertTrue(unit.changeNomineePermission(users.get(0),(TeamManager)teamUsers.get(2),true,false,true));
        assertTrue(users.get(1).hasPermission(UserPermission.Permission.ADD));
        assertFalse(users.get(1).hasPermission(UserPermission.Permission.REMOVE));
        assertTrue(users.get(1).hasPermission(UserPermission.Permission.EDIT));
    }

    @Test
    public void testChangeNomineePermissionBadArguments()
    {
        // null
        assertFalse(unit.changeNomineePermission(null,(TeamManager)teamUsers.get(2),true,false,true));
        assertFalse(unit.changeNomineePermission(users.get(0),null,true,false,true));
        // not his manager
        assertFalse(unit.changeNomineePermission(users.get(2),(TeamManager)teamUsers.get(2),true,false,true));

        // check no changes
        assertFalse(users.get(1).hasPermission(UserPermission.Permission.ADD));
        assertFalse(users.get(1).hasPermission(UserPermission.Permission.REMOVE));
        assertFalse(users.get(1).hasPermission(UserPermission.Permission.EDIT));
    }
}
