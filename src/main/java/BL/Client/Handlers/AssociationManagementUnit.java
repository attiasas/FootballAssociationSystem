package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import DL.Game.Match;
import DL.Game.Referee;
import DL.Team.Members.TeamOwner;
import DL.Team.Members.TeamUser;
import DL.Team.Team;
import DL.Users.Fan;

import java.util.HashMap;
import java.util.List;

/**
 * Description:  AssociationManagementUnit responsible for basic management system operations such as adding new team etc X
 * ID:              X
 **/

public class AssociationManagementUnit {

    ClientServerCommunication clientServerCommunication;

    /**
     * Constructor
     * @param clientServerCommunication
     */
    public AssociationManagementUnit(ClientServerCommunication clientServerCommunication) {
        if (clientServerCommunication == null)
            throw new IllegalArgumentException();

        this.clientServerCommunication = clientServerCommunication;
    }

    /**
     * Adds new team to the system
     * @param name
     * @return true if the operation succeeded, or false otherwise (invalid input, db failure)
     */
    public boolean addTeam(String name, String teamUserName, Fan fan) {

        if (name == null || !isValidTeamName(name) || fan == null) return false;

        Team team = new Team(name, true, false);
        TeamUser teamUser = new TeamUser(teamUserName, true, fan, team);
        TeamOwner teamOwner = new TeamOwner(team, teamUser);
        boolean teamInsertion = clientServerCommunication.insert(team);
        boolean teamOwnerInsertion = clientServerCommunication.insert(teamOwner);

        return teamInsertion && teamOwnerInsertion;
    }

    /**
     * Removes referee from the system (i.e. deactivate it)
     * @param referee
     * @return true if the operation succeeded, or false otherwise (there are still matches left for the referee/db failure/
     * invalid input)
     */
    public boolean removeReferee(Referee referee) {

        if (referee == null) return false;

        HashMap<String, Object> args = new HashMap<>();

        args.put("referee", referee);
        args.put("fan", referee.getFan());
        List<Match> nextMatches = clientServerCommunication.query("nextMatchesListByReferee", args);

        if (nextMatches != null && !nextMatches.isEmpty()) return false;

        args.clear();
        args.put("active", false);
        args.put("fan", referee.getFan());
        boolean status = clientServerCommunication.update("setRefereeActivity", args);

        return status;

    }

    /**
     * Signs up a user and injects it in a Referee object
     * @param fan
     * @param qualification
     * @return A referee object. Returns null if the user can not be created or arguments were wrong
     */
    public boolean addNewReferee(Fan fan, String name, String qualification) {
        if(fan == null || qualification == null || qualification.equals(""))
        {
            return false;
        }

        // Create a new Referee with the fan we received
        Referee referee = new Referee(qualification, name, fan, true);

        if(!clientServerCommunication.insert(referee))
        {
            return false;
        }
        return true;
    }

    // ** Private methods ** //
    /**
     * Check name validation (valid name contains letters and numbers only)
     * @param name
     * @return true if the name is valid
     */
    private boolean isValidTeamName(String name)
    {
        return name != null && name.matches("([a-zA-Z0-9]+(\\s[a-zA-Z0-9]*)*)+");
    }


    /**
     * Signs up a user and injects it in a Referee object
     * @param fan
     * @param qualification
     * @return A referee object. Returns null if the user can not be created or arguments were wrong
     */
    public boolean addNewReferee(Fan fan, String name, String qualification)
    {
        if(fan == null || qualification == null || qualification.equals(""))
        {
            return false;
        }

        // Create a new Referee with the fan we received
        Referee referee = new Referee(qualification, name, fan, true);

        if(!clientServerCommunication.insert(referee))
        {
            return false;
        }
        return true;
    }

    public boolean removeReferee(Referee referee)
    {
        return false;
    }
}
