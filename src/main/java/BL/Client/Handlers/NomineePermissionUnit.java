package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import BL.Communication.SystemRequest;
import DL.Team.Members.PageUser;
import DL.Team.Members.TeamManager;
import DL.Team.Members.TeamOwner;
import DL.Team.Members.TeamUser;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;
import DL.Users.UserPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:     This Unit responsible for managing all the nominees of team owner
 *                  Only Users That have an active TeamUser and a TeamOwner can use this unit
 * ID:              X
 * Usage:           * Query for Nominees of a user
 *                  * Update (Add/Remove) Nominees
 *                  * Changing Permissions of Managers Nominees
 **/
public class NomineePermissionUnit
{
    private TeamOwner cachedOwner;
    private ClientServerCommunication communication;

    /**
     * Constructor
     * @param communication - Object That Connects to the DataBase
     */
    public NomineePermissionUnit(ClientServerCommunication communication)
    {
        this.communication = communication;
        cachedOwner = null;
    }

    private Map<String,Object> mapOf(String k1, Object v1)
    {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put(k1,v1);
        return parameters;
    }

    private Map<String,Object> mapOf(String k1, Object v1, String k2, Object v2)
    {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put(k1,v1);
        parameters.put(k2,v2);
        return parameters;
    }

    /**
     * Handle Validation that the given user has an active teamOwner and fetching this information from the DB, storing into cache.
     * @param user - user to validate if it has a matching active teamOwner
     * @return true if the fetching is success and the user has a matching active teamOwner, false otherwise
     */
    private boolean getOwnerFromServer(User user)
    {
        if(user == null) return false;
        if(cachedOwner != null && cachedOwner.getTeamUser().getUser().equals(user)) return true; // cached user, nothing to get

        // new user, fetch info from server
        List<TeamOwner> queryResult = communication.query("TeamOwnerByUser",mapOf("user",user));

        if(queryResult == null || queryResult.isEmpty()) return false; // not a team owner or exception

        // only one can be returned from the query
        cachedOwner = queryResult.get(0);
        return true;
    }

    /**
     * This method will return all the owner active nominees of the given user if exists
     * @param user - user to fetch his nominees
     * @return list of active owners that was nominated by this user, null otherwise (user not an owner)
     */
    public List<TeamUser> getOwnerNominees(User user)
    {
        // validate Illegal arguments and fetch info
        if(!getOwnerFromServer(user)) return null;

        // get users of owners
        List<TeamUser> ownerUsers = new ArrayList<>();
        for(TeamOwner owner : cachedOwner.getOwnerNominees())
        {
            // only active owners
            if(owner.isActive()) ownerUsers.add(owner.getTeamUser());
        }

        return ownerUsers;
    }

    /**
     * This method will return all the active Manager nominees of the given user if exists
     * @param user - user to fetch his nominees
     * @return list of active managers that was nominated by this user, null otherwise (user not an owner)
     */
    public List<TeamManager> getManageNominees(User user)
    {
        // validate Illegal arguments and fetch info
        if(!getOwnerFromServer(user)) return null;

        List<TeamManager> managerUsers = new ArrayList<>();
        for(TeamManager manager : cachedOwner.getManageNominees())
        {
            // only active managers
            if(manager.isActive()) managerUsers.add(manager);
        }

        return managerUsers;
    }

    /**
     * Validate parameters for adding a new nominee, also validating user info and fetching info from server
     * @param user - the user that the new nominee belongs to
     * @param nomineeUserName - user name of the user that will become a nominee
     * @param name - the name of the teamUser that will be created if not exists
     * @return the nominee user that the username belongs to, null if validation fails (not owner, user not exists...)
     */
    private Fan validateAdding(User user, String nomineeUserName, String name)
    {
        // validate Illegal arguments and fetch info
        if(!getOwnerFromServer(user) || name == null || name.isEmpty()) return null;

        // Get User
        List<User> nomineeQueryResult = communication.query("UserByUsername",mapOf("username",nomineeUserName));
        if(nomineeQueryResult == null || nomineeQueryResult.isEmpty()) return null; // user not exists

        User potentialNominee = nomineeQueryResult.get(0);
        if(!(potentialNominee instanceof Fan)) return null; // Only fan can become teamUser

        // validate user is not a teamOwner
        List<User> ownerQueryResult = communication.query("TeamOwnerByUser",mapOf("user",potentialNominee));
        if(ownerQueryResult == null || !ownerQueryResult.isEmpty()) return null; // user is already a owner

        return (Fan)potentialNominee;
    }

