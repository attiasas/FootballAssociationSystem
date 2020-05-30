//package PL.AssociationUITest;
//
//import BL.Client.ClientSystem;
//import BL.Communication.CommunicationLeagueSeasonAndPoliciesStub;
//import BL.Communication.CommunicationNullStub;
//import DL.Administration.AssociationMember;
//import DL.Game.LeagueSeason.League;
//import DL.Game.LeagueSeason.LeagueSeason;
//import DL.Game.LeagueSeason.Season;
//import DL.Game.Policy.GamePolicy;
//import DL.Game.Policy.ScorePolicy;
//import DL.Users.User;
//import PL.TestFXBase;
//import PL.main.App;
//import com.jfoenix.controls.JFXTextField;
//import javafx.scene.input.KeyCode;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.junit.jupiter.api.*;
//import org.testfx.framework.junit.ApplicationTest;
//import org.testfx.util.WaitForAsyncUtils;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.Callable;
//import java.util.concurrent.TimeUnit;
//
//import static org.testfx.api.FxAssert.verifyThat;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class LeagueSeasonControllerTest extends TestFXBase {
//
//    final String CONNECT_AS_GUEST = "#guestButton";
//    final String MENU = "#hamburger";
//    final String MANAGE_LEAGUES = "#manageLeaguesButton";
//    final String CREATE_LEAGUE = "#createLeagueButton";
//    final String LEAGUE_NAME_FIELD = "#leagueName";
//    final String OKAY_BUTTON = "#okayButton";
//
//    private List<User> associationUser;
//
//
//    @BeforeEach
//    public void init() throws Exception {
//        associationUser = new ArrayList<>();
//        AssociationMember u = new AssociationMember("association", "a@gmail.com", DigestUtils.sha1Hex("1234"));
//        associationUser.add(u);
//        ClientSystem.communication = new CommunicationLeagueSeasonAndPoliciesStub(associationUser);
//    }
//
//    //Create League tests
//    @Test
//    @Order(1)
//    public void createNewLeagueSuccessTest() throws Exception {
//        ApplicationTest.launch(App.class);
//        robot.clickOn("#txt_username");
//        write("association");
//        robot.clickOn("#txt_password");
//        write("1234");
//        robot.clickOn("LOG IN");
//        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                sleep(500);
//                return robot.lookup("#hamburger").tryQuery().isPresent();
//            }
//        });
//        robot.clickOn(MENU);
//        sleep(500);
//        robot.clickOn(MANAGE_LEAGUES);
//        sleep(500);
//        robot.clickOn(CREATE_LEAGUE);
//        sleep(500);
//        robot.clickOn(LEAGUE_NAME_FIELD);
//        write("check League");
//        robot.clickOn(OKAY_BUTTON);
//        sleep(500);
//        alert_dialog_has_header_and_content(
//                "Succeeded", "New league added successfully");
//        verifyThat(LEAGUE_NAME_FIELD, (JFXTextField textField) -> {
//            String text = textField.getText();
//            return text.contains("check League");
//        });
//        robot.clickOn("OK");
//        robot.closeCurrentWindow();
//    }
//
//    @Test
//    @Order(2)
//    public void createNewLeagueServerErrorTest() throws Exception {
//        ClientSystem.communication = new CommunicationNullStub(associationUser);
//        ApplicationTest.launch(App.class);
//        robot.clickOn("#txt_username");
//        write("association");
//        robot.clickOn("#txt_password");
//        write("1234");
//        robot.clickOn("LOG IN");
//        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                sleep(500);
//                return robot.lookup("#hamburger").tryQuery().isPresent();
//            }
//        });
//        robot.clickOn(MENU);
//        sleep(500);
//        robot.clickOn(MANAGE_LEAGUES);
//        sleep(500);
//        robot.clickOn(CREATE_LEAGUE);
//        sleep(500);
//        robot.clickOn(LEAGUE_NAME_FIELD);
//        write("check League");
//        robot.clickOn(OKAY_BUTTON);
//        sleep(500);
//        alert_dialog_has_header_and_content(
//                "Error", "There was a problem with the connection to the server. Please try again later");
//        robot.clickOn("OK");
//        robot.closeCurrentWindow();
//    }
//
//    @Test
//    @Order(3)
//    public void createNewLeagueAlreadyExistsErrorTest() throws Exception {
//        League l = new League("check League");
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeague(l);
//        ApplicationTest.launch(App.class);
//        robot.clickOn("#txt_username");
//        write("association");
//        robot.clickOn("#txt_password");
//        write("1234");
//        robot.clickOn("LOG IN");
//        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                sleep(500);
//                return robot.lookup("#hamburger").tryQuery().isPresent();
//            }
//        });
//        robot.clickOn(MENU);
//        sleep(500);
//        robot.clickOn(MANAGE_LEAGUES);
//        sleep(500);
//        robot.clickOn(CREATE_LEAGUE);
//        sleep(500);
//        robot.clickOn(LEAGUE_NAME_FIELD);
//        write("check League");
//        robot.clickOn(OKAY_BUTTON);
//        sleep(500);
//        alert_dialog_has_header_and_content(
//                "Error", "League with that name already exists. Please insert different name.");
//        robot.clickOn("OK");
//        robot.closeCurrentWindow();
//    }
//
//    @Test
//    @Order(4)
//    public void createNewLeagueNullParamErrorTest() throws Exception {
//        ApplicationTest.launch(App.class);
//        robot.clickOn("#txt_username");
//        write("association");
//        robot.clickOn("#txt_password");
//        write("1234");
//        robot.clickOn("LOG IN");
//        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                sleep(500);
//                return robot.lookup("#hamburger").tryQuery().isPresent();
//            }
//        });
//        robot.clickOn(MENU);
//        sleep(500);
//        robot.clickOn(MANAGE_LEAGUES);
//        sleep(500);
//        robot.clickOn(CREATE_LEAGUE);
//        sleep(500);
//        robot.clickOn(LEAGUE_NAME_FIELD);
//        write("");
//        robot.clickOn(OKAY_BUTTON);
//        sleep(500);
//        alert_dialog_has_header_and_content(
//                "Error", "League name can not be empty.");
//        robot.clickOn("OK");
//        robot.closeCurrentWindow();
//    }
//
//    //Create LeagueSeason tests
//    @Test
//    @Order(5)
//    public void createNewLeagueSeasonSuccessPlusParamErrorsTest() throws Exception {
//        ApplicationTest.launch(App.class);
//        robot.clickOn("#txt_username");
//        write("association");
//        robot.clickOn("#txt_password");
//        write("1234");
//        robot.clickOn("LOG IN");
//        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                sleep(1000);
//                return robot.lookup("#hamburger").tryQuery().isPresent();
//            }
//        });
//        robot.clickOn(MENU);
//        sleep(500);
//        robot.clickOn(MANAGE_LEAGUES);
//        sleep(500);
//        robot.clickOn("Create Season");
//        sleep(500);
//        alert_dialog_has_header_and_content(
//                "Error", "Please create a new League First and then try again.");
//        robot.clickOn("OK");
//
//        //add league
//        League l = new League("check");
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeague(l);
//        robot.clickOn("Create Season");
//        sleep(500);
//        alert_dialog_has_header_and_content(
//                "Error", "Please create a new Game Policy First and then try again.");
//        robot.clickOn("OK");
//
//        //add game policy
//        GamePolicy gp = new GamePolicy(3, 2);
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addGamePolicy(gp);
//        robot.clickOn("Create Season");
//        sleep(500);
//        alert_dialog_has_header_and_content(
//                "Error", "Please create a new Score Policy First and then try again.");
//        robot.clickOn("OK");
//
//        //add score policy - should be okay now
//        ScorePolicy sp = new ScorePolicy(3, 1, 0);
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addScorePolicy(sp);
//        robot.clickOn("Create Season");
//        sleep(500);
//        clickOn("Okay");
//        alert_dialog_has_header_and_content(
//                "Error", "Please fill all the required fields.");
//        robot.clickOn("OK");
//        robot.clickOn("#season");
//        write("2020");
//        clickOn("#leagueNames");
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn("#gamePolicies");
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn("#scorePolicies");
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn("#startDate");
//        write("20/05/2020");
//        clickOn("Okay");
//        alert_dialog_has_header_and_content(
//                "Success", "League Season added successfully!");
//        robot.clickOn("OK");
//        robot.closeCurrentWindow();
//    }
//
//    @Test
//    @Order(6)
//    public void createNewLeagueSeasonLowYearErrorTest() throws Exception {
//        //init data
//        League l = new League("check");
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeague(l);
//        GamePolicy gp = new GamePolicy(3, 2);
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addGamePolicy(gp);
//        ScorePolicy sp = new ScorePolicy(3, 1, 0);
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addScorePolicy(sp);
//
//        ApplicationTest.launch(App.class);
//        robot.clickOn("#txt_username");
//        write("association");
//        robot.clickOn("#txt_password");
//        write("1234");
//        robot.clickOn("LOG IN");
//        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                sleep(1000);
//                return robot.lookup("#hamburger").tryQuery().isPresent();
//            }
//        });
//        robot.clickOn(MENU);
//        sleep(500);
//        robot.clickOn(MANAGE_LEAGUES);
//        sleep(500);
//        robot.clickOn("Create Season");
//        sleep(500);
//        robot.clickOn("#season");
//        write("1949");
//        clickOn("#leagueNames");
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn("#gamePolicies");
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn("#scorePolicies");
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn("#startDate");
//        write("20/05/2020");
//        clickOn("Okay");
//        alert_dialog_has_header_and_content(
//                "Error", "Year must be greater than 1950. Please try again.");
//        robot.clickOn("OK");
//        robot.closeCurrentWindow();
//    }
//
//    @Test
//    @Order(7)
//    public void createNewLeagueSeasonAlreadyExistsErrorTest() throws Exception {
//        //init data
//        League l = new League("check");
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeague(l);
//        GamePolicy gp = new GamePolicy(3, 2);
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addGamePolicy(gp);
//        ScorePolicy sp = new ScorePolicy(3, 1, 0);
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addScorePolicy(sp);
//        LeagueSeason ls = new LeagueSeason(l, new Season(2020), gp, sp, new Date());
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeagueSeason(ls);
//        ApplicationTest.launch(App.class);
//        robot.clickOn("#txt_username");
//        write("association");
//        robot.clickOn("#txt_password");
//        write("1234");
//        robot.clickOn("LOG IN");
//        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                sleep(1000);
//                return robot.lookup("#hamburger").tryQuery().isPresent();
//            }
//        });
//        robot.clickOn(MENU);
//        sleep(500);
//        robot.clickOn(MANAGE_LEAGUES);
//        sleep(500);
//        robot.clickOn("Create Season");
//        sleep(500);
//        robot.clickOn("#season");
//        write("2020");
//        clickOn("#leagueNames");
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn("#gamePolicies");
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn("#scorePolicies");
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn("#startDate");
//        write("20/05/2020");
//        clickOn("Okay");
//        alert_dialog_has_header_and_content(
//                "Error", "LeagueSeason already exists. Please try with different year or name.");
//        robot.clickOn("OK");
//        robot.closeCurrentWindow();
//    }
//
//    @Test
//    @Order(8)
//    public void createNewLeagueSeasonServerErrorTest() throws Exception {
//        ClientSystem.communication = new CommunicationNullStub(associationUser);
//        ApplicationTest.launch(App.class);
//        robot.clickOn("#txt_username");
//        write("association");
//        robot.clickOn("#txt_password");
//        write("1234");
//        robot.clickOn("LOG IN");
//        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                sleep(500);
//                return robot.lookup("#hamburger").tryQuery().isPresent();
//            }
//        });
//        robot.clickOn(MENU);
//        sleep(500);
//        robot.clickOn(MANAGE_LEAGUES);
//        sleep(500);
//        robot.clickOn("Create Season");
//        alert_dialog_has_header_and_content(
//                "Error", "There was a problem with the server. please try again");
//        robot.clickOn("OK");
//        robot.closeCurrentWindow();
//    }
//
//    @Test
//    @Order(9)
//    public void createNewLeagueSeasonYearWithLettersErrorTest() throws Exception {
//        //init data
//        League l = new League("check");
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeague(l);
//        GamePolicy gp = new GamePolicy(3, 2);
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addGamePolicy(gp);
//        ScorePolicy sp = new ScorePolicy(3, 1, 0);
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addScorePolicy(sp);
//        LeagueSeason ls = new LeagueSeason(l, new Season(2020), gp, sp, new Date());
//        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addLeagueSeason(ls);
//        ApplicationTest.launch(App.class);
//        robot.clickOn("#txt_username");
//        write("association");
//        robot.clickOn("#txt_password");
//        write("1234");
//        robot.clickOn("LOG IN");
//        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                sleep(1000);
//                return robot.lookup("#hamburger").tryQuery().isPresent();
//            }
//        });
//        robot.clickOn(MENU);
//        sleep(500);
//        robot.clickOn(MANAGE_LEAGUES);
//        sleep(500);
//        robot.clickOn("Create Season");
//        sleep(500);
//        robot.clickOn("#season");
//        write("abcd");
//        clickOn("#leagueNames");
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn("#gamePolicies");
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn("#scorePolicies");
//        type(KeyCode.DOWN);
//        type(KeyCode.ENTER);
//        clickOn("#startDate");
//        write("20/05/2020");
//        clickOn("Okay");
//        alert_dialog_has_header_and_content(
//                "Error", "Please insert only numbers.");
//        robot.clickOn("OK");
//        robot.closeCurrentWindow();
//    }
//
//    //schedule matches tests
//
//    //setRefereeInLeagueSeason tests
//
//
//}
