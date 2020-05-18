package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import DL.Game.Match;
import DL.Team.Assets.Stadium;
import DL.Team.Members.*;
import DL.Team.Page.Page;
import DL.Team.Page.UserPage;
import DL.Team.Team;
import DL.Users.*;

import java.util.*;

/**
 * Description:  TeamAssetUnit responsible for  team management operations by a logged-in user X
 * ID:              X
 **/
public class TeamAssetUnit {
    private ClientServerCommunication clientServerCommunication;

    /**
     * Constructor
     *
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
     *
     * @param name     - stadium name
     * @param capacity
     * @param team
     * @return true if adding operation succeeded
     */
    public boolean addStadium(String name, int capacity, Team team) {

        if (!isValidStadiumName(name) || capacity <= 0 || team == null || isClosedTeam(team))
            return false;


        //get stadium by name
        HashMap<String, Object> args = new HashMap<>();
        args.put("name", name);
        List<Stadium> stadiums = clientServerCommunication.query("stadiumByName", args);

        if (stadiums.size() == 1) // There already exists stadium with this name
            return false;

        Stadium stadium = new Stadium(name, capacity, team);

        boolean status = clientServerCommunication.insert(stadium);

        if (status)
        {
            // update in client
            if (!team.addStadium(stadium)) return false;
            if (!stadium.addTeam(team)) return false;
        }

        return status;
    }

    /**
     * Set details of a stadium connected to a given team
     *
     * @param newName   - new stadium's name
     * @param capacity  - new stadium's capacity
     * @param stadium   - stadium to set
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

        if (status)
        {
            if (!stadium.setDetails(newName, capacity, teamsList)) return false;

            for (Team t : teamsList) t.addStadium(stadium);
        }

        return status;
    }

    /**
     * Removes a stadium from the system (deactivate it).
     *
     * @param name - stadium name
     * @return true if removal operation succeeded and false otherwise (db failure/ remained teams related to
     * this stadium)
     */
    public boolean removeStadium(String name) {

        boolean status = false;

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
        args.put("active", false);
        if (teams != null && teams.size() == 0) // there are no teams related to this stadium
            status = clientServerCommunication.update("setStadiumActivity", args);

        if (status)
        {
            // set in client
            stadiums.get(0).setActive(false);
        }

        return status;

    }

    /**
     * Updates team's stadiums list
     *
     * @param team
     * @param stadiums - new stadiums list
     * @return true if the operation succeeded, or false otherwise (db failure/invalid arguments/empty stadium list)
     */
    public boolean updateTeamStadiums(Team team, List<Stadium> stadiums) {

        if (team == null || stadiums == null || stadiums.size() == 0 || isClosedTeam(team)) return false;

        HashMap<String, Object> args = new HashMap<>();
        args.put("newStadiumsList", stadiums);
        args.put("name", team.getName());
        boolean status = clientServerCommunication.update("updateStadiumsToTeam", args);

        if (status)
        {
            if (!team.setStadiumsList(stadiums)) return false;

            for (Stadium stadium : stadiums) stadium.addTeam(team);
        }

        return status;

    }

    /**
     * Sets team activity (active or not)
     * @param team
     * @param active - if true -> activate , if false -> deactivate
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure, doesn't doesn't exist)
     */
    public boolean setTeamActivity(Team team, boolean active) {

        if (team == null || isClosedTeam(team)) return false;

        if (active == false && remainedMatches(team)) return false;

        HashMap<String, Object> args = new HashMap<>();
        args.put("name", team.getName());

        List<Team> queryResult = clientServerCommunication.query("teamByName", args);

        if (queryResult.isEmpty()) return false; // team doesn't exist

        args.clear();
        args.put("name", team.getName());
        args.put("active", active);

        boolean status = clientServerCommunication.update("SetTeamActivity", args);

        if (status)
        {
            team.setActive(active);
        }

        Notifiable closeTeamNot = new Notifiable() {

            public Notification getNotification() {
                if (active)
                    return new Notification(String.format("Team: %s is open. \n You will get notifications related to it from now on.", team.getName()));

                return new Notification(String.format("Team: %s was closed temporarily. \n You will no longer get notifications about it.", team.getName()));
            }

            public Set getNotifyUsersList() {
                return team.getPage().getFollowers();
            }
        };

        return status;
    }

