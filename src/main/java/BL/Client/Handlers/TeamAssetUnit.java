package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import DL.Team.Assets.Stadium;
import DL.Team.Members.*;
import DL.Team.Page.Page;
import DL.Team.Page.UserPage;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;
import DL.Users.UserPermission;

import javax.management.OperationsException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description:  TeamAssetUnit responsible for  team management operations by a logged-in user X
 * ID:              X
 **/
public class TeamAssetUnit
{
    private ClientServerCommunication clientServerCommunication;

    /**
     * Constructor
     * @param clientServerCommunication
     */
    public TeamAssetUnit(ClientServerCommunication clientServerCommunication) {
        this.clientServerCommunication = clientServerCommunication;
    }

    /**
     * @param team
     * @return stadiums list of a given team
     */
    public List<Stadium> loadTeamStadium(Team team) {

        if (team == null)
            return null;

        HashMap<String, Object> args = new HashMap<>();
        args.put("team", team);
        List<Stadium> queryResult = clientServerCommunication.query("stadiumsByTeam", args);

        if (queryResult == null)
            return new ArrayList<>();

        return queryResult;
    }

    /**
     * @param team
     * @return players list of a given team
     */
    public List<Player> loadTeamPlayers(Team team) {

        if (team == null)
            return null;

        HashMap<String, Object> args = new HashMap<>();
        args.put("team", team);
        List<Player> queryResult = clientServerCommunication.query("playersByTeam", args);

        if (queryResult == null)
            return new ArrayList<>();

        return queryResult;

    }

    /**
     * @param team
     * @return coaches list of a given team
     */
    public List<Coach> loadTeamCoach(Team team) {

        if (team == null)
            return null;

        HashMap<String, Object> args = new HashMap<>();
        args.put("team", team);
        List<Coach> queryResult = clientServerCommunication.query("coachesByTeam", args);

        if (queryResult == null)
            return new ArrayList<>();

        return queryResult;
    }

    /**
     * Adds a stadium to the system and connect it to a
     * @param name - stadium name
     * @param capacity
     * @param team
     * @return true if adding operation succeeded
     */
    public boolean addStadium(String name, int capacity, Team team) {

        if (!isValidStadiumName(name) || capacity <= 0 || team == null)
            return false;

        //get stadium by name
        HashMap<String, Object> args = new HashMap<>();
        args.put("name", name);
        List<Stadium> stadiums = clientServerCommunication.query("stadiumByName", args);

        if (stadiums.size() == 1) // There already exists stadium with this name
            return false;

        Stadium stadium = new Stadium(name, capacity, team);
        boolean status = clientServerCommunication.insert(stadium);

        return status;
    }

    /**
     * Set details of a stadium connected to a given team
     * @param newName - new stadium's name
     * @param capacity - new stadium's capacity
     * @param stadium - stadium to set
     * @param teamsList
     * @return true if the operation succeeded (i.e. changes was set in db)
     */
    public boolean updateStadium(String newName, int capacity, List<Team> teamsList, Stadium stadium) {

        if (teamsList == null || stadium == null || !isValidStadiumName(newName) || capacity <= 0)
            return false;

        HashMap<String, Object> args = new HashMap<>();
        args.put("newName", newName);
        args.put("newCapacity", capacity);
        args.put("name", stadium.getName());
        args.put("teamsList", teamsList);
        boolean status = clientServerCommunication.update("setStadiumDetails", args);

        return status;
    }

    /**
     * Removes a stadium from the system (deactivate it).
     * @param name - stadium name
     * @return true if removal operation succeeded and false otherwise (db failure/ remained teams related to
     * this stadium)
     */
    public boolean removeStadium(String name) {

        //get stadium by name
        HashMap<String, Object> args = new HashMap<>();
        args.put("name", name);
        List<Stadium> stadiums = clientServerCommunication.query("stadiumByName", args);

        if (stadiums == null || stadiums.isEmpty())
            return false;

        args.clear();
        args.put("stadium", stadiums.get(0));
        List<Team> teams = clientServerCommunication.query("teamsByStadium", args);
        args.clear();
        args.put("name", name);
        if (teams != null && teams.size() == 0) // there are no teams related to this stadium
            return clientServerCommunication.update("deactivateStadium", args);

        return false;

    }

