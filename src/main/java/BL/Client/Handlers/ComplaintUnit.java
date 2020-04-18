package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import DL.Users.User;
import DL.Users.UserComplaint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:     This class handles all requests from the client the refers to complaints
 * ID:              5
 **/
public class ComplaintUnit
{


    private ClientServerCommunication communication;

    public ComplaintUnit (ClientServerCommunication communication)
    {
        this.communication = communication;
    }


    /**
     * get all the complaints of a user
     * @param user
     * @return
     */
    public List<UserComplaint> showUserComplaints(User user)
    {
        if(user == null)
        {
            return null;
        }

        Map<String, Object> parametersUserComplaints = new HashMap<>();
        parametersUserComplaints.put("user", user);
        List<Object> serverUserComplaints = communication.query("UserComplaintsByUser", parametersUserComplaints);

        //we will get a list of lists with the userComplaints of each username that has this username
        serverUserComplaints = (List)serverUserComplaints.get(0);

        //make a list of UserComplaints from the objects list we received from the server
        List<UserComplaint> userComplaints = new ArrayList<>();

        for(int i=0; i<serverUserComplaints.size(); i++)
        {
            userComplaints.add((UserComplaint)serverUserComplaints.get(i));
        }

        return userComplaints;
    }

    /**
     * Creates a complaint
     * @param user
     * @param msg
     * @return
     */
    public boolean createComplaint (User user, String msg)
    {
        if(user == null || msg == null || msg.equals(""))
        {
            return false;
        }

        UserComplaint userComplaint = new UserComplaint(user, msg);

        //connect the new UserComplaint to the user
        if(!user.addUserComplaint(userComplaint))
        {
            return false;
        }

        communication.insert(userComplaint);

        return true;
    }


    /**
     * make comment to an existing userComplaint
     * @param userComplaint
     * @param comment
     * @return
     */
    public boolean makeComment(UserComplaint userComplaint, String comment)
    {
        if(userComplaint == null || comment == null || comment.equals(""))
        {
            return false;
        }

        //update on client
        userComplaint.setResponse(comment);

        //update on the DB
        Map<String, Object> updateUserComplaintParameters = new HashMap<>();
        updateUserComplaintParameters.put("comment", comment);

        //this will be with the persistence DB that will generate the id attribute
        //updateUserComplaintParameters.put("id", userComplaint.getId());

        //for now we will use the msg as kind of identifier
        updateUserComplaintParameters.put("msg", userComplaint.getMessage());
        return communication.update("UserSetComment", updateUserComplaintParameters);
    }



}
