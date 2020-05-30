package PL.AssociationUITest;

import BL.Client.ClientSystem;
import BL.Communication.CommunicationLeagueSeasonAndPoliciesStub;
import BL.Communication.CommunicationNullStub;
import DL.Administration.AssociationMember;
import DL.Game.Policy.GamePolicy;
import DL.Users.User;
import PL.TestFXBase;
import PL.main.App;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.TextInputControl;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.*;
import org.loadui.testfx.controls.TextInputControls;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.testfx.api.FxAssert.verifyThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GamePolicyControllerTest extends TestFXBase {

    private List<User> associationUser;


    @BeforeEach
    public void init() throws Exception {
        associationUser = new ArrayList<>();
        AssociationMember u = new AssociationMember("association", "a@gmail.com", DigestUtils.sha1Hex("1234"));
        associationUser.add(u);
        ClientSystem.communication = new CommunicationLeagueSeasonAndPoliciesStub(associationUser);
    }

    @Test
    @Order(1)
    public void createNewGamePolicySuccessTest() throws Exception {
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("association");
        robot.clickOn("#txt_password");
        write("1234");
        robot.clickOn("LOG IN");
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(500);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        //robot.clickOn(CONNECT_AS_GUEST);
        //sleep(500);
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Create Game Policy");
        sleep(500);
        robot.clickOn("#numberOfRounds");
        write("3");
        robot.clickOn("#gamesPerDay");
        write("3");
        robot.clickOn("Okay");
        sleep(500);
        alert_dialog_has_header_and_content(
                "Success", "Game Policy added successfully!");
        verifyThat("#numberOfRounds", (JFXTextField textField) -> {
            String text = textField.getText();
            return text.contains("3");
        });
        verifyThat("#gamesPerDay", (JFXTextField textField) -> {
            String text = textField.getText();
            return text.contains("3");
        });
        robot.clickOn("OK");
        robot.closeCurrentWindow();
    }

    @Test
    @Order(2)
    public void createNewGamePolicyServerErrorTest() throws Exception {
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
                sleep(500);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        //robot.clickOn(CONNECT_AS_GUEST);
        //sleep(500);
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Create Game Policy");
        sleep(500);
        robot.clickOn("#numberOfRounds");
        write("3");
        robot.clickOn("#gamesPerDay");
        write("3");
        robot.clickOn("Okay");
        sleep(500);
        alert_dialog_has_header_and_content(
                "Error", "There was a problem with the connection to the server. Please try again later");
        robot.clickOn("OK");
        robot.closeCurrentWindow();
    }

    @Test
    @Order(3)
    public void createNewGamePolicyErrorsTest() throws Exception {
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
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Create Game Policy");
        sleep(500);

        //empty values
        robot.clickOn("Okay");
        sleep(500);
        alert_dialog_has_header_and_content(
                "Error", "Please fill the required (*) fields.");
        robot.clickOn("OK");
        sleep(500);

        //negative values
        robot.clickOn("#numberOfRounds");
        write("-2");
        robot.clickOn("#gamesPerDay");
        write("3");
        robot.clickOn("Okay");
        sleep(500);
        alert_dialog_has_header_and_content(
                "Error", "Parameters must be greater than 0.");
        robot.clickOn("OK");
        sleep(500);

        //letters
        robot.clickOn("#numberOfRounds");
        write("a");
        robot.clickOn("#gamesPerDay");
        write("3");
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
        GamePolicy gp = new GamePolicy(3, 2);
        ((CommunicationLeagueSeasonAndPoliciesStub) ClientSystem.communication).addGamePolicy(gp);
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("association");
        robot.clickOn("#txt_password");
        write("1234");
        robot.clickOn("LOG IN");
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(500);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Create Game Policy");
        sleep(500);

        robot.clickOn("#numberOfRounds");
        write("3");
        robot.clickOn("#gamesPerDay");
        write("2");
        sleep(500);
        robot.clickOn("Okay");
        alert_dialog_has_header_and_content(
                "Error", "Game policy already exists.");
        robot.clickOn("OK");
        robot.closeCurrentWindow();
    }

    @Test
    @Order(5)
    public void createNewGamePolicyMoreThan7GamesPerDayErrorTest() throws Exception {
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
        robot.clickOn("#hamburger");
        sleep(500);
        robot.clickOn("Manage Policies");
        sleep(500);
        robot.clickOn("Create Game Policy");
        sleep(500);

        //negative values
        robot.clickOn("#numberOfRounds");
        write("3");
        robot.clickOn("#gamesPerDay");
        write("8");
        robot.clickOn("Okay");
        sleep(500);
        alert_dialog_has_header_and_content(
                "Error", "The maximum games per day is 7.");
        robot.clickOn("OK");
        robot.closeCurrentWindow();
    }


}