    /**
     * Updates team's stadiums list
     * @param team
     * @param stadiums - new stadiums list
     * @return true if the operation succeeded, or false otherwise (db failure/invalid arguments/empty stadium list)
     */
    public boolean updateTeamStadiums(Team team, List<Stadium> stadiums) {

        if (team == null || stadiums == null || stadiums.size() == 0) return false;

        HashMap<String, Object> args = new HashMap<>();
        args.put("newStadiumsList", stadiums);
        args.put("name", team.getName());
        boolean status = clientServerCommunication.update("updateStadiumsToTeam", args);

        return status;

    }

    /**
     * Set activation status of a team (active or not)
     * @param team
     * @return true if the operation succeeded (i.e. changes was set in db)
     */
    public boolean activateTeam(Team team) {

        return setTeamActivity(team, true);
    }

    /**
     * Removes team from the system, i.e. deactivate it
     * @param team
     * @return true if the operation succeeded, or false otherwise (invalid input, team doesn't exist, db failure)
     */
    public boolean deactivateTeam(Team team) {

        return setTeamActivity(team, false);

    }

    /**
     * Changes team status to "close"
     * @param team
     * @return true if the operation succeeded (i.e. changes was set in db)
     */
    public boolean closeTeam(Team team) {

        if (team == null)
            return false;

        HashMap<String, Object> args = new HashMap<>();

        args.put("name", team.getName());
        boolean status = clientServerCommunication.update("closeTeam", args);

        return status;

    }

    /**
     * Adds new player to the system
     * @param team
     * @param fan - a fan user connected to the player
     * @param name
     * @param role
     * @param birthDate
     * @return true if the operation succeeded, and false otherwise (invalid input, db failure, fan exists)
     */
    public boolean addPlayer(Team team, Fan fan ,String name, String role, Date birthDate) {

        if (team == null || isClosedTeam(team) || fan == null || birthDate == null || !isValidName(name) || !isValidName(role))
            return false;

        TeamUser teamUser = getTeamUserByFan(fan);
        if (teamUser != null) // a TeamUser connected to this fan already exists
            return false;

        Player player = new Player(name, true, fan, birthDate, role, team);

        boolean status = clientServerCommunication.insert(player);

        return status;
    }

    /**
     * Edit a player details. If a detail stayed the same, it value passes also.
     * @param fan
     * @param team
     * @param name
     * @param role
     * @param birthDate
     * @return true if the operation succeeded and false otherwise (invalid input, db failure, fan doesn't exist)
     */
    public boolean editPlayer(Team team, Fan fan, String name, String role, Date birthDate) {

        if (team == null || isClosedTeam(team) || birthDate == null || !isValidName(name) || !isValidName(role))
            return false;

        TeamUser teamUser = getTeamUserByFan(fan);
        if (teamUser == null) return false;

        HashMap<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("role", role);
        args.put("team", team);
        args.put("active", true);
        args.put("birthDate", birthDate);
        args.put("fan", fan);

        boolean status = clientServerCommunication.update("updatePlayerDetails", args);

        return status;

    }

    /**
     * removes a player - i.e. deactivates it
     * @param team
     * @param fan
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure, fan doesn't exist)
     */
    public boolean removePlayer(Team team, Fan fan) {

        return deactivateTeamUser(team, fan);

    }

    /**
     * Adds new coach to the system
     * @param team
     * @param fan - a fan user connected to the coach
     * @param name
     * @param qualification
     * @param role
     * @return true if the operation succeeded, and false otherwise (invalid input, db failure, fan exists)
     */
    public boolean addCoach(Team team, Fan fan ,String name, String qualification, String role) {

        if (team == null || isClosedTeam(team) || fan == null || !isValidName(name) || !isValidName(qualification) || !isValidName(role))
            return false;

        TeamUser teamUser = getTeamUserByFan(fan);
        if (teamUser != null) // a TeamUser connected to this fan already exists
            return false;

        Coach coach = new Coach(name, true, fan, qualification, role, team);

        boolean status = clientServerCommunication.insert(coach);

        return status;

    }

