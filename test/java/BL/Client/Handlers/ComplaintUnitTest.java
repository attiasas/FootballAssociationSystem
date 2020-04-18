package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import BL.Communication.CommunicationComplaintUnitStub;
import BL.Communication.CommunicationUserUnitStub;
import DL.Game.Referee;
import DL.Team.Members.Player;
import DL.Team.Page.TeamPage;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.UserComplaint;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Description:     tests for the ComplaintUnitTest class
 * ID:              5
 **/
public class ComplaintUnitTest
{
    private ComplaintUnit complaintUnit;
    private ClientServerCommunication communication;

    @Before
    public void init()
    {
        communication = new CommunicationComplaintUnitStub();
        complaintUnit = new ComplaintUnit(communication);

        //init fans like pages
        Fan u1 = new Fan("Assaf","test@mail.com", DigestUtils.sha1Hex("abcde"));
        Fan u2 = new Fan("Amir","test@mail.com",DigestUtils.sha1Hex("abcde"));
        Fan u3 = new Fan("Amit","test@mail.com",DigestUtils.sha1Hex("abcde"));
        Fan u4 = new Fan("Dvir","test@mail.com",DigestUtils.sha1Hex("abcde"));
        Fan u5 = new Fan("Avihai","test@mail.com",DigestUtils.sha1Hex("abcde"));

        //insert users
        communication.insert(u1);
        communication.insert(u2);
        communication.insert(u3);
        communication.insert(u4);
        communication.insert(u5);

        complaintUnit.createComplaint(u1, "aaa");

    }

    @Test
    public void testCreateComplaintBadParameters()
    {
        assertFalse(complaintUnit.createComplaint(null, ""));
    }

    @Test
    public void testCreateComplaint()
    {
        Fan amir = getFanByUsernameFromDB("Amir");
        complaintUnit.createComplaint(amir, "abc");

        assertEquals("abc", getFanByUsernameFromDB("Amir").getUserComplaintsOwner().get(0).getMessage());
    }

    @Test
    public void testMakeCommentBadParameters()
    {
        assertFalse(complaintUnit.makeComment(null, ""));
    }

    @Test
    public void testMakeComment()
    {
        Fan assaf = getFanByUsernameFromDB("Assaf");
        UserComplaint uc = assaf.getUserComplaintsOwner().get(0);

        complaintUnit.makeComment(uc, "this is a comment");
        //get assaf again from the DB to see if his comment has a comment now
        assaf = getFanByUsernameFromDB("Assaf");
        assertEquals("this is a comment", assaf.getUserComplaintsOwner().get(0).getResponse());
    }

    @Test
    public void testShowUserComplaintsBadParameters()
    {
        assertNull(complaintUnit.showUserComplaints(null));
    }

    @Test
    public void testShowUserComplaints()
    {
        Fan assaf = getFanByUsernameFromDB("Assaf");
        List<UserComplaint> complaintsList = complaintUnit.showUserComplaints(assaf);

        assertEquals(1, complaintsList.size());

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

}