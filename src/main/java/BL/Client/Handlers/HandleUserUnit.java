package BL.Client.Handlers;

import BL.Client.ClientSystem;
import BL.Communication.ClientServerCommunication;
import BL.Communication.SystemRequest;
import DL.Administration.AssociationMember;
import DL.Administration.SystemManager;
import DL.Game.Referee;
import DL.Team.Members.Coach;
import DL.Team.Members.Player;
import DL.Team.Members.TeamManager;

import DL.Users.Fan;
import DL.Users.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Description:     This class handles all requests from the client that refers to users
 * ID:              4
 **/
public class HandleUserUnit
{

    private ClientServerCommunication communication;
    private NomineePermissionUnit nomineePermissionUnit;
    private TeamAssetUnit teamAssetUnit;
    private AssociationManagementUnit associationManagementUnit;

    public HandleUserUnit (ClientServerCommunication communication, NomineePermissionUnit nomineePermissionUnit, TeamAssetUnit teamAssetUnit, AssociationManagementUnit associationManagementUnit)
    {
        this.communication = communication;
        this.nomineePermissionUnit = nomineePermissionUnit;
        this.teamAssetUnit = teamAssetUnit;
        this.associationManagementUnit = associationManagementUnit;
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
     * deactivates the user. If the user is contained in a Referee or a TeamUser they get deactivated too. If the user is also a teamOwner he the teamOwner and
     * all his nominees get deactivated too (if there will be at least one nominee after the deactivation)
     * @param user
     * @return true if the activation succeeded
     */
    public boolean removeUser(User user)
    {
        if(user instanceof AssociationMember)
        {
            return deActivateUser(user);
        }

        if(user instanceof SystemManager)
        {
            if(getAllSystemManagers().size() <= 1)
            {//there is only one system manager so we can not remove it
                return false;
            }
            return deActivateUser(user);
        }

        if(!(user instanceof Fan))
        {//user must be a fan in that point. if not- return false
            return false;
        }

        Fan fan = (Fan)user;

        Referee userReferee = getRefereeOfUser(fan);

        boolean isRefereeRemoved = false;

        if(userReferee != null)
        {//the user is a referee. We need to remove the referee object first
            isRefereeRemoved = associationManagementUnit.removeReferee(userReferee);
            if(!isRefereeRemoved)
            {//if the referee object could not be removed - do not keep going and return false
                return false;
            }
        }

        boolean isTeamUsersRemoved = removeFanTeamUsers(fan); //remove all the teamUsers related to this fan

        if(!isTeamUsersRemoved)
        {//there was a failure in removing the teamUsers
            return false;
        }

        if(!deactivateFan(fan))
        {//there was a failure in deactivating the fan
            if(isRefereeRemoved)
            {
                activateReferee(fan);
            }
            if(isTeamUsersRemoved)
            {
                activateTeamUserByFan(fan);
            }
            return false;
        }

        return true;
    }



    private boolean removeFanTeamUsers(Fan fan)
    {
        boolean teamOwnerNeedToBeRemoved = false, teamManagerNeedToBeRemoved = false, playerNeedToBeRemoved = false, coachNeedToBeRemoved = false;
        boolean isTeamManagerRemovalFailed = false, isPlayerRemovalFailed = false, isCoachRemovalFailed = false; //will show if the TeamUsers removal failed

        List<SystemRequest> teamOwnerRemovalRequests = null; // will hold all the requests needed in order to remove a teamOwner

        List<Object> fanTeamUsersObjects = getTeamUsersOfFan(fan); //teamUsers of the fan

        if(isTeamOwner(fan))
        {//get all the requests needed for removing the team owner
            teamOwnerNeedToBeRemoved = true;
            teamOwnerRemovalRequests = nomineePermissionUnit.removeTeamOwner(fan);
            //will send all these requests to the db once we make sure all other requests worked
        }

//        if(fanTeamUsersObjects == null || fanTeamUsersObjects.size() == 0)
//        {//there are no teamUsers to remove
//            return true;
//        }

        if(fanTeamUsersObjects != null && fanTeamUsersObjects.size() > 0)
        {//the user is a teamUser. can have multiple positions as a teamUser (for example Player and a TeamManager). We will treat all of the user's jobs and then delete it

            if(listIncludeTeamManager(fanTeamUsersObjects))
            {
                teamManagerNeedToBeRemoved = true;
                isTeamManagerRemovalFailed = !nomineePermissionUnit.removeTeamManager(fan);
            }
            if(listIncludePlayer(fanTeamUsersObjects))
            {
                playerNeedToBeRemoved = true;
                isPlayerRemovalFailed = !teamAssetUnit.removePlayer(fan);
            }
            if(listIncludeCoach(fanTeamUsersObjects))
            {
                coachNeedToBeRemoved = true;
                isCoachRemovalFailed = !teamAssetUnit.removeCoach(fan);
            }
        }

        //Check if the removals succeeded and if not reactivate what's necessary

        if(isCoachRemovalFailed)
        {
            if((teamManagerNeedToBeRemoved && !isTeamManagerRemovalFailed) || (playerNeedToBeRemoved && !isPlayerRemovalFailed))
            {
                activateTeamUserByFan(fan);
            }
            return false;
        }

        if(isPlayerRemovalFailed)
        {
            if(teamManagerNeedToBeRemoved && !isTeamManagerRemovalFailed)
            {
                activateTeamUserByFan(fan);
            }
            return false;
        }

        if(isTeamManagerRemovalFailed)
        {
            return false;
        }

        if(teamOwnerNeedToBeRemoved && teamOwnerRemovalRequests!=null && !isTeamManagerRemovalFailed && !isPlayerRemovalFailed && !isCoachRemovalFailed )
        {//teamOwner need to be removed and there were no failures with removing any other teamUser
            return communication.transaction(teamOwnerRemovalRequests);
        }

        return true;

    }

    private boolean deActivateUser(User user)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user", user);
        return communication.update("DeactivateUser", parameters);
    }

