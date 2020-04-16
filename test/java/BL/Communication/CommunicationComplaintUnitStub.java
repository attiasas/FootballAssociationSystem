package BL.Communication;

import BL.Client.ClientSystem;
import DL.Game.Referee;
import DL.Team.Members.TeamUser;
import DL.Team.Page.Page;
import DL.Team.Page.TeamPage;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;
import DL.Users.UserComplaint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunicationComplaintUnitStub extends ClientServerCommunication
{
    private List<User> users;
    private List<UserComplaint> userComplaints;


    private HashMap<String,List> queryToList;

    public CommunicationComplaintUnitStub()
    {
        users = new ArrayList<>();
        userComplaints = new ArrayList<>();
    }


    @Override
    public List query(String queryName, Map<String, Object> parameters)
    {
        if(queryName.equals("UserByUserName"))
        {
            List<User> result = new ArrayList<>();
            for(User user : users)
            {
                if(user.getUsername().equals(parameters.get("username")))
                {
                    result.add(user);
                }
            }
            return result;
        }
        else if(queryName.equals("UserComplaintsByUser"))
        {
            List<List<UserComplaint>> result = new ArrayList<>();
            for(User user : users)
            {
                if(user.getUsername().equals(((User)(parameters.get("user"))).getUsername()))
                {
                    result.add(user.getUserComplaintsOwner());
                }
            }
            return result;
        }

        return null;
    }

    @Override
    public boolean update(String queryName, Map<String, Object> parameters)
    {
        if(queryName.equals("UserSetComment"))
        {
            UserComplaint currUc = null;

            for(UserComplaint uc : userComplaints)
            {
                if(uc.getMessage().equals(parameters.get("msg")))
                {
                    currUc = uc;
                }
            }

            if(currUc != null)
            {
                currUc.setResponse((String)parameters.get("comment"));
                return true;
            }

        }


        return false;
    }



    @Override
    public boolean insert(Object toInsert)
    {
        if(toInsert instanceof User)
        {
            users.add((User)toInsert);
            return true;
        }
        else if(toInsert instanceof UserComplaint)
        {
            userComplaints.add((UserComplaint)toInsert);

            return true;
        }

        return false;
    }

    @Override
    public boolean delete(Object toDelete)
    {

        return false;
    }

    @Override
    public boolean transaction(List<SystemRequest> requests)
    {
        return false;
    }

    private User getUserByUsernameFromDB(String username)
    {
        for(User user : users)
        {
            if(user.getUsername().equals(username))
            {
                return user;
            }
        }
        return null;
    }
}
