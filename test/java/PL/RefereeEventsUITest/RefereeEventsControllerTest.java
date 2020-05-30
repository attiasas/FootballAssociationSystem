package PL.RefereeEventsUITest;

import BL.Client.ClientSystem;
import BL.Communication.ClientServerCommunication;
import BL.Communication.CommunicationMatchEventUnitStub;
import DL.Administration.AssociationMember;
import DL.Administration.SystemManager;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Game.Referee;
import DL.Team.Assets.Stadium;
import DL.Team.Members.Player;
import DL.Team.Team;
import DL.Users.Fan;
import DL.Users.User;
import PL.TestFXBase;
import PL.main.App;
import com.jfoenix.controls.JFXTextField;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.testfx.api.FxAssert.verifyThat;

/**
 * Created By: Assaf, On 30/05/2020
 * Description:
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RefereeEventsControllerTest extends TestFXBase
{
    final String CONNECT_AS_GUEST = "#guestButton";
    final String MENU = "#hamburger";
    final String MANAGE_MATCHES = "#b_myMatches";
    final String CREATE_LEAGUE = "#createLeagueButton";
    final String LEAGUE_NAME_FIELD = "#leagueName";
    final String OKAY_BUTTON = "#okayButton";

    private List users,matches,referees;

    @BeforeEach
    public void init() throws Exception {
        initLocalData();
    }

    private void initLocalData()
    {
        // init data to test with
        users = new ArrayList<>();
        Fan f1 = new Fan("Assaf","test@mail.com", DigestUtils.sha1Hex("abcd"));
        Fan f2 = new Fan("Amir","test@mail.com",DigestUtils.sha1Hex("abcd"));
        Fan f3 = new Fan("Amit","test@mail.com",DigestUtils.sha1Hex("abcd"));
        Fan f4 = new Fan("Avihai","test@mail.com",DigestUtils.sha1Hex("abcd"));
        Fan f5 = new Fan("Dvir","test@mail.com",DigestUtils.sha1Hex("abcd"));
        AssociationMember f6 = new AssociationMember("test","test@mail.com",DigestUtils.sha1Hex("abcd"));
        SystemManager f7 = new SystemManager("test2","test@mail.com",DigestUtils.sha1Hex("abcd"));
        users.add(f1);
        users.add(f2);
        users.add(f3);
        users.add(f4);
        users.add(f5);
        users.add(f6);
        users.add(f7);

        //ClientSystem.logIn(f1);

        referees = new ArrayList<>();
        Referee r1 = new Referee("main","Referee-A",f1,true);
        referees.add(r1);

        League league = new League("Super League");
        Season season = new Season(2020);
        LeagueSeason leagueSeason = new LeagueSeason(league,season,new GamePolicy(),new ScorePolicy(),new java.sql.Date(2020,1,1));

        Team t1 = new Team("TeamA",true,false);
        Team t2 = new Team("TeamB",true,false);

        Player p1 = new Player("playerA",true,f2,new Date(2000,1,1),"attacker",t1);
        Player p2 = new Player("playerB",true,f3,new Date(2000,1,1),"attacker",t1);
        Player p3 = new Player("playerC",true,f4,new Date(2000,1,1),"defender",t1);
        Player p4 = new Player("playerA",true,f5,new Date(2000,1,1),"attacker",t2);
        t1.addPlayer(p1);
        t1.addPlayer(p2);
        t1.addPlayer(p3);
        t2.addPlayer(p4);

        Stadium stadium = new Stadium("Stadium",100,t1);

        Match m1 = new Match(new Date(new Date().getTime() + (5 * 59 * 1000)),t1,t2,leagueSeason,stadium);
        m1.setReferee(r1);
        r1.addMatch(m1);
        t1.setHomeMatches(m1);
        t2.setAwayMatches(m1);
        Match m2 = new Match(Date.from(Instant.from(LocalDate.of(2019,1,2).atStartOfDay(ZoneId.systemDefault()))),t1,t2,leagueSeason,stadium);
        m2.setReferee(r1);
        r1.addMatch(m2);
        t1.setHomeMatches(m2);
        t2.setAwayMatches(m2);
        Match m3 = new Match(Date.from(Instant.from(LocalDate.of(2019,1,3).atStartOfDay(ZoneId.systemDefault()))),t1,t2,leagueSeason,stadium);
        r1.addMatch(m3);
        t1.setHomeMatches(m3);
        t2.setAwayMatches(m3);
        Match m4 = new Match(Date.from(Instant.from(LocalDate.of(2019,1,4).atStartOfDay(ZoneId.systemDefault()))),t1,t2,leagueSeason,stadium);
        m4.setReferee(r1);
        r1.addMatch(m4);
        t1.setHomeMatches(m4);
        t2.setAwayMatches(m4);

        matches = new ArrayList<>();
        matches.add(m1);
        matches.add(m2);
        matches.add(m3);
        matches.add(m4);

        ClientSystem.communication = new CommunicationMatchEventUnitStub(matches,referees,users);
    }

    @Test
    @Order(1)
    public void createNewEventSuccessTest() throws Exception {
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("Assaf");
        robot.clickOn("#txt_password");
        write("abcd");
        robot.clickOn("LOG IN");
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(500);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        robot.clickOn(MENU);
        sleep(500);
        robot.clickOn(MANAGE_MATCHES);
        sleep(500);
        /*robot.clickOn(CREATE_LEAGUE);
        sleep(500);
        robot.clickOn(LEAGUE_NAME_FIELD);
        write("check League");
        robot.clickOn(OKAY_BUTTON);
        sleep(500);
        alert_dialog_has_header_and_content(
                "Succeeded", "New league added successfully");
        verifyThat(LEAGUE_NAME_FIELD, (JFXTextField textField) -> {
            String text = textField.getText();
            return text.contains("check League");
        });
        robot.clickOn("OK");
        robot.closeCurrentWindow();*/
    }

    @Test
    @Order(2)
    public void createNewEventErrorTest() throws Exception {}

}