    /**
     * This method will be Add a new Owner to a user by a given username and it will be a nominee of the given user
     * The nominee cannot be an active owner already
     * If the nominee is not a teamUser already, a teamUser that belongs will be belong to the user will be created
     * @param user - the owner that the new owner nominee belongs to
     * @param nomineeUserName - username of the new owner
     * @param name - the name of the new teamUser that created
     * @return - true if owner was created successfully, false otherwise (user not an owner, username not exists,
     */
    public boolean addOwnerNominee(User user, String nomineeUserName, String name)
    {
        // validate Illegal arguments and fetch info
        Fan potentialNominee = validateAdding(user,nomineeUserName,name);
        if(potentialNominee == null) return false;

        // get (or create) teamUser for the user
        List<TeamUser> teamUserQueryResult = communication.query("activeTeamUserByFan",mapOf("fan",potentialNominee));

        TeamUser teamUser;
        List<SystemRequest> serverRequests = new ArrayList<>();

        if(teamUserQueryResult == null) return false; // communication problem
        else if(teamUserQueryResult.isEmpty())
        {
            teamUser = new TeamUser(name,true,potentialNominee);
            serverRequests.add(SystemRequest.insert(teamUser));
        }
        else
        {
            teamUser = teamUserQueryResult.get(0);
        }

        // add owner and update lists
        TeamOwner owner = cachedOwner.addTeamOwnerNominee(teamUser);

        // Update Server
        Team team = cachedOwner.getTeam();
        List<TeamOwner> nominees = cachedOwner.getOwnerNominees();
        serverRequests.add(SystemRequest.insert(owner));
        serverRequests.add(SystemRequest.update("TeamOwnerAddOwnerNominee",mapOf("newNomineesList",nominees,"teamOwner",cachedOwner)));
        serverRequests.add(SystemRequest.update("updateTeamOwnersOfTeam",mapOf("teamOwners",team.teamOwners,"team",team)));

        return communication.transaction(serverRequests);
    }

    /**
     * This method will be Add a new Manager to a user by a given username and it will be a nominee of the given user
     * The nominee cannot have an active owner or a teamUser (Manager/Player/Coach) already
     * @param user - the owner that the new manager nominee belongs to
     * @param nomineeUserName - username of the new manager
     * @param name - the name of the new teamManager that created
     * @return - true if owner was created successfully, false otherwise.
     */
    public boolean addManagerNominee(User user, String nomineeUserName, String name)
    {
        // validate Illegal arguments and fetch info
        Fan potentialNominee = validateAdding(user,nomineeUserName,name);
        if(potentialNominee == null) return false;

        // get (or create) teamUser for the user
        List<TeamUser> teamUserQueryResult = communication.query("activeTeamUserByFan",mapOf("fan",potentialNominee));

        TeamManager teamManager;
        List<SystemRequest> serverRequests = new ArrayList<>();

        if(teamUserQueryResult == null) return false; // communication problem
        else if(!teamUserQueryResult.isEmpty()) return false; // potential nominee already a teamUser

        // add Manager and update lists
        teamManager = cachedOwner.addTeamManagerNominee(potentialNominee,name);

        // Update Server
        Team team = cachedOwner.getTeam();
        List<TeamManager> nominees = cachedOwner.getManageNominees();
        serverRequests.add(SystemRequest.insert(teamManager));
        serverRequests.add(SystemRequest.update("TeamOwnerAddManageNominee",mapOf("newNomineesList",nominees,"teamOwner",cachedOwner)));
        serverRequests.add(SystemRequest.update("updateTeamManagersOfTeam",mapOf("teamManagers",team.teamOwners,"team",team)));

        return communication.transaction(serverRequests);
    }

    /**
     * This method will remove (Deactivate) an owner nominee of a given user and all his nominees recursively.
     * The given user must have a active teamOwner that belongs to him
     * @param user - the user that the nominee belongs to
     * @param userNominee - the teamUser of the owner that will be removed
     * @return - true if the owner was removed (deactivated) successfully, false otherwise
     */
    public boolean removeOwnerNominee(User user, TeamUser userNominee)
    {
        if(!getOwnerFromServer(user)) return false;
        if(userNominee == null) return false;

        // getOwner from nominees
        TeamOwner toRemove = null;
        for(TeamOwner ownerInfo : cachedOwner.getOwnerNominees())
        {
            if(ownerInfo.isActive() && ownerInfo.getTeamUser().equals(userNominee)) toRemove = ownerInfo;
        }
        if(toRemove == null) return false; // not a nominee of the user

        // Deactivate All of this owner nominees
        List<SystemRequest> serverRequests = new ArrayList<>();
        deactivateNominees(toRemove,serverRequests);

        // TODO: Add Notification

        // Update Server
        return communication.transaction(serverRequests);
    }

