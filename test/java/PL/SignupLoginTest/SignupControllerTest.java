package PL.SignupLoginTest;

import BL.Client.ClientSystem;
import BL.Communication.CommunicationLeagueSeasonAndPoliciesStub;
import DL.Administration.AssociationMember;
import DL.Users.User;
import PL.TestFXBase;
import PL.main.App;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignupControllerTest extends TestFXBase {

    final String SIGN_UP = "#signButton";
    final String CONNECT_AS_GUEST = "#guestButton";
    final String MENU = "#hamburger";
    final String MANAGE_LEAGUES = "#manageLeaguesButton";
    final String CREATE_LEAGUE = "#createLeagueButton";
    final String LEAGUE_NAME_FIELD = "#leagueName";
    final String OKAY_BUTTON = "#okayButton";
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
    public void signupSucceedTest() throws Exception {
        ApplicationTest.launch(App.class);
        robot.clickOn(SIGN_UP);
        sleep(500);
        robot.clickOn("#txt_fullName");
        write("Steve Jobs");
        sleep(500);
        robot.clickOn("#txt_email");
        write("steve@apple.com");
        sleep(500);
        robot.clickOn("#txt_confirmMail");
        write("steve@apple.com");
        sleep(500);
        robot.clickOn("#txt_username");
        write("apple");
        sleep(500);
        robot.clickOn("#txt_password");
        write("apple");
        sleep(500);
        robot.clickOn("#txt_dob");
        write("25/06/1970");
        sleep(500);
        robot.clickOn("#rb_male");
        sleep(500);
        robot.clickOn("#sign_btn");
        sleep(2000);

        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(500);
                return robot.lookup("#hamburger").tryQuery().isPresent();
            }
        });
        assertTrue(robot.lookup("#hamburger").tryQuery().isPresent());
    }

    @Test
    public void signupFailedTest() throws Exception {
        ApplicationTest.launch(App.class);
        robot.clickOn(SIGN_UP);
        sleep(500);
        robot.clickOn("#txt_fullName");
        write("Steve Jobs");
        sleep(500);
        robot.clickOn("#txt_email");
        write("steve@apple.com");
        sleep(500);
        robot.clickOn("#txt_confirmMail");
        write("steve@apple.com");
        sleep(500);
        robot.clickOn("#txt_username");
        write("admin");
        sleep(500);
        robot.clickOn("#txt_password");
        write("admin");
        sleep(500);
        robot.clickOn("#txt_dob");
        write("25/06/1970");
        sleep(500);
        robot.clickOn("#rb_male");
        sleep(500);
        robot.clickOn("#sign_btn");
        sleep(2000);
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(500);
                return robot.lookup("#rb_female").tryQuery().isPresent();
            }
        });
        assertTrue(robot.lookup("#rb_female").tryQuery().isPresent());
    }

    @Test
    public void backToLoginTest() throws Exception {
        ApplicationTest.launch(App.class);

        robot.clickOn(SIGN_UP);
        sleep(500);

        robot.clickOn("#back_login");
        sleep(500);

        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(500);
                return robot.lookup("#guestButton").tryQuery().isPresent();
            }
        });
        assertTrue(robot.lookup("#guestButton").tryQuery().isPresent());
    }
}