    private boolean activateReferee(Fan fan)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fan", fan);
        return communication.update("SetActiveRefereeByFan", parameters);
    }

    private boolean activateTeamUserByFan(Fan fan)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fan", fan);
        return communication.update("ActivateTeamUserByFan", parameters);
    }




    private boolean listIncludeCoach(List<Object> teamUsers)
    {
        for (int i=0; i<teamUsers.size(); i++)
        {
            if(teamUsers.get(i) instanceof Coach)
            {
                return true;
            }
        }
        return false;
    }

    private boolean listIncludePlayer(List<Object> teamUsers)
    {
        for (int i=0; i<teamUsers.size(); i++)
        {
            if(teamUsers.get(i) instanceof Player)
            {
                return true;
            }
        }
        return false;
    }

    private boolean listIncludeTeamManager(List<Object> teamUsers)
    {
        for(int i=0; i<teamUsers.size(); i++)
        {
            if(teamUsers.get(i) instanceof TeamManager)
            {
                return true;
            }
        }
        return false;
    }


    private boolean isTeamOwner(User user)
    {
        Map<String, Object> parametersIsTeamOwner = new HashMap<>();
        parametersIsTeamOwner.put("user", user);
        List<Object> teamOwnersOfUser = communication.query("TeamOwnerByUser", parametersIsTeamOwner);
        return teamOwnersOfUser!=null && teamOwnersOfUser.size() > 0;
    }



    private List<Object> getTeamUsersOfFan(Fan fan)
    {
        Map<String, Object> parametersTeamUser = new HashMap<>();
        parametersTeamUser.put("fan", fan);
        List<Object> teamUsers = communication.query("ActiveTeamUserByFan", parametersTeamUser);
        return teamUsers;
    }

    private boolean deactivateFan(Fan fan)
    {
        if(!fan.unfollowAllPages())
        {
            return false;
        }

        //deactivate the user
        Map<String, Object> parametersDeactivateUser = new HashMap<>();
        parametersDeactivateUser.put("user", fan);
        return communication.update("DeactivateUser", parametersDeactivateUser);
    }

    private Referee getRefereeOfUser(Fan fan)
    {
        Map<String, Object> parametersReferee = new HashMap<>();
        parametersReferee.put("fan", fan);
        List<Object> refereesOfUser = communication.query("RefereeByFan", parametersReferee);
        if(refereesOfUser == null || refereesOfUser.size() == 0)
        {
            return null;
        }
        return (Referee)refereesOfUser.get(0);
    }


    private List<Object> getAllSystemManagers()
    {
        Map<String, Object> parametersSystemManagers = new HashMap<>();
        List<Object> teamOwnersOfUser = communication.query("SystemManagers", new HashMap<>());
        return teamOwnersOfUser;
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

        if(users == null || users.size()<=0)
        {
            return false;
        }

        User user = (User)users.get(0); //can be only one user returned
        ClientSystem.logIn(user);

        return  true;
    }

    /**
     *
     * @return true if a user was logged in already and is no longer logged in
     */
    public boolean logOut()
    {
        return ClientSystem.logOut();
    }



}
