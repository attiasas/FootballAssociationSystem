package BL.Client.Handlers;

import BL.Client.ClientSystem;
import BL.Communication.ClientServerCommunication;
import DL.Game.Referee;
import DL.Team.Members.TeamUser;
import DL.Team.Team;
import org.apache.commons.codec.digest.Crypt;

import DL.Users.Fan;
import DL.Users.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Description:     X
 * ID:              X
 **/
public class HandleUserUnit
{

    private ClientServerCommunication communication;

    public HandleUserUnit (ClientServerCommunication communication)
    {
        this.communication = communication;
    }

    /**
     * Creates a Fan User
     * @param userName the username of the user that is being created
     * @param email the email of the user that is being created - should be in an email template
     * @param password the password of the user being created - can't be an empty String
     * @return A user that was signed up in the system. Returns null if user with the same username exists or one of the arguments was null or empty String
     */
    public User signUp(String userName, String email, String password)
    {
        if(password == null || email == null || userName == null || userName.equals("") || email.equals("") || password.equals(""))
        {
            return null;
        }

        //check there is no user with the same userName
        Map<String, Object> userNameQueryMap = new HashMap<>();
        userNameQueryMap.put("username", userName);

        List<User> usersWithSameUserName = communication.query("UserByUserName", userNameQueryMap);

        if(usersWithSameUserName.size() > 0)
        {//user with the same username exists
            return null;
        }

        //create the fan user

        String hashedPassword = DigestUtils.sha1Hex(password);
        Fan fan = new Fan(userName, email, hashedPassword);

        //insert the fan user to the DB
        communication.insert(fan);

        return fan;
    }


    /**
     * Removes a user. Only Fan users that are not a TeamUser and are not a Referee can be removed
     * @param user
     * @return false if the user is not a Fan and if the user is not a TeamUser and not Referee
     */
    public boolean removeUser(User user)
    {
        if(user == null)
        {
            return false;
        }

        if(!(user instanceof Fan))
        {
            return false;
        }

        Fan fanUser = (Fan)user;


        //Check the user is not a teamUser
        Map<String, Object> queryTeamUserMap = new HashMap<>();
        queryTeamUserMap.put("fan", fanUser);
        //get teamUsers with this fan
        List<Object> userTeamUsers = communication.query("TeamUserByFan", queryTeamUserMap);

        if(userTeamUsers.size() > 0)
        {// the user is a TeamUser
            return false;
        }

        //Check the user is not a Referee
        Map<String, Object> queryRefereeMap = new HashMap<>();
        queryRefereeMap.put("fan", fanUser);
        //get Referees with this fan
        List<Object> userReferees = communication.query("RefereeByFan", queryRefereeMap);

        if(userReferees.size() > 0)
        {// the user is a Referee
            return false;
        }

        //the user is just a Fan - We can remove it
        //first we remove it from all pages he likes
        fanUser.unfollowAllPages();
        //remove the user from the database
        communication.delete(fanUser);

        return true;
    }


    /**
     * Gets the user object of the user with the same username and password
     * @param userName
     * @param password
     * @return
     */
    public boolean logIn (String userName, String password)
    {
        if(userName == null || userName.equals("") || password == null || password.equals(""))
        {
            return false;
        }

        Map<String, Object> logInParameters = new HashMap<>();
        logInParameters.put("username", userName);
        logInParameters.put("hashedPassword", DigestUtils.sha1Hex(password));

        List<Object> users = communication.query("UserByUserNameAndPassword", logInParameters);

        if(users.size()<=0)
        {
            return false;
        }

        User user = (User)users.get(0); //can be only one user returned
        ClientSystem.logIn(user);

        return  true;
    }


    /**
     * Signs up a user and injects it in a Referee object
     * @param userName
     * @param email
     * @param password
     * @param name
     * @param qualification
     * @return A referee object. Returns null if the user can not be created or arguments were wrong
     */
    public Referee addNewReferee(String userName, String email, String password, String name, String qualification)
    {
        if(userName == null || userName.equals("") || email == null || email.equals("") || password == null || password.equals("") ||
                name == null || name.equals("") || qualification == null || qualification.equals(""))
        {
            return null;
        }

        // Create a new fan user that will be injected to the referee object
        Fan fan = (Fan)signUp(userName, email, password);

        if(fan == null)
        {
            return null;
        }

        // Create a new Referee with the fan that we have already created
        Referee referee = new Referee(qualification, name, fan, true);

        if(!communication.insert(referee))
        {
            return null;
        }
        return referee;
    }






}
