package UseCaseCommand;

import BL.Client.Handlers.NomineePermissionUnit;
import BL.Communication.CommunicationNomineePermissionStub;
import DL.Team.Members.TeamManager;
import DL.Team.Members.TeamOwner;
import DL.Team.Members.TeamUser;
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
    private static User loggedUser;
    private static NomineePermissionUnit unit;

    /**
     * Initialize Data To Test With.
     */
    public static void init()
    {
        // Set Initial Data To Work with
        List<User> users = new ArrayList<>();
        List<TeamOwner> owners = new ArrayList<>();
        List<TeamUser> teamUsers = new ArrayList<>();

        // init users
        Fan u1 = new Fan("Dana","test@mail.com","abcde");
        Fan u2 = new Fan("Assaf","test@mail.com","abcde");
        Fan u3 = new Fan("Amir","test@mail.com","abcde");
        Fan u4 = new Fan("Amit","test@mail.com","abcde");
        Fan u5 = new Fan("Dvir","test@mail.com","abcde");
        Fan u6 = new Fan("Avihai","test@mail.com","abcde");
        loggedUser = u1;
        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.add(u4);
        users.add(u5);
        users.add(u6);

        // init logged user to be an owner
        Team team = new Team("BestTeam",true,false);
        TeamUser teamUser = new TeamUser("Dana",true,u1,team);
        TeamOwner owner = new TeamOwner(team,teamUser);
        team.getTeamOwners().add(owner);
        teamUsers.add(teamUser);
        owners.add(owner);

        unit = new NomineePermissionUnit(new CommunicationNomineePermissionStub(users,owners,teamUsers));
    }

    public static void main(String[] args)
    {
        init();

        Scanner scanner = new Scanner(System.in);
        int entered = 0;

        while (entered != 6)
        {

            System.out.println("==========================================");
            System.out.println("Hello " + loggedUser.getUsername() + " ( Team Owner )");
            System.out.println("");
            System.out.println("-- Your Owner Nominees ------");
            List<TeamUser> ownerNominees = unit.getOwnerNominees(loggedUser);
            if(ownerNominees.isEmpty()) System.out.println("No Owner Nominees.");
            else
            {
                for(int i = 0; i < ownerNominees.size(); i++)
                {
                    System.out.println(i + ". Owner Name:" + ownerNominees.get(i).getName() + ", UserName: " + ownerNominees.get(i).getFan().getUsername());
                }
            }
            System.out.println("-----------------------------");
            System.out.println("-- Your Manager Nominees ----");
            List<TeamManager> managerNominees = unit.getManageNominees(loggedUser);
            if(managerNominees.isEmpty()) System.out.println("No Manager Nominees.");
            else
            {
                for(int i = 0; i < managerNominees.size(); i++)
                {
                    System.out.println(i + ". Manager Name:" + managerNominees.get(i).getName() + ", UserName: " + managerNominees.get(i).getFan().getUsername() + " | Permissions: " + managerNominees.get(i).getFan().getUserPermission());
                }
            }
            System.out.println("-----------------------------");
            System.out.println("");
            System.out.println("Manage your team nominees (Enter Operation Number):");
            System.out.println("1. Add Owner Nominee.");
            System.out.println("2. Add Manager Nominee.");
            System.out.println("3. Remove Owner Nominee.");
            System.out.println("4. Remove Manager Nominee.");
            System.out.println("5. Change Manager Nominee Permission.");
            System.out.println("6. Exit Simulation.");
            System.out.println("-----------------------------");
            System.out.println("==========================================");

            entered = scanner.nextInt();

            String userName,name;
            boolean success = false;
            int index;

            switch (entered)
            {
                case 1: System.out.println("Enter UserName To Nominate:");
                        userName = scanner.next();
                        System.out.println("Enter The Name Of The Owner:");
                        name = scanner.next();
                        success = unit.addOwnerNominee(loggedUser,userName,name);
                        if(!success) System.out.println(">>> Operation Failed <<<\n"); break;
                case 2: System.out.println("Enter UserName To Nominate:");
                        userName = scanner.next();
                        System.out.println("Enter The Name Of The Owner:");
                        name = scanner.next();
                        success = unit.addManagerNominee(loggedUser,userName,name);
                        if(!success) System.out.println(">>> Operation Failed <<<\n"); break;
                case 3: System.out.println("Enter Index Of The Owner To Remove");
                        index = scanner.nextInt();
                        if(index >= 0 && index < ownerNominees.size()) success = unit.removeOwnerNominee(loggedUser,ownerNominees.get(index));
                        if(!success) System.out.println(">>> Operation Failed <<<\n"); break;
                case 4: System.out.println("Enter Index Of The Manager To Remove");
                        index = scanner.nextInt();
                        if(index >= 0 && index < managerNominees.size()) success = unit.removeManagerNominee(loggedUser,managerNominees.get(index));
                        if(!success) System.out.println(">>> Operation Failed <<<\n"); break;
                case 5: System.out.println("Enter Index Of The Manager To Change Permission:");
                        index = scanner.nextInt();
                        System.out.println("enter 'true' for having permission, 'false' otherwise.");
                        System.out.println("Add Permission: ");
                        boolean add = scanner.nextBoolean();
                        System.out.println("Remove Permission: ");
                        boolean remove = scanner.nextBoolean();
                        System.out.println("Edit Permission: ");
                        boolean edit = scanner.nextBoolean();
                        if(index >= 0 && index < managerNominees.size()) success = unit.changeNomineePermission(loggedUser,managerNominees.get(index),add,remove,edit);
                        if(!success) System.out.println(">>> Operation Failed <<<\n"); break;
            }

        }
    }
}
