package BL.Client;

import BL.Client.Handlers.AssociationManagementUnit;
import BL.Client.Handlers.TeamAssetUnit;
import BL.Communication.ClientServerCommunication;
import DL.Administration.AssociationMember;
import DL.Administration.SystemManager;
import DL.Game.Match;
import DL.Team.Assets.Stadium;
import DL.Team.Members.Coach;
import DL.Team.Members.Player;
import DL.Team.Members.TeamManager;
import DL.Team.Members.TeamOwner;
import DL.Team.Members.TeamUser;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;
import DL.Users.UserPermission;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * Description:     X ID:              X
 **/
public class ClientSystem
{

    private static User loggedUser = null;


    public static User getLoggedUser()
    {
        return loggedUser;
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


//    /**
//     *
//     * @param userName
//     * @param password
//     * @return true if user managed to log in
//     */
//    public boolean logIn (String userName, String password)
//    {
//        this.loggedUser = userUnit.logIn(userName, password);
//
//        if(this.loggedUser == null)
//        {
//            return false;
//        }
//
//        return true;
//    }
//
//
//
//    /**
//     * Signs up a user up and logging him in. This function signs a user up as a  Fan
//     * @param userName
//     * @param email
//     * @param password
//     * @return true if user was able to sign up and log in
//     */
//    public boolean signUp(String userName, String email, String password)
//    {
//        User user = userUnit.signUp(userName, email, password);
//
//        if(user == null)
//        {
//            return false;
//        }
//
//        this.loggedUser = user;
//        return true;
//    }
//
//
//    /**
//     * Signs up a new referee to the system and logging the new user that was created in
//     * @param userName
//     * @param email
//     * @param password
//     * @param name
//     * @param qualification
//     * @return
//     */
//    public boolean signUpReferee(String userName, String email, String password, String name, String qualification)
//    {
//        Referee referee = userUnit.addNewReferee(userName, email, password, name, qualification);
//        if(referee == null)
//        {
//            return false;
//        }
//
//        this.loggedUser = referee.getFan();
//        return true;
//    }
//
//
//    /**
//     * removes a user. A user can remove only himself
//     * @return
//     */
//    public boolean removeUser()
//    {
//        return userUnit.removeUser(this.loggedUser);
//    }
//
//
//
//
//
//    /**
//     * gets the complaints of a user
//     * @param user
//     * @return
//     */
//    public List<UserComplaint> showUserComplaints(User user)
//    {
//        return complaintUnit.showUserComplaints(user);
//    }
//
//
//    /**
//     * the logged user creates a complaint
//     * @param msg
//     * @return
//     */
//    public boolean createComplaint (String msg)
//    {
//        return complaintUnit.createComplaint(this.loggedUser, msg);
//    }
//
//    /**
//     *
//     * @param userComplaint
//     * @param comment
//     * @return
//     */
//    public boolean makeComment(UserComplaint userComplaint, String comment)
//    {
//        return makeComment(userComplaint, comment);
//    }

    User loggedInUser;
    TeamAssetUnit teamAssetUnit;
    AssociationManagementUnit associationManagementUnit;
    ClientServerCommunication clientServerCommunication;

    /**
     * Constructor
     * @param user - logged in user
     */
    public ClientSystem(User user) {
        loggedInUser = user;
        clientServerCommunication = new ClientServerCommunication();
        teamAssetUnit = new TeamAssetUnit(clientServerCommunication);
        associationManagementUnit = new AssociationManagementUnit(clientServerCommunication);
    }

    /**
     * @param team
     * @return stadiums list of a given team
     */
    public List<Stadium> loadTeamStadium(Team team) {

        return teamAssetUnit.loadTeamStadium(team);
    }

    /**
     * @param team
     * @return players list of a given team
     */
    public List<Player> loadTeamPlayers(Team team) {

       return teamAssetUnit.loadTeamPlayers(team);

    }

    /**
     * @param team
     * @return coaches list of a given team
     */
    public List<Coach> loadTeamCoach(Team team) {

        return teamAssetUnit.loadTeamCoach(team);
    }

    /**
     * Execute the following operation, only for authorized users: Adds a stadium to team's stadiums list
     * @param name - stadium name
     * @param team
     * @return true if adding operation succeeded
     */
    public boolean addStadium(String name, int capacity, Team team) {

        if (isTeamOwner(loggedInUser) || isAuthorizedTeamUserAdd(loggedInUser))
            return teamAssetUnit.addStadium(name, capacity, team);

        return false;

    }

    /**
     * Execute the following operation, only for authorized users: Set details of a stadium connected to a given team
     * @param name - new stadium's name
     * @param capacity - new stadium's capacity
     * @param stadium - stadium to set
     * @param teams
     * @return true if the operation succeeded (i.e. changes was set in db)
     */
    public boolean updateStadium(String name, int capacity, Stadium stadium, List<Team> teams) {

        if (isTeamOwner(loggedInUser) || isAuthorizedTeamUserEdit(loggedInUser))
            return teamAssetUnit.updateStadium(name, capacity, teams, stadium);

        return false;

    }

    /**
     * Execute the following operation, only for authorized users: Removes a stadium from team's stadiums list
     * @param name - stadium name
     * @return true if removal operation succeeded
     */
    public boolean removeStadium(String name) {

        if (isTeamOwner(loggedInUser) || isAuthorizedTeamUserRemove(loggedInUser))
            return teamAssetUnit.removeStadium(name);

        return false;

    }

    /**
     * Execute the following operation, only for authorized users: Updates team's stadiums list
     * @param team
     * @param stadiums - new stadiums list
     * @return true if the operation succeeded, or false otherwise (db failure/invalid arguments/empty stadium list)
     */
    public boolean updateTeamStadiums(Team team, List<Stadium> stadiums) {

        if (isTeamOwner(loggedInUser) || isAuthorizedTeamUserRemove(loggedInUser))
            return teamAssetUnit.updateTeamStadiums(team, stadiums);

        return false;

    }

    /**
     * Execute the following operation, only for authorized users: Set activation status of a team (active or not)
     * @param team
     * @return true if the operation succeeded (i.e. changes was set in db)
     */
    public boolean setTeamActivity(Team team, boolean active) {

        if (isTeamOwner(loggedInUser))
            return teamAssetUnit.setTeamActivity(team, active);

        return false;

    }

    /**
     * Execute the following operation, only for authorized users: Changes team status to "close"
     * @param team
     * @return true if the operation succeeded (i.e. changes was set in db)
     */
    public boolean closeTeam(Team team) {

        if (isAuthorizedSystemManagerRemove(loggedInUser))
            return teamAssetUnit.closeTeam(team);

        return false;

    }

    /**
     * Execute the following operation, only for authorized users: Adds new player to the system
     * @param team
     * @param fan - a fan user connected to the player
     * @param name
     * @param role
     * @param birthDate
     * @return true if the operation succeeded, and false otherwise (invalid input, db failure, fan exists)
     */
    public boolean addPlayer(Team team, Fan fan , String name, String role, Date birthDate) {

        if (isTeamOwner(loggedInUser) || isAuthorizedTeamUserAdd(loggedInUser))
            return teamAssetUnit.addPlayer(team, fan, name, role, birthDate);

        return false;
    }

    /**
     * Execute the following operation, only for authorized users: Edit a player details. If a detail stayed the same, it value passes also.
     * @param fan
     * @param team
     * @param name
     * @param role
     * @param birthDate
     * @return true if the operation succeeded and false otherwise (invalid input, db failure, fan doesn't exist)
     */
    public boolean editPlayer(Team team, Fan fan, String name, String role, Date birthDate) {

        if (isTeamOwner(loggedInUser) || isAuthorizedTeamUserEdit(loggedInUser))
            return teamAssetUnit.editPlayer(team, fan, name, role, birthDate);

        return false;

    }

    /**
     * Execute the following operation, only for authorized users: removes a player - i.e. turns it to be "inactive"
     * @param fan
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure, fan doesn't exist)
     */
    public boolean removePlayer(Fan fan) {

        if (isTeamOwner(loggedInUser) || isAuthorizedTeamUserRemove(loggedInUser))
            return teamAssetUnit.removePlayer(fan);

        return false;

    }

    /**
     * Execute the following operation, only for authorized users: Adds new coach to the system
     * @param team
     * @param fan - a fan user connected to the coach
     * @param name
     * @param qualification
     * @param role
     * @return true if the operation succeeded, and false otherwise (invalid input, db failure, fan exists)
     */
    public boolean addCoach(Team team, Fan fan ,String name, String qualification, String role) {

        if (isTeamOwner(loggedInUser) || isAuthorizedTeamUserAdd(loggedInUser))
            return teamAssetUnit.addCoach(team, fan, name, qualification, role);

        return false;

    }

    /**
     * Execute the following operation, only for authorized users: Edit a coach details. If a detail stayed the same, it value passes also.
     * @param team
     * @param fan
     * @param name
     * @param qualification
     * @param role
     * @return true if the operation succeeded and false otherwise (invalid input, db failure, fan doesn't exist)
     */
    public boolean editCoach(Team team, Fan fan, String name, String qualification, String role) {

        if (isTeamOwner(loggedInUser) || isAuthorizedTeamUserEdit(loggedInUser))
            return teamAssetUnit.editCoach(team, fan, name, qualification, role);

        return false;

    }

    /**
     * Execute the following operation, only for authorized users: removes a coach - i.e. turns it to be "inactive"
     * @param fan
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure, fan doesn't exist)
     */
    public boolean removeCoach(Fan fan) {

        if (isTeamOwner(loggedInUser) || isAuthorizedTeamUserRemove(loggedInUser))
            return teamAssetUnit.removeCoach(fan);

        return false;

    }

    /**
     * Execute the following operation, only for authorized users: Adds new team to the system
     * @param name
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure)
     */
    public boolean addTeam(String name, String teamUserName, Fan fan) {

        if (isAuthorizedFootballAssociationRepresentativeAdd(loggedInUser))
            return associationManagementUnit.addTeam(name, teamUserName, fan);

        return false;
    }

    /**
     * Execute the following operation, only for authorized users: Removes referee from the system (i.e. deactivate it)
     * @param referee
     * @return true if the operation succeeded, or false otherwise (there are still matches left for the referee/db failure/
     * invalid input)
     */
    public boolean removeReferee(Referee referee) {

        if (isAuthorizedFootballAssociationRepresentativeRemove(loggedInUser))
            return associationManagementUnit.removeReferee(referee);

        return false;

    }

    /**
     * Execute the following operation, only for authorized users: Signs up a user and injects it in a Referee object
     * @param fan
     * @param qualification
     * @return A referee object. Returns null if the user can not be created or arguments were wrong
     */
    public boolean addNewReferee(Fan fan, String name, String qualification) {
        if (isAuthorizedFootballAssociationRepresentativeRemove(loggedInUser))
            return associationManagementUnit.addNewReferee(fan, name, qualification);

        return false;
    }

    // Private methods //

    /**
     * @param loggedInUser
     * @return true if given user is connected to TeamOwner or false otherwise (isn't teamOwner/invalid input/db failure)
     */
    private boolean isTeamOwner(User loggedInUser) {

      TeamUser teamUser = getTeamUser(loggedInUser);

      if (teamUser == null) {
        return false;
      }

      HashMap<String, Object> args = new HashMap<>();
      args.put("teamUser", teamUser);
      List<TeamOwner> teamOwners = clientServerCommunication.query("TeamOwnerByTeamUser", args);

      // given user isn't a teamOwner
      return teamOwners != null && !teamOwners.isEmpty();
    }

    /**
     * @param loggedInUser
     * @return true if given user is connected to teamUser and owes editing permissions
     */
    private boolean isAuthorizedTeamUserEdit(User loggedInUser){

        TeamUser teamUser = getTeamUser(loggedInUser);

        if (teamUser == null) return false;

        return teamUser instanceof TeamManager &&  loggedInUser.hasPermission(UserPermission.Permission.EDIT);

    }

    /**
     * @param loggedInUser
     * @return true if given user is connected to teamUser and owes adding permissions
     */
    private boolean isAuthorizedTeamUserAdd(User loggedInUser){

        TeamUser teamUser = getTeamUser(loggedInUser);

        if (teamUser == null) return false;

        return teamUser instanceof TeamManager &&  loggedInUser.hasPermission(UserPermission.Permission.ADD);

    }

    /**
     * @param loggedInUser
     * @return true if given user is connected to teamUser and owes removal permissions
     */
    private boolean isAuthorizedTeamUserRemove(User loggedInUser){

        TeamUser teamUser = getTeamUser(loggedInUser);

        if (teamUser == null) return false;

        return teamUser instanceof TeamManager &&  loggedInUser.hasPermission(UserPermission.Permission.REMOVE);

    }

    /**
     * @param loggedInUser
     * @return true if given user is type of Association member user and owes adding permissions
     */
    private boolean isAuthorizedFootballAssociationRepresentativeAdd(User loggedInUser) {

        AssociationMember associationMember = getAssociationMember(loggedInUser);

        if (associationMember == null) return false;

        return associationMember.hasPermission(UserPermission.Permission.ADD);
    }

    /**
     * @param loggedInUser
     * @return true if given user is type of Association member user and owes adding permissions
     */
    private boolean isAuthorizedFootballAssociationRepresentativeRemove(User loggedInUser) {

        AssociationMember associationMember = getAssociationMember(loggedInUser);

        if (associationMember == null) return false;

        return associationMember.hasPermission(UserPermission.Permission.REMOVE);
    }

    /**
     * @param loggedInUser
     * @return true if given user is type of System manager user and owes removal permissions
     */
    private boolean isAuthorizedSystemManagerRemove(User loggedInUser) {

        SystemManager systemManager = getSystemManager(loggedInUser);

        if (systemManager == null) return false;

        return systemManager.hasPermission(UserPermission.Permission.REMOVE);

    }

    /**
     * @param loggedInUser
     * @return User as TeamUser instance if given user is connected to a TeamUser or null otherwise
     */
    private TeamUser getTeamUser(User loggedInUser) {

        if ( !(loggedInUser instanceof Fan) )
            return null;

        HashMap<String, Object> args = new HashMap<>();
        args.put("fan", loggedInUser);
        List<TeamUser> teamUsers = clientServerCommunication.query("teamUserByFan", args);

        if (teamUsers == null || teamUsers.isEmpty()) // there's no teamUser connected to this fan
            return null;

        TeamUser teamUser = teamUsers.get(0); // only one teamUser possible per fan

        return teamUser;
    }

    /**
     * @param loggedInUser
     * @return User as AssociationMember instance if given user is type of Association member user or null otherwise
     */
    private AssociationMember getAssociationMember(User loggedInUser) {

        if ( !(loggedInUser instanceof AssociationMember) )
            return null;

        return (AssociationMember)loggedInUser;

    }

    /**
     * @param loggedInUser
     * @return User as SystemManager instance if given user is type of SystemManager or null otherwise
     */
    private SystemManager getSystemManager(User loggedInUser) {

        if ( !(loggedInUser instanceof SystemManager) )
            return null;

        return (SystemManager) loggedInUser;
    }

}
