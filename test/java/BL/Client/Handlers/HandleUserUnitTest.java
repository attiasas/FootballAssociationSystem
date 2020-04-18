package BL.Client.Handlers;

import BL.Client.ClientSystem;
import BL.Communication.ClientServerCommunication;
import BL.Communication.CommunicationUserUnitStub;
import DL.Administration.AssociationMember;
import DL.Administration.Financial.FinancialUser;
import DL.Game.Referee;
import DL.Team.Members.*;
import DL.Team.Page.Page;
import DL.Team.Page.TeamPage;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;
import com.sun.security.ntlm.Client;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Description:     test class to the HandleUserUnit class
 * ID:              4
 **/
public class HandleUserUnitTest
{

    private HandleUserUnit userUnit;
    private ClientServerCommunication communication;

    @Before
    public void init()
    {
        communication = new CommunicationUserUnitStub();
        TeamAssetUnit teamAssetUnit = new TeamAssetUnit(communication);
        NomineePermissionUnit nomineePermissionUnit = new NomineePermissionUnit();
        AssociationManagementUnit associationManagementUnit = new AssociationManagementUnit(communication);
        userUnit = new HandleUserUnit(communication, nomineePermissionUnit, teamAssetUnit, associationManagementUnit);

        //init fans like pages
        Fan u1 = new Fan("Assaf","test@mail.com", DigestUtils.sha1Hex("abcde"));
        Fan u2 = new Fan("Amir","test@mail.com",DigestUtils.sha1Hex("abcde"));
        Fan u3 = new Fan("Amit","test@mail.com",DigestUtils.sha1Hex("abcde"));
        Fan u4 = new Fan("Dvir","test@mail.com",DigestUtils.sha1Hex("abcde"));
        Fan u5 = new Fan("Avihai","test@mail.com",DigestUtils.sha1Hex("abcde"));
        //Create team
        Team t1 = new Team("BestTeam",true,false);
        //Create Page
        TeamPage t1Page = new TeamPage("t1 Page!", t1);
        //connect team to page
        t1.setPage(t1Page);
        //make Avihai follow t1Page
        u5.followPage(t1Page);

        //insert users
        communication.insert(u1);
        communication.insert(u2);
        communication.insert(u3);
        communication.insert(u4);
        communication.insert(u5);
        //insert team
        communication.insert(t1);
        //insert page
        communication.insert(t1Page);

        Player player = new Player("AmirPlayer", true, u2, new Date(1994, 9, 30), "Attacker", t1);
        communication.insert(player);

        Referee referee = new Referee("linesMan", "AssafReferee", u1, true);
        communication.insert(referee);

        AssociationMember associationMember = new AssociationMember("Yoram", "yoram@mail.com", "abcde");
        communication.insert(associationMember);

        Coach amitCoach = new Coach("AmitCoach", true, u3, "High", "Main", t1);
        communication.insert(amitCoach);

        Fan teamManagerFan = new Fan("teamManager","test@mail.com",DigestUtils.sha1Hex("abcde"));
        communication.insert(teamManagerFan);

        TeamManager teamManager = new TeamManager("teamManager", true, teamManagerFan, t1, new TeamOwner());
        communication.insert(teamManager);

    }


    @Test
    public void testSignUpAUserAndLogHimIn()
    {
        User user = userUnit.signUp("a", "a@gmail.com", "abc");
        userUnit.logIn("a", "abc");
        assertEquals(user, ClientSystem.getLoggedUser());
    }


    @Test
    public void testSignUpBadParameters()
    {
        User user = userUnit.signUp("", "", "");
        assertNull(user);
    }

    @Test
    public void testLogIn()
    {
        boolean isLoggedIn = userUnit.logIn("Amir", "abcde");
        User loggedUser = ClientSystem.getLoggedUser();
        assertTrue(isLoggedIn);
        assertEquals("Amir", loggedUser.getUsername());
    }

    @Test
    public void testLogInBadParameters()
    {
        assertFalse(userUnit.logIn("",""));
    }

    @Test
    public void testLogInNotExistingUser()
    {
        assertFalse(userUnit.logIn("HelloHello",""));
    }

    @Test
    public void testLogInWrongPassword()
    {
        assertFalse(userUnit.logIn("Amir","aaaa"));
    }

    @Test
    public void testRemoveNullUser()
    {
        assertFalse(userUnit.removeUser(null));
    }

    @Test
    public void testRemoveAssociationMember()
    {
        AssociationMember associationMember = new AssociationMember("Yoram", "yoram@mail.com", "abcde");
        assertTrue(userUnit.removeUser(associationMember));
    }