    /**
     * Changes team status to "close"
     *
     * @param team
     * @return true if the operation succeeded (i.e. changes was set in db)
     */
    public boolean closeTeam(Team team) {

        if (team == null || isClosedTeam(team) || remainedMatches(team))
            return false;

        HashMap<String, Object> args = new HashMap<>();

        args.put("name", team.getName());
        boolean status = clientServerCommunication.update("closeTeam", args);

        if (status)  team.setClose(true);

        Notifiable closeTeamPermanentlyNot = new Notifiable() {

            public Notification getNotification() {
                return new Notification(String.format("Team: %s was closed permanently. \n You will no longer get notifications about it.", team.getName()));
            }

            public Set getNotifyUsersList() {
                return team.getPage().getFollowers();
            }
        };

        return status;

    }

    /**
     * Adds new player to the system
     *
     * @param team
     * @param fan       - a fan user connected to the player
     * @param name
     * @param role
     * @param birthDate
     * @return true if the operation succeeded, and false otherwise (invalid input, db failure, fan exists)
     */
    public boolean addPlayer(Team team, Fan fan, String name, String role, Date birthDate) {

        if (team == null || isClosedTeam(team) || fan == null || birthDate == null || !isValidName(name) || !isValidName(role))
            return false;

        TeamUser teamUser = getTeamUserByFan(fan);
        if (teamUser != null) // a TeamUser connected to this fan already exists
            return false;

        Player player = new Player(name, true, fan, birthDate, role, team);

        boolean status = clientServerCommunication.insert(player);

        if (status)
        {
            if (!team.addPlayer(player)) return false;
        }

        return status;
    }

    /**
     * Edit a player details. If a detail stayed the same, it value passes also.
     *
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
        if (teamUser == null || !(teamUser instanceof Player)) return false;

        HashMap<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("role", role);
        args.put("team", team);
        args.put("active", true);
        args.put("birthDate", birthDate);
        args.put("fan", fan);

        boolean status = clientServerCommunication.update("updatePlayerDetails", args);

        if (status)
        {
            ((Player) teamUser).setDetails(name, role, team, true, birthDate);
        }

        return status;

    }

    /**
     * removes a player - i.e. deactivates it
     *
     * @param fan
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure, fan doesn't exist)
     */
    public boolean removePlayer(Fan fan) {

        return deactivateTeamUser(fan, "Player");

    }

    /**
     * Adds new coach to the system
     *
     * @param team
     * @param fan           - a fan user connected to the coach
     * @param name
     * @param qualification
     * @param role
     * @return true if the operation succeeded, and false otherwise (invalid input, db failure, fan exists)
     */
    public boolean addCoach(Team team, Fan fan, String name, String qualification, String role) {

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
     *
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
        if (teamUser == null || !(teamUser instanceof Coach)) return false;

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
     *
     * @param fan
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure, fan doesn't exist)
     */
    public boolean removeCoach(Fan fan) {

        return deactivateTeamUser(fan, "Coach");

    }

    // ** Private methods ** //

    /**
     * Check name validation (valid name contains only letters)
     *
     * @param name
     * @return true if the name is valid
     */
    private boolean isValidName(String name) {

        return name != null && name.matches("([a-zA-Z]+(\\s[a-zA-Z]*)*)+");
    }

    /**
     * Check name validation (valid name contains letters and numbers only)
     *
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
     *
     * @param fan
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure, fan doesn't exist)
     */
    private boolean deactivateTeamUser(Fan fan, String type) {

        if (fan == null) return false;

        TeamUser teamUser;
        if ((teamUser = getTeamUserByFan(fan)) == null) return false;

        if (type.equals("Player")) {
            if (!(teamUser instanceof Player)) return false;
        }

        else if (type.equals("Coach")) {
            if (!(teamUser instanceof Coach)) return false;
        }

        HashMap<String, Object> args = new HashMap<>();
        args.put("teamUser", teamUser);
        args.put("active", false);

        boolean status = clientServerCommunication.update("SetActiveTeamUser", args);

        if (status)
        {
            if (type.equals("Player")) teamUser.getTeam().removePlayer((Player)teamUser);

            else if (type.equals("Coach")) teamUser.getTeam().removeCoach((Coach)teamUser);
        }

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
     * @param team
     * @return true if there are still matches left for a team to play, or false otherwise
     */
    private boolean remainedMatches(Team team) {

        HashMap<String, Object> args = new HashMap<>();
        args.put("team", team);
        List<Match> nextMatches = clientServerCommunication.query("nextMatchesListByTeam", args);

        if (nextMatches == null || nextMatches.isEmpty()) return false;

        return true;

    }
}
