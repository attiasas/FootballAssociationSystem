package BL.Client.Handlers;

import BL.Communication.ClientServerCommunication;
import DL.Team.Team;

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
    public boolean addTeam(String name)
    {
        if (name == null || !isValidTeamName(name)) return false;

        Team team = new Team(name, true, false);
        boolean status = clientServerCommunication.insert(team);

        return status;
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
}