    @Test
    public void testRemoveFan()
    {//should remove the fan and remove it from all the pges he follows

        //get the user from the DB
        Map<String, Object> parametersUser = new HashMap<>();
        parametersUser.put("username", "Avihai");
        List<Object> users = communication.query("UserByUserName", parametersUser);
        Fan fanUser = (Fan)users.get(0);

        boolean isRemoved = userUnit.removeUser(fanUser);

        //check the pages the fan liked are no longer hold him as a follower
        Team t1 = new Team("BestTeam",true,false);
        Map<String, Object> parametersTeamPageT1 = new HashMap<>();
        parametersTeamPageT1.put("team", t1);
        List<Object> teamPageT1List = communication.query("TeamPageByTeam", parametersTeamPageT1);
        TeamPage t1Page = (TeamPage)teamPageT1List.get(0);

        //check if the page doesn't have the fan as a follower
        assertFalse(t1Page.isFollower(fanUser));
        //check the fan does not exist in the DB
        assertTrue(isRemoved);
    }

    @Test
    public void testRemoveFanInPlayer()
    {
        //get the user from the DB
        Map<String, Object> parametersUser = new HashMap<>();
        parametersUser.put("username", "Amir");
        List<Object> users = communication.query("UserByUserName", parametersUser);
        Fan fanUser = (Fan)users.get(0);

        boolean isRemoved = userUnit.removeUser(fanUser);

        //get the player from the DB to check if he became not active
        List<Object> teamUsersObjects = getAllTeamUsersOfFan(fanUser);
        Player player = (Player)teamUsersObjects.get(0);

        assertTrue(isRemoved);
        assertFalse(fanUser.getActive());
        assertFalse(player.isActive());
    }

    @Test
    public void testRemoveFanInCoach()
    {
        //get the user from the DB
        Map<String, Object> parametersUser = new HashMap<>();
        parametersUser.put("username", "Amit");
        List<Object> users = communication.query("UserByUserName", parametersUser);
        Fan fanUser = (Fan)users.get(0);

        boolean isRemoved = userUnit.removeUser(fanUser);

        //get the player and coach from the DB to check if they became not active
        List<Object> teamUsersObjects = getAllTeamUsersOfFan(fanUser);
        Coach amitCoach = (Coach)teamUsersObjects.get(0);

        assertTrue(isRemoved);
        assertFalse(fanUser.getActive());
        assertFalse(amitCoach.isActive());
    }

    @Test
    public void testRemoveFanInTeamManager()
    {
        //get the user from the DB
        Map<String, Object> parametersUser = new HashMap<>();
        parametersUser.put("username", "teamManager");
        List<Object> users = communication.query("UserByUserName", parametersUser);
        Fan fanUser = (Fan)users.get(0);

        boolean isRemoved = userUnit.removeUser(fanUser);

        //get the player and coach from the DB to check if they became not active
        List<Object> teamUsersObjects = getAllTeamUsersOfFan(fanUser);
        TeamManager teamManager = (TeamManager)teamUsersObjects.get(0);

        assertTrue(isRemoved);
        assertFalse(fanUser.getActive());
        assertFalse(teamManager.isActive());
    }



//    @Test
//    public void testAddNewRefereeBadParameters()
//    {
//        assertNull(userUnit.addNewReferee("","","","",""));
//    }

//    @Test
//    public void testAddNewReferee()
//    {
//        Referee referee = userUnit.addNewReferee("Yossi", "test@mail.com", "abcde", "Yossi", "High");
//
//        //Get the referee from the Database and see if he was saved successfully in the database
//        Referee dbReferee = getRefereeByUsernameFromDB("Yossi");
//
//        // check if the referee from the userUnit function is the right one
//        assertEquals("Yossi", referee.getName());
//        //check if the referee from the db hs the right one
//        assertEquals("Yossi", dbReferee.getName());
//    }

    @Test
    public void testLogOut()
    {
        userUnit.logIn("Amir", "abcde");
        assertTrue(userUnit.logOut());
        assertNull(ClientSystem.getLoggedUser());
    }


    private Referee getRefereeByUsernameFromDB(String username)
    {
        Map<String, Object> parametersGetReferee = new HashMap<>();
        Fan fan = getFanByUsernameFromDB(username);
        parametersGetReferee.put("fan", fan);
        List<Object> referees = communication.query("RefereeByFan", parametersGetReferee);
        return (Referee)referees.get(0);
    }

    private Fan getFanByUsernameFromDB(String username)
    {
        //get the user from the DB
        Map<String, Object> parametersUser = new HashMap<>();
        parametersUser.put("username", username);
        List<Object> users = communication.query("UserByUserName", parametersUser);
        Fan fanUser = (Fan)users.get(0);
        return fanUser;
    }

    private List<Object> getAllTeamUsersOfFan(Fan fan)
    {
        Map<String, Object> parametersTeamUser = new HashMap<>();
        parametersTeamUser.put("fan", fan);
        List<Object> teamUsers = communication.query("AllTeamUsersByFan", parametersTeamUser);
        return teamUsers;
    }

}