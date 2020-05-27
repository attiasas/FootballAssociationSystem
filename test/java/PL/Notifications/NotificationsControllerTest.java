package PL.Notifications;

import BL.Client.ClientSystem;
import BL.Client.Handlers.AssociationManagementUnit;
import BL.Client.Handlers.HandleUserUnit;
import BL.Client.Handlers.NomineePermissionUnit;
import BL.Client.Handlers.TeamAssetUnit;
import BL.Communication.ClientServerCommunication;
import DL.Game.MatchEvents.EventLog;
import DL.Game.MatchEvents.Goal;
import DL.Game.Referee;
import DL.Team.Members.Player;
import DL.Users.Fan;
import PL.main.App;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NotificationsControllerTest
{

    @Before
    public void init()
    {

    }



    //Server should be up before we run this
    @Test
    public void testReceivingNotificationsFromServer()
    {
        ClientServerCommunication communication = new ClientServerCommunication();
        HandleUserUnit userUnit = new HandleUserUnit(communication, new NomineePermissionUnit(communication), new TeamAssetUnit(communication), new AssociationManagementUnit(communication));
        userUnit.logIn("admin", "admin");



        System.out.println();

    }


    public static void sendEvent(ClientServerCommunication communication)
    {
        ClientServerCommunication client = communication;

        client.insert(new Goal(new Referee("a", "shalom", new Fan("a","a","a"), true), new EventLog(), 5, new Player()));


    }

}
