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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginControllerTest extends TestFXBase {

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
    public void loginSucceedTest() throws Exception {
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("association");
        sleep(1000);
        robot.clickOn("#txt_password");
        sleep(1000);
        write("1234");
        robot.clickOn("LOG IN");
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
    @Order(2)
    public void loginFailedTest() throws Exception {
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("failed");
        sleep(1000);
        robot.clickOn("#txt_password");
        sleep(1000);
        write("failed");
        robot.clickOn("LOG IN");
        assertFalse(robot.lookup("#hamburger").tryQuery().isPresent());
    }

    @Test
    @Order(3)
    public void loginAsGuestTest() throws Exception {
        ApplicationTest.launch(App.class);

        robot.clickOn(CONNECT_AS_GUEST);
        sleep(500);

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
    @Order(4)
    public void moveToSignupTest() throws Exception {
        ApplicationTest.launch(App.class);

        robot.clickOn("#signButton");
        sleep(500);

        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                sleep(500);
                return robot.lookup("#txt_fullName").tryQuery().isPresent();
            }
        });
        assertTrue(robot.lookup("#txt_fullName").tryQuery().isPresent());
    }

    @Test
    @Order(4)
    public void loginServerFailedTest() throws Exception {
        ApplicationTest.launch(App.class);
        robot.clickOn("#txt_username");
        write("failed");
        sleep(1000);
        robot.clickOn("#txt_password");
        sleep(1000);
        write("failed");
        robot.clickOn("LOG IN");
        assertFalse(robot.lookup("#hamburger").tryQuery().isPresent());
    }
}
