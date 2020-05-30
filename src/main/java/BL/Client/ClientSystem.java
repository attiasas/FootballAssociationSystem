package BL.Client;

import BL.Client.Handlers.*;
import BL.Communication.ClientServerCommunication;
import DL.Administration.AssociationMember;
import DL.Administration.SystemManager;
import DL.Game.Referee;
import DL.Team.Members.*;
import DL.Users.Fan;
import DL.Users.User;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Description:     X ID:              X
 **/
public class ClientSystem
{
    private static User loggedUser = null;
    public static ClientServerCommunication communication = new ClientServerCommunication();

    public AssociationManagementUnit associationManagementUnit;
    public ComplaintUnit complaintUnit;
    public FinancialUnit financialUnit;
    public HandleUserUnit userUnit;
    public LeagueSeasonUnit leagueSeasonUnit;
    public MatchEventUnit matchEventUnit;
    public NomineePermissionUnit nomineePermissionUnit;
    public PageUnit pageUnit;
    public PoliciesUnit policiesUnit;
    public RecommendationUnit recommendationUnit;
    public TeamAssetUnit teamAssetUnit;

    public ClientSystem()
    {
        matchEventUnit = new MatchEventUnit(communication);
        nomineePermissionUnit = new NomineePermissionUnit(communication);
        associationManagementUnit = new AssociationManagementUnit(communication);
        leagueSeasonUnit = new LeagueSeasonUnit(communication);
        teamAssetUnit = new TeamAssetUnit(communication);
        userUnit = new HandleUserUnit(communication,nomineePermissionUnit,teamAssetUnit,associationManagementUnit);
        complaintUnit = new ComplaintUnit(communication);
        policiesUnit = new PoliciesUnit(communication);
    }

    
    public static User getLoggedUser()
    {
        return loggedUser;
    }

    public enum UserTypes
    {
        None,Fan,Referee,Association,Admin,Manager, Player, Owner, Coach, OwnerManager,OwnerPlayer,OwnerCoach;
    }

    public static UserTypes getLoggedUserType()
    {
        if(loggedUser != null)
        {
            if(loggedUser instanceof AssociationMember) return UserTypes.Association;
            if(loggedUser instanceof SystemManager) return UserTypes.Admin;

            HashMap<String,Object> params = new HashMap<>();
            List queryResult;

            // check if referee
            params.put("user",loggedUser);
            queryResult = communication.query("activeRefereeByUser",params);
            if(queryResult != null && !queryResult.isEmpty()) return UserTypes.Referee;

            // check if team user
            params.clear();
            params.put("fan",loggedUser);
            queryResult = communication.query("ActiveTeamUserByFan",params);
            if(queryResult != null && !queryResult.isEmpty())
            {
                // check if team owner
                params.clear();
                params.put("user",loggedUser);
                queryResult = communication.query("TeamOwnerByUser",params);
                if(queryResult != null && !queryResult.isEmpty())
                {
                    TeamUser teamUser = (TeamUser)queryResult.get(0);
                    if(teamUser instanceof Player) return UserTypes.OwnerPlayer;
                    if(teamUser instanceof Coach) return UserTypes.OwnerCoach;
                    if(teamUser instanceof TeamManager) return UserTypes.OwnerManager;

                    return UserTypes.Owner;
                }
                else
                {
                    TeamUser teamUser = (TeamUser)queryResult.get(0);
                    if(teamUser instanceof Player) return UserTypes.Player;
                    if(teamUser instanceof Coach) return UserTypes.Coach;
                    if(teamUser instanceof TeamManager) return UserTypes.Manager;
                }
            }

            // default - Fan
            return UserTypes.Fan;
        }

        return UserTypes.None;
    }

    public static boolean logIn(User user)
    {
        if(user == null)
        {
            return false;
        }

        loggedUser = user;

        return true;
    }


    /**
     * log out from the logged user
     * @return true if the logged user was exist and now is logged out
     */
    public static boolean logOut()
    {
        if(loggedUser == null)
        {
            return false;
        }

        loggedUser = null;
        return true;
    }

    public void close()
    {
        communication.stopListener();
    }
}
