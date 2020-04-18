package BL.Communication;

import DL.Team.Members.TeamOwner;
import DL.Team.Members.TeamUser;
import DL.Users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 */
public class CommunicationNomineePermissionStub extends ClientServerCommunication
{
    private List<User> users;
    private List<TeamOwner> owners;
    private List<TeamUser> teamUsers;

    private List<String> queryToList;

    public CommunicationNomineePermissionStub(List<User> users, List<TeamOwner> owners, List<TeamUser> teamUsers)
    {
        this.users = users;
        this.owners = owners;
        this.teamUsers = teamUsers;
    }

    public CommunicationNomineePermissionStub()
    {
        owners = new ArrayList<>();
        users = new ArrayList<>();
        teamUsers = new ArrayList<>();
    }

    private void QueriesInUse()
    {
        queryToList = new ArrayList<>();
        queryToList.add("TeamOwnerByUser"); // query = "SELECT to FROM TeamOwner to WHERE to.active = true and to.teamUser.myUser = :user"
        queryToList.add("UserByUsername"); // query = "SELECT u From User u WHERE u.username = :username"
        queryToList.add("activeTeamUserByFan"); // query = "SELECT tu from TeamUser tu WHERE tu.fan = :fan AND tu.active = true"

        // Update - Not needed in stub
        queryToList.add("updateTeamOwnersOfTeam"); // query = "update Team t set t.teamOwners = :teamOwners where t = :team"
        queryToList.add("TeamOwnerAddManageNominee"); // query = "UPDATE TeamOwner to SET to.manageNominees = :newNomineesList WHERE to =:teamOwner and to.active = true"
        queryToList.add("updateTeamManagersOfTeam"); // query = "update Team t set t.teamManagers = :teamManagers where t = :team"
        queryToList.add("setActiveTeamOwner"); // query = "UPDATE TeamOwner to SET to.active = : active where to =: teamOwner"
        queryToList.add("setActiveTeamUser"); // query = "UPDATE TeamUser tu SET tu.active = : active where tu =: teamUser"
        queryToList.add("UpdateUserPermission"); // query = "update User u set u.userPermission = :permission where u = :user"
        queryToList.add("TeamOwnerAddOwnerNominee"); // query = "UPDATE TeamOwner to SET to.ownerNominees = :newNomineesList WHERE to =:teamOwner and to.active = true"
    }

    @Override
    public List query(String queryName, Map<String, Object> parameters)
    {
        if(queryName.equals("TeamOwnerByUser"))
        {
            // query = "SELECT to FROM TeamOwner to WHERE to.active = true and to.teamUser.myUser = :user"
            List<TeamOwner> result = new ArrayList<>();

            for(TeamOwner owner : owners)
            {
                if(owner.isActive() && owner.getTeamUser().getFan().equals(parameters.get("user"))) result.add(owner);
            }

            return result;
        }
        else if(queryName.equals("UserByUsername"))
        {
            // query = "SELECT u From User u WHERE u.username = :username"
            List<User> result = new ArrayList<>();

            for(User user : users)
            {
                if(user.getUsername().equals(parameters.get("username"))) result.add(user);
            }

            return result;
        }
        else if(queryName.equals("activeTeamUserByFan"))
        {
            // query = "SELECT tu from TeamUser tu WHERE tu.fan = :fan AND tu.active = true"
            List<TeamUser> result = new ArrayList<>();

            for(TeamUser teamUser : teamUsers)
            {
                if(teamUser.isActive() && teamUser.getFan().equals(parameters.get("fan"))) result.add(teamUser);
            }

            return result;
        }

        return null;
    }

    @Override
    public boolean update(String queryName, Map<String, Object> parameters)
    {
        return true;
    }

    @Override
    public boolean insert(Object toInsert)
    {
        if(toInsert instanceof TeamOwner)
        {
            owners.add((TeamOwner)toInsert);
        }
        else if(toInsert instanceof User)
        {
            users.add((User)toInsert);
        }
        else if(toInsert instanceof TeamUser)
        {
            teamUsers.add((TeamUser)toInsert);
        }

        return true;
    }

    @Override
    public boolean delete(Object toDelete)
    {
        return true;
    }

    @Override
    public boolean transaction(List<SystemRequest> requests)
    {
        boolean res = true;

        for(int i = 0; i < requests.size() && res; i++)
        {
            SystemRequest current = requests.get(i);
            switch (current.type)
            {
                case Insert: res = insert(current.data); break;
                case Delete: res = delete(current.data); break;
                case Update: res = update(current.queryName,(Map<String,Object>)current.data); break;
                default: res = false;
            }
        }

        return res;
    }
}