    /**
     * Recursively deactivate an owner and his nominees
     * @param toRemove - owner to deactivate
     * @param requests - DB Requests to update the active flags of the nominees
     */
    private void deactivateNominees(TeamOwner toRemove, List<SystemRequest> requests)
    {
        if(toRemove == null) return;

        List<TeamOwner> ownerList = toRemove.getOwnerNominees();
        List<TeamManager> managerList = toRemove.getManageNominees();

        // deactivate owners nominees
        for(TeamOwner owner : ownerList)
        {
            deactivateNominees(owner,requests);
            requests.add(SystemRequest.update("setActiveTeamOwner",mapOf("active",false,"teamOwner",owner)));
        }

        // deactivate managers nominees
        for(TeamManager manager : managerList)
        {
            manager.setActive(false);
            requests.add(SystemRequest.update("setActiveTeamUser",mapOf("active",false,"teamUser",manager)));
        }

        // deactivate my owner
        toRemove.setActive(false);
        requests.add(SystemRequest.update("setActiveTeamOwner",mapOf("active",false,"teamOwner",toRemove)));
        // deactivate teamUser if the user is only an owner
        TeamUser ownerUser = toRemove.getTeamUser();
        if(!(ownerUser instanceof PageUser) && !(ownerUser instanceof TeamManager))
        {
            toRemove.getTeamUser().setActive(false);
            requests.add(SystemRequest.update("setActiveTeamUser",mapOf("active",false,"teamUser",ownerUser)));
        }
    }

    /**
     * This method will remove (Deactivate) a manager nominee of a given user
     * The given user must have an active teamOwner that belongs to him
     * @param user - the user that the nominee belongs to
     * @param nominee - the manager that will be removed (deactivated)
     * @return - true if the manager was removed (deactivated) successfully, false otherwise
     */
    public boolean removeManagerNominee(User user, TeamManager nominee)
    {
        if(!getOwnerFromServer(user)) return false;
        if(nominee == null) return false;

        // check manager is a nominee of the user
        boolean found = false;
        for(TeamManager manager : cachedOwner.getManageNominees())
        {
            if(manager.isActive() && manager.equals(nominee)) found = true;
        }
        if(!found) return false; // not an active nominee of the user

        nominee.setActive(false);

        // TODO: Add Notification

        return communication.update("setActiveTeamUser",mapOf("active",false,"teamUser",nominee));
    }

    /**
     * This method will update the permission to manage the team of a given manager
     * The manager must be a nominee of the given user
     * @param user - the owner that the manager belongs to
     * @param manager - manager that his permission will be updated
     * @param add - add permission
     * @param remove - remove permission
     * @param edit - edit permission
     * @return true if the permission was updated successfully, false otherwise
     */
    public boolean changeNomineePermission(User user, TeamManager manager, boolean add, boolean remove, boolean edit)
    {
        if(!getOwnerFromServer(user)) return false;
        if(manager == null || !manager.getTeamOwner().equals(cachedOwner)) return false; // manager is not a nominee of the user

        List<SystemRequest> requests = new ArrayList<>();

        // make new permission
        List<UserPermission.Permission> permissions = new ArrayList<>();
        if(add) permissions.add(UserPermission.Permission.ADD);
        if(remove) permissions.add(UserPermission.Permission.REMOVE);
        if(edit) permissions.add(UserPermission.Permission.EDIT);

        UserPermission userPermission = new UserPermission(permissions);
        UserPermission oldPermission = manager.getUser().getUserPermission();
        // update local
        manager.getUser().setUserPermission(userPermission);

        // TODO: ADD Notification

        // update server
        requests.add(SystemRequest.delete(oldPermission));
        requests.add(SystemRequest.insert(userPermission));
        requests.add(SystemRequest.update("UpdateUserPermission",mapOf("permission",userPermission,"user",manager.getUser())));

        return communication.transaction(requests);
    }
}
