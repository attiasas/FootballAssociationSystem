package BL.Client.Handlers;

import BL.Client.ClientSystem;
import BL.Communication.ClientServerCommunication;
import DL.Administration.AssociationMember;
import DL.Game.Match;
import DL.Game.Referee;
import DL.Team.Members.TeamOwner;
import DL.Team.Members.TeamUser;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description:  AssociationManagementUnit responsible for basic management system operations such as adding new team etc X
 * ID:              X
 **/

public class AssociationManagementUnit {

    public static void main(String[] args) {
//        AssociationManagementUnit s = new AssociationManagementUnit(new ClientServerCommunication());
//        s.addTeam("beitar","check",new Fan("check","amit","avihay"));
    }

    ClientServerCommunication clientServerCommunication;

    /**
     * Constructor
     *
     * @param clientServerCommunication
     */
    public AssociationManagementUnit(ClientServerCommunication clientServerCommunication) {
        if (clientServerCommunication == null)
            throw new IllegalArgumentException();

        this.clientServerCommunication = clientServerCommunication;
    }

    /**
     * Adds new team to the system
     *
     * @param name
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure)
     */
    public boolean addTeam(String name, String teamUserName, Fan fan) {

        String err = "";
        if (!isValidTeamName(name)) {
            err += "Name: Team's name should contain only letters and\\or numbers. \n";
        }
        if (fan == null) {
            err += "Fan: Fan doesn't exist. \n";
        }

        Team team = new Team(name, true, false);

        if (team.getName().isEmpty()) { // couldn't create team
            err += "Team: Team doesn't exist. \n";
        }

        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        TeamUser teamUser = new TeamUser(teamUserName, true, fan, team);

        err = "";

        if (teamUser.getName().isEmpty()) {
            err += "User: User doesn't exist. \n";
        }

        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        TeamOwner teamOwner = new TeamOwner(team, teamUser);

        boolean teamInsertion = clientServerCommunication.insert(team);
        boolean teamOwnerInsertion = clientServerCommunication.insert(teamOwner);

        return teamInsertion && teamOwnerInsertion;
    }

    /**
     * Removes referee from the system (i.e. deactivate it)
     *
     * @param referee
     * @return true if the operation succeeded, or false otherwise (there are still matches left for the referee/db failure/
     * invalid input)
     */
    public boolean removeReferee(Referee referee) {

        if (referee == null)
            throw new IllegalArgumentException("Illegal Arguments Insertion: \nReferee: Referee doesn't exist.\n");

        HashMap<String, Object> args = new HashMap<>();

        args.put("referee", referee);
        args.put("fan", referee.getFan());
        List<Match> nextMatches = clientServerCommunication.query("nextMatchesListByReferee", args);

        if (nextMatches != null && !nextMatches.isEmpty())
            throw new IllegalArgumentException("Cannot remove referee because it has unplayed matches left.\n");

        args.clear();
        args.put("active", false);
        args.put("fan", referee.getFan());
        boolean status = clientServerCommunication.update("setRefereeActivity", args);

        if (status) {
            referee.setActive(false);
        }

        return status;

    }

    /**
     * Signs up a user and injects it in a Referee object
     *
     * @param fan
     * @param qualification
     * @return A referee object. Returns null if the user can not be created or arguments were wrong
     */
    public boolean addNewReferee(Fan fan, String name, String qualification) {
        String err = "";
        if (fan == null) {
            err += "Fan: Fan doesn't exist.\n";
        }
        if (!isValidName(qualification)) {
            err += "Qualification: Qualification can't be empty and must contain only letters.\n";
        }
        if (!err.isEmpty()) throw new IllegalArgumentException("Illegal Arguments Insertion: \n" + err);

        // Create a new Referee with the fan we received
        Referee referee = new Referee(qualification, name, fan, true);

        if (!clientServerCommunication.insert(referee)) {
            return false;
        }
        return true;
    }

    public Team loadTeam(String teamName) {

        if (!isValidTeamName(teamName)) return null;

        HashMap<String, Object> args = new HashMap<>();
        args.put("name", teamName);
        List<Team> queryResult = clientServerCommunication.query("teamByName", args);

        if (queryResult == null)
            return null;

        return queryResult.get(0);

    }


    // ** Private methods ** //

    /**
     * Check name validation (valid name contains letters and numbers only)
     *
     * @param name
     * @return true if the name is valid
     */
    private boolean isValidTeamName(String name) {
        return name != null && name.matches("([a-zA-Z0-9]+(\\s[a-zA-Z0-9]*)*)+");
    }

    /**
     * Check name validation (valid name contains only letters)
     *
     * @param name
     * @return true if the name is valid
     */
    private boolean isValidName(String name) {

        return name != null && name.matches("([a-zA-Z]+(\\s[a-zA-Z]*)*)+");
    }

}
