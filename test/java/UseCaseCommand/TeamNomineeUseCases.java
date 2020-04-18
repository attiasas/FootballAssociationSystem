package UseCaseCommand;

import BL.Client.Handlers.NomineePermissionUnit;
import BL.Communication.CommunicationNomineePermissionStub;
import DL.Team.Members.TeamManager;
import DL.Team.Members.TeamOwner;
import DL.Team.Members.TeamUser;
import DL.Team.Page.TeamPage;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Description: This UseCase Test Class will show the flow and operations of some UseCases relating to Team Owner.
 *              The UI will be shown in the command prompt.
 *
 * Use Cases:   * ID: 6.4 (Enter New Team Owner Nominee)
 *              * ID: 6.4 (Enter New Team Manager Nominee)
 *              * ID: 6.5 (Remove Team Owner Nominee)
 *              * ID: 6.5 (Remove Team Manager Nominee)
 *              * ID: 6.6 (Change Team Manager Nominee Permission)
 *
 * Remarks:     This Test Class Wont be handling mistakes of wrong input to the printed interface (entering String when expecting int...).
 *              This Test Class Wont Handle Initializing of The First Team Owner of the Team.
 *              This Test Class wont handle creating new (or logging) users, simulation can be tested with the given users when a user is logged in.
 *              Available Users Can Be Viewed in the Init method ( Logged In User: 'Dana' ).
 *
 * Assumption:  * Team Owner User is loggedIn
 *              * There are already users in the system to nominate
 */
public class TeamNomineeUseCases
{


}
