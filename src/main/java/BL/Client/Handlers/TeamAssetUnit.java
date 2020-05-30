package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import DL.Game.Match;
import DL.Team.Assets.Stadium;
import DL.Team.Members.*;
import DL.Team.Page.Page;
import DL.Team.Page.UserPage;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;
import DL.Users.UserPermission;

import javax.management.OperationsException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

        String err = "";

        if (!isValidStadiumName(name)) {
            err += "Stadium name: " + name + ". Stadium name should contain only letters and\\or numbers. \n";
        }
        if (capacity <= 0 ) {
            err += "Stadium capacity: " + capacity + ". Stadium capacity should be greater than 0. \n";
        }

        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);


        //get stadium by name
        HashMap<String, Object> args = new HashMap<>();
        args.put("name", name);
        List<Stadium> stadiums = clientServerCommunication.query("stadiumByName", args);

        if (stadiums.size() == 1) // There already exists stadium with this name
            return false;

        Stadium stadium = new Stadium(name, capacity, team);
      //  if (team.getStadiums() == null)
            team.setStadiums();
        team.addStadium(stadium);
        return clientServerCommunication.insert(stadium);
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

        String err = "";

        if (stadium == null) {
            err += "Stadium: Stadium doesn't exist. \n";
        }
        if (!isValidStadiumName(newName)) {
            err += "Stadium name: " + newName + ". Stadium name should contain only letters and\\or numbers. \n";
        }
        if (capacity <= 0) {
            err += "Stadium capacity: " + capacity + ". Stadium capacity should be greater than 0. \n";
        }
        if (teamsList == null) {
            err += "Stadium teams list: Stadium teams list is undefined. \n";
        }
         if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);


        List<Team> oldTeams = stadium.getTeams();

        HashMap<String, Object> args = new HashMap<>();
        args.put("newName", newName);
        args.put("newCapacity", capacity);
        args.put("name", stadium.getName());
        args.put("teamsList", teamsList);
        boolean status = clientServerCommunication.update("setStadiumDetails", args);


        if (status) {

            stadium.getTeams().addAll(teamsList);

            for (Team team : oldTeams) {
                team.removeStadium(stadium);
            }

            for (Team team : teamsList) {
                team.addStadium(stadium);
            }
        }

        return status;
    }

    /**
     * Removes a stadium from the system (deactivate it).
     *
     * @param name - stadium name
     * @return true if removal operation succeeded and false otherwise (db failure/ remained teams related to
     * this stadium)
     * We do not allow removal of stadiums who still have teams related
     */
    public boolean removeStadium(String name) {

        //get stadium by name
        HashMap<String, Object> args = new HashMap<>();
        args.put("name", name);
        List<Stadium> stadiums = clientServerCommunication.query("stadiumByName", args);

        if (stadiums == null || stadiums.isEmpty()) {
            throw new IllegalArgumentException("Illegal Arguments Insertion: \nStadium: Stadium doesn't exist. \n");
        }

        args.clear();
        args.put("stadium", stadiums.get(0));
        List<Team> teams = clientServerCommunication.query("teamsByStadium", args);
        args.clear();
        args.put("name", name);
        args.put("active", false);
        boolean status;
        if (teams != null && teams.size() == 0) { // there are no teams related to this stadium
            status = clientServerCommunication.update("setStadiumActivity", args);
            if (status) stadiums.get(0).setActive(false);
        }
        else {
            throw new IllegalArgumentException("Cannot remove stadium: \nStadium has active teams related. \n");
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

        String err = "";
        if (team == null) {
            err += "Team: Team doesn't exist. \n";
        }
        if (stadiums == null || stadiums.isEmpty()) {
            err += "Stadiums List: Stadiums list is empty. \n";
        }
        if (team != null && isClosedTeam(team)) {
            err += "Team: Chosen team is closed. \n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        HashMap<String, Object> args = new HashMap<>();
        args.put("newStadiumsList", stadiums);
        args.put("name", team.getName());
        boolean status = clientServerCommunication.update("updateStadiumsToTeam", args);

        if (status) {
            team.setStadiumsList(stadiums);
        }

        return status;
    }

    /**
     * Sets team activity (active or not)
     * @param team
     * @param active - if true -> activate , if false -> deactivate
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure, doesn't doesn't exist)
     * We do not allow deactivation of teams who still have one of the following: players, coaches, stadiums, managers or
     * owners related, or matches left
     */
    public boolean setTeamActivity(Team team, boolean active) {

        String err = "";
        if (team == null) {
            err += "Team: Team doesn't exist. \n";
        }
        if (team != null && isClosedTeam(team)) {
            err += "Team: Team is closed. \n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        if (team.hasActiveObjectsConnected()) throw new IllegalArgumentException("Team has still active objects related. \n");

        HashMap<String, Object> args = new HashMap<>();
        args.put("name", team.getName());

        List<Team> queryResult = clientServerCommunication.query("teamByName", args);

        if (queryResult == null) return false; // server failure

        if (queryResult.isEmpty())
            throw new IllegalArgumentException("Illegal Arguments Insertion: \nTeam: Team doesn't exist. \n"); // team doesn't exist

        args.clear();
        args.put("name", team.getName());
        args.put("active", active);

        boolean status = clientServerCommunication.update("SetTeamActivity", args);

        if (status) {
            team.setActive(active);
        }

        return status;
    }

    /**
     * Changes team status to "close"
     *
     * @param team
     * @return true if the operation succeeded (i.e. changes was set in db)
     * We do not allow closure of teams who still have one of the following: players, coaches, stadiums, managers or
     * owners related, or matches left
     */
    public boolean closeTeam(Team team) {

        String err = "";
        if (team == null) {
            err += "Team: Team doesn't exist. \n";
        }
        if (team != null && isClosedTeam(team)) {
            err += "Team: Team is closed. \n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        if (team.hasActiveObjectsConnected()) throw new IllegalArgumentException("Team has still active objects related. \n");

        HashMap<String, Object> args = new HashMap<>();

        args.put("name", team.getName());
        boolean status = clientServerCommunication.update("closeTeam", args);

        if (status) {
            team.setClose(true);
        }
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

        String err = "";
        if (!isValidName(name)) {
            err += "Name: " + name + ". Name should contain only letters. \n";
        }
        if (!isValidName(role)) {
            err += "Role: " + role + ". Role should contain only letters. \n";
        }
        if (fan == null) {
            err += "Fan: Fan doesn't exist. \n";
        }
        if (birthDate == null) {
            err += "Birth Date: Birth date cannot be empty. \n";
        }
        if (birthDate != null && (Year.now().getValue() - birthDate.getYear()) < 13) {
            err += "Birth Date: Player's age cannot be younger than 13. \n";
        }
        if (team == null) {
            err += "Team: Cannot create player without team. \n";
        }
        if (team != null && team.isClose()) {
            err += "Team: Cannot add player to closed team. \n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);


        TeamUser teamUser = getTeamUserByFan(fan);
        if (teamUser != null) // a TeamUser connected to this fan already exists
            throw new IllegalArgumentException("Illegal Arguments Insertion: \nUser already has another role.\n");

        Player player = new Player(name, true, fan, birthDate, role, team);

        boolean status = clientServerCommunication.insert(player);

        if (status) {
            if (team.getPlayers() == null)
                team.setPlayers();
            team.addPlayer(player);
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

        String err = "";
        if (!isValidName(name)) {
            err += "Name: " + name + ". Name should contain only letters. \n";
        }
        if (!isValidName(role)) {
            err += "Role: " + role + ". Role should contain only letters. \n";
        }
        if (fan == null) {
            err += "Fan: Fan doesn't exist. \n";
        }
        if (birthDate == null) {
            err += "Birth Date: Birth date cannot be empty. \n";
        }
        if (birthDate != null && (Year.now().getValue()) < 13 - birthDate.getYear()) {
            err += "Birth Date: Player's age cannot be younger than 13. \n";
        }
        if (team == null) {
            err += "Team: Cannot create player without team. \n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        TeamUser teamUser = getTeamUserByFan(fan);
        if (teamUser == null)
            throw new IllegalArgumentException("Illegal Arguments Insertion: \nPlayer doesn't exist.\n");
        if (!(teamUser instanceof Player))
            throw new IllegalArgumentException("Illegal Arguments Insertion: \nChosen user isn't player.\n");

        Team oldTeam = teamUser.getTeam();

        HashMap<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("role", role);
        args.put("team", team);
        args.put("active", true);
        args.put("birthDate", birthDate);
        args.put("fan", fan);

        boolean status = clientServerCommunication.update("updatePlayerDetails", args);

        if (status && !oldTeam.equals(team)) {
            oldTeam.removePlayer((Player) teamUser);
            team.addPlayer((Player) teamUser);
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

        String err = "";
        if (!isValidName(name)) {
            err += "Name: " + name + ". Name should contain only letters. \n";
        }
        if (!isValidName(qualification)) {
            err += "Qualification: " + qualification + ". Qualification should contain only letters. \n";
        }
        if (!isValidName(role)) {
            err += "Role: " + role + ". Role should contain only letters. \n";
        }
        if (fan == null) {
            err += "Fan: Fan doesn't exist. \n";
        }
        if (team == null) {
            err += "Team: Cannot create coach without team. \n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        TeamUser teamUser = getTeamUserByFan(fan);
        if (teamUser != null) // a TeamUser connected to this fan already exists
            throw new IllegalArgumentException("Illegal Arguments Insertion: \nUser already has another role.\n");

        Coach coach = new Coach(name, true, fan, qualification, role, team);

        boolean status = clientServerCommunication.insert(coach);

        if (status) {
           // if (team.getCoaches() == null)
                team.setCoaches();
            team.addCoach(coach);
        }

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

        String err = "";
        if (!isValidName(name)) {
            err += "Name: " + name + ". Name should contain only letters. \n";
        }
        if (!isValidName(qualification)) {
            err += "Qualification: " + qualification + ". Qualification should contain only letters. \n";
        }
        if (!isValidName(role)) {
            err += "Role: " + role + ". Role should contain only letters. \n";
        }
        if (fan == null) {
            err += "Fan: Fan doesn't exist. \n";
        }
        if (team == null) {
            err += "Team: Cannot create coach without team. \n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        TeamUser teamUser = getTeamUserByFan(fan);
        if (teamUser == null)
            throw new IllegalArgumentException("Illegal Arguments Insertion: \nCoach doesn't exist.\n");
        if(!(teamUser instanceof Coach))
            throw new IllegalArgumentException("Illegal Arguments Insertion: \nChosen user isn't coach.\n");

        Team oldTeam = teamUser.getTeam();

        Coach coach = (Coach)teamUser;
        coach.setDetails(name, role, team, true, qualification);
       // HashMap<String, Object> args = new HashMap<>();
        //args.put("name", name);
        //args.put("role", role);
        //args.put("team", team);
        //args.put("active", true);
        //args.put("qualification", qualification);
        //args.put("fan", fan);

        boolean status = clientServerCommunication.merge(coach);

        if (status && !oldTeam.equals(team)) {
            oldTeam.removeCoach((Coach) teamUser);
            team.addCoach((Coach) teamUser);
        }

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

        if (queryResults == null || queryResults.isEmpty())
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

        if (fan == null) throw new IllegalArgumentException("Illegal Arguments Insertion: \nChosen fan doesn't exist.\n");

        TeamUser teamUser;
        if ((teamUser = getTeamUserByFan(fan)) == null)
            throw new IllegalArgumentException("Illegal Arguments Insertion: \nChosen user doesn't exist.\n");

        if (type.equals("Player")) {
            if (!(teamUser instanceof Player))
                throw new IllegalArgumentException("Illegal Arguments Insertion: \nChosen user isn't player.\n");
        }

        else if (type.equals("Coach")) {
            if (!(teamUser instanceof Coach))
                throw new IllegalArgumentException("Illegal Arguments Insertion: \nChosen user isn't coach.\n");
        }

        HashMap<String, Object> args = new HashMap<>();
        args.put("teamUser", teamUser);
        args.put("active", false);

        boolean status = clientServerCommunication.update("SetActiveTeamUser", args);

        if (status) {

            if (type.equals("Player")) {
                Player player = (Player) teamUser;
                player.getTeam().removePlayer(player);
            }
            else if (type.equals("Coach")) {
                Coach coach = (Coach) teamUser;
                coach.getTeam().removeCoach(coach);
            }

            teamUser.setActive(false);

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

}
