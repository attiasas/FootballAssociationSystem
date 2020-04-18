package BL.Communication;

import DL.Game.Referee;
import DL.Team.Members.Player;
import DL.Team.Members.TeamOwner;
import DL.Team.Members.TeamUser;
import DL.Team.Page.Page;
import DL.Team.Page.TeamPage;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 */
public class CommunicationUserUnitStub extends ClientServerCommunication
{

    private List<User> users;
    private List<Page> pages;
    private List<Team> teams;
    private List<TeamUser> teamUsers;
    private List<Referee> referees;

    private HashMap<String,List> queryToList;

    public CommunicationUserUnitStub()
    {
        users = new ArrayList<>();
        pages = new ArrayList<>();
        teams = new ArrayList<>();
        teamUsers = new ArrayList<>();
        referees = new ArrayList<>();
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
        else if(queryName.equals("UserByUserNameAndPassword"))
        {
            List<User> result = new ArrayList<>();
            for(User user : users)
            {
                if(user.getUsername().equals(parameters.get("username")) && user.getHashedPassword().equals(parameters.get("hashedPassword")))
                {
                    result.add(user);
                }
            }
            return result;
        }
        else if(queryName.equals("TeamUserByFan"))
        {
            List<TeamUser> result = new ArrayList<>();
            for(TeamUser teamUser : teamUsers)
            {
                if(teamUser.getFan().getUsername().equals(((User)(parameters.get("fan"))).getUsername()))
                {
                    result.add(teamUser);
                }
            }
            return result;
        }
        else if(queryName.equals("RefereeByFan"))
        {
            List<Referee> result = new ArrayList<>();
            for(Referee referee : referees)
            {
                if(referee.getFan().getUsername().equals(((User)(parameters.get("fan"))).getUsername()))
                {
                    result.add(referee);
                }
            }
            return result;
        }
        else if(queryName.equals("TeamPageByTeam"))
        {
            List<Page> result = new ArrayList<>();
            for(Page page : pages)
            {
                if(page instanceof TeamPage)
                {
                    TeamPage teamPage = (TeamPage)page;
                    if(teamPage.getTeam().getName().equals(((Team)(parameters.get("team"))).getName()))
                    {
                        result.add(teamPage);
                    }
                }
            }
            return result;
        }
        else if(queryName.equals("RefereeByFan"))
        {
            List<Referee> result = new ArrayList<>();
            for(Referee referee : referees)
            {
                if(referee.getFan().getUsername().equals(((Fan)(parameters.get("fan"))).getUsername()))
                {
                    result.add(referee);
                }
            }
            return result;
        }

//        if(queryName.equals("TeamOwnerByTeamUser"))
//        {
//            List<TeamOwner> result = new ArrayList<>();
//            for(TeamOwner owner : owners) if(owner.isMyOwner((TeamUser)parameters.get("teamUser"))) result.add(owner);
//            return result;
//        }

        return null;
    }

    @Override
    public boolean update(String queryName, Map<String, Object> parameters)
    {

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
        else if(toInsert instanceof Page)
        {
            pages.add((Page)toInsert);
            return true;
        }
        else if(toInsert instanceof  Team)
        {
            teams.add((Team)toInsert);
            return true;
        }
        else if(toInsert instanceof TeamUser)
        {
            teamUsers.add((TeamUser)toInsert);
            return true;
        }
        else if(toInsert instanceof Referee)
        {
            referees.add((Referee)toInsert);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(Object toDelete)
    {
        if(toDelete instanceof Fan)
        {
            users.remove((User)toDelete);

            Fan fanToDelete = (Fan)toDelete;
            fanToDelete.unfollowAllPages();
            return true;
        }

        return false;
    }

    @Override
    public boolean transaction(List<SystemRequest> requests)
    {
        return false;
    }
}