    /**
     * Edit a coach details. If a detail stayed the same, it value passes also.
     * @param team
     * @param fan
     * @param name
     * @param qualification
     * @param role
     * @return true if the operation succeeded and false otherwise (invalid input, db failure, fan doesn't exist)
     */
    public boolean editCoach(Team team, Fan fan, String name, String qualification, String role) {

        if (team == null || isClosedTeam(team) || fan == null || !isValidName(name) || !isValidName(qualification) || !isValidName(role))
            return false;

        TeamUser teamUser = getTeamUserByFan(fan);
        if (teamUser == null) return false;

        HashMap<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("role", role);
        args.put("team", team);
        args.put("active", true);
        args.put("qualification", qualification);
        args.put("fan", fan);

        boolean status = clientServerCommunication.update("updateCoachDetails", args);

        return status;

    }

    /**
     * removes a coach - i.e. turns it to be "inactive"
     * @param team
     * @param fan
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure, fan doesn't exist)
     */
    public boolean removeCoach(Team team, Fan fan) {

        return deactivateTeamUser(team, fan);
    }

    // ** Private methods ** //
    /**
     * Check name validation (valid name contains only letters)
     * @param name
     * @return true if the name is valid
     */
    private boolean isValidName(String name) {

        return name != null && name.matches("([a-zA-Z]+(\\s[a-zA-Z]*)*)+");
    }

    /**
     * Check name validation (valid name contains letters and numbers only)
     * @param name
     * @return true if the name is valid
     */
    private boolean isValidStadiumName(String name) {

        return name != null && name.matches("([a-zA-Z0-9]+(\\s[a-zA-Z0-9]*)*)+");
    }

    /**
     * @param fan
     * @return teamUser connected to given fan, or null if teamUser doesn't exist
     */
    private TeamUser getTeamUserByFan(Fan fan) {

        HashMap<String, Object> args = new HashMap<>();
        args.put("fan", fan);
        List<TeamUser> queryResults = clientServerCommunication.query("teamUserByFan", args);

        if (queryResults.isEmpty())
            return null;

        return queryResults.get(0); // only one TeamUser connected to fan-user

    }

    /**
     * removes a player - i.e. turns it to be "inactive"
     * @param team
     * @param fan
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure, fan doesn't exist)
     */
    private boolean deactivateTeamUser(Team team, Fan fan) {

        if (team == null || fan == null) return false;

        TeamUser teamUser;
        if ((teamUser = getTeamUserByFan(fan)) == null) return false;

        if (!teamUser.getTeam().equals(team)) return false;

        HashMap<String, Object> args = new HashMap<>();
        args.put("fan", fan);

        boolean status = clientServerCommunication.update("deactivateTeamUser", args);

        return status;

    }

    /**
     * @param team
     * @return true if the team is closed or false otherwise
     */
    private boolean isClosedTeam(Team team) {

        List<Team> closedTeams = clientServerCommunication.query("closedTeamsList", new HashMap<>());

        if (closedTeams == null) return true;

        return closedTeams.contains(team);
    }

    /**
     * Sets team activity (active or not)
     * @param team
     * @param activate - if true -> activate , if false -> deactivate
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure, doesn't doesn't exist)
     */
    private boolean setTeamActivity(Team team, boolean activate) {

        if (team == null) return false;

        HashMap<String, Object> args = new HashMap<>();
        args.put("name", team.getName());

        List<Team> queryResult = clientServerCommunication.query("teamByName", args);

        if (queryResult.isEmpty()) return false; // team doesn't exist

        args.clear();
        args.put("name", team.getName());

        String request = "";
        if (activate) request = "activateTeam";
        else request = "deactivateTeam";
        boolean status = clientServerCommunication.update(request, args);

        return status;
    }


}
