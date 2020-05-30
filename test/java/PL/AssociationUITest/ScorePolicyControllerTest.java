package PL.AssociationUITest;

import BL.Client.ClientSystem;
import BL.Communication.CommunicationLeagueSeasonAndPoliciesStub;
import BL.Communication.CommunicationNullStub;
import DL.Administration.AssociationMember;
import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Match;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import DL.Team.Assets.Stadium;
import DL.Team.Team;
import DL.Users.User;
import PL.TestFXBase;
import PL.main.App;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.input.KeyCode;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.testfx.api.FxAssert.verifyThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ScorePolicyControllerTest extends TestFXBase {
    private List<User> associationUser;


    @BeforeEach
    public void init() throws Exception {
        associationUser = new ArrayList<>();
        AssociationMember u = new AssociationMember("association", "a@gmail.com", DigestUtils.sha1Hex("1234"));
        associationUser.add(u);
        ClientSystem.communication = new CommunicationLeagueSeasonAndPoliciesStub(associationUser);
    }

    //create score policy
    @Test
    @Order(1)
    public void createNewScorePolicySuccessTest() throws Exception {
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("association");
        robot.clickOn("#txt_password");
        write("1234");
        robot.clickOn("LOG IN");
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(1000);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        sleep(500);
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Create Score Policy");
        sleep(500);
        robot.clickOn("#winPoints");
        write("3");
        robot.clickOn("#drawPoints");
        write("1");
        robot.clickOn("#losePoints");
        write("0");
        robot.clickOn("Okay");
        sleep(500);
        alert_dialog_has_header_and_content(
                "Success", "Score Policy added successfully!");
        verifyThat("#winPoints", (JFXTextField textField) -> {
            String text = textField.getText();
            return text.contains("3");
        });
        verifyThat("#drawPoints", (JFXTextField textField) -> {
            String text = textField.getText();
            return text.contains("1");
        });
        verifyThat("#losePoints", (JFXTextField textField) -> {
            String text = textField.getText();
            return text.contains("0");
        });
        robot.clickOn("OK");
        robot.closeCurrentWindow();
    }

    @Test
    @Order(2)
    public void createNewScorePolicyServerErrorTest() throws Exception {
        ClientSystem.communication = new CommunicationNullStub(associationUser);
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("association");
        robot.clickOn("#txt_password");
        write("1234");
        robot.clickOn("LOG IN");
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(1000);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        sleep(500);
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Create Score Policy");
        sleep(500);
        robot.clickOn("#winPoints");
        write("3");
        robot.clickOn("#drawPoints");
        write("1");
        robot.clickOn("#losePoints");
        write("0");
        robot.clickOn("Okay");
        sleep(500);
        alert_dialog_has_header_and_content(
                "Error", "There was a problem with the connection to the server. Please try again later");
        robot.clickOn("OK");
        robot.closeCurrentWindow();
    }

    @Test
    @Order(3)
    public void createNewScorePolicyErrorsTest() throws Exception {
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("association");
        robot.clickOn("#txt_password");
        write("1234");
        robot.clickOn("LOG IN");
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(1000);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        sleep(500);
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Create Score Policy");
        sleep(500);

        //empty values
        robot.clickOn("Okay");
        sleep(500);
        alert_dialog_has_header_and_content(
                "Error", "Please fill the required (*) fields.");
        robot.clickOn("OK");
        sleep(500);

        //letters
        robot.clickOn("#winPoints");
        write("a");
        robot.clickOn("#drawPoints");
        write("1");
        robot.clickOn("#losePoints");
        write("0");
        robot.clickOn("Okay");
        sleep(500);
        alert_dialog_has_header_and_content(
                "Error", "Please insert only numbers.");
        robot.clickOn("OK");
        robot.closeCurrentWindow();
    }

    @Test
    @Order(4)
    public void createNewGamePolicyAlreadyExistsTest() throws Exception {
        ScorePolicy sp = new ScorePolicy(3, 1, 0);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addScorePolicy(sp);
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("association");
        robot.clickOn("#txt_password");
        write("1234");
        robot.clickOn("LOG IN");
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(1000);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        sleep(500);
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Create Score Policy");
        sleep(500);

        robot.clickOn("#winPoints");
        write("3");
        robot.clickOn("#drawPoints");
        write("1");
        robot.clickOn("#losePoints");
        write("0");
        robot.clickOn("Okay");
        alert_dialog_has_header_and_content(
                "Error", "Score policy already exists.");
        robot.clickOn("OK");
        robot.closeCurrentWindow();
    }

    //change score policy
    @Test
    @Order(5)
    public void changeScorePolicySuccessTest() throws Exception {
        //init data
        League l = new League("check");
        Season s = new Season(2020);
        GamePolicy gp = new GamePolicy(3, 3);
        ScorePolicy sp = new ScorePolicy(3, 1, 0);
        ScorePolicy sp1 = new ScorePolicy(3, 1, 1);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeague(l);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addSeason(s);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addGamePolicy(gp);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addScorePolicy(sp);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addScorePolicy(sp1);
        LeagueSeason ls = new LeagueSeason(l, s, gp, sp, new Date());
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeagueSeason(ls);
        System.out.println(String.format("Score policy before: %s", ls.getScorePolicy()));
        //start test
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("association");
        robot.clickOn("#txt_password");
        write("1234");
        robot.clickOn("LOG IN");
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(1000);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        sleep(500);
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Change Score Policy");
        sleep(500);
        robot.clickOn("#seasons");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        sleep(500);
        robot.clickOn("#leagues");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        robot.clickOn("#scorePolicies");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        sleep(500);
        robot.clickOn("Okay");
        alert_dialog_has_header_and_content(
                "Success", "Score Policy changed successfully!");
        verifyThat("#scorePolicies", (JFXComboBox<ScorePolicy> comboField) -> {
            System.out.println(String.format("Score policy after: %s", ls.getScorePolicy()));
            ScorePolicy scorePolicy = comboField.getValue();
            return scorePolicy.getWinPoints() == ls.getScorePolicy().getWinPoints()
                    && scorePolicy.getLosePoints() == ls.getScorePolicy().getLosePoints()
                    && scorePolicy.getDrawPoints() == ls.getScorePolicy().getDrawPoints();
        });
        robot.clickOn("OK");
        robot.closeCurrentWindow();
    }

    @Test
    @Order(6)
    public void changeScorePolicyServerErrorTest() throws Exception {
        ClientSystem.communication = new CommunicationNullStub(associationUser);
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("association");
        robot.clickOn("#txt_password");
        write("1234");
        robot.clickOn("LOG IN");
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(1000);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        sleep(500);
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Change Score Policy");
        sleep(500);
        alert_dialog_has_header_and_content(
                "Error", "There was a problem with the server. please try again");
        robot.clickOn("OK");
        robot.closeCurrentWindow();
    }

    @Test
    @Order(7)
    public void changeScorePolicyParamErrorTest() throws Exception {
        //init data
        League l = new League("check");
        Season s = new Season(2020);
        GamePolicy gp = new GamePolicy(3, 3);
        ScorePolicy sp = new ScorePolicy(3, 1, 0);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeague(l);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addSeason(s);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addGamePolicy(gp);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addScorePolicy(sp);
        LeagueSeason ls = new LeagueSeason(l, s, gp, sp, new Date());
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeagueSeason(ls);
        //start test
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("association");
        robot.clickOn("#txt_password");
        write("1234");
        robot.clickOn("LOG IN");
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(1000);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        sleep(500);
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Change Score Policy");
        sleep(500);
        robot.clickOn("Okay");
        alert_dialog_has_header_and_content(
                "Error", "Please fill all the fields.");
        robot.clickOn("OK");
        robot.closeCurrentWindow();
    }

    @Test
    @Order(8)
    public void changeScorePolicyLeagueAlreadyRunningErrorTest() throws Exception {
        //init data
        League l = new League("check");
        Season s = new Season(2020);
        GamePolicy gp = new GamePolicy(3, 3);
        ScorePolicy sp = new ScorePolicy(3, 1, 0);
        ScorePolicy sp1 = new ScorePolicy(3, 1, 1);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeague(l);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addSeason(s);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addGamePolicy(gp);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addScorePolicy(sp);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addScorePolicy(sp1);
        LeagueSeason ls = new LeagueSeason(l, s, gp, sp, new Date());
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeagueSeason(ls);
        List<Match> matches = new ArrayList<>();
        Team t = new Team("first", true, false);
        Team t1 = new Team("second", true, false);
        Stadium st = new Stadium("third", 20000, t);
        matches.add(new Match(new Date(70, 10, 29), t, t1, ls, st));
        ls.setGames(matches);
        System.out.println(String.format("Match data: %s", ls.getMatches().get(0).getStartTime()));
        System.out.println(String.format("Change dat:: %s", new Date()));
        //start test
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("association");
        robot.clickOn("#txt_password");
        write("1234");
        robot.clickOn("LOG IN");
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(1000);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        sleep(500);
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Change Score Policy");
        sleep(500);
        robot.clickOn("#seasons");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        sleep(500);
        robot.clickOn("#leagues");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        robot.clickOn("#scorePolicies");
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        sleep(500);
        robot.clickOn("Okay");
        alert_dialog_has_header_and_content(
                "Error", "Sorry, you can not change the score policy because the league is already running.");
        robot.clickOn("OK");
        robot.closeCurrentWindow();
    }
}
