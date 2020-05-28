package java.PL.AssociationUITest;

import com.jfoenix.controls.JFXTextField;
import org.junit.jupiter.api.Test;

import java.PL.TestFXBase;

import static org.testfx.api.FxAssert.verifyThat;

public class ManageTeamsControllerTest extends TestFXBase {

    final String CONNECT_AS_GUEST = "#guestButton";
    final String MENU = "#hamburger";
    final String MANAGE_TEAMS_BTN = "#ManageTeamsBtn";
    final String CREATE_TEAM_BTN = "#createTeamBtn";
    final String ADD_TEAM_BTN = "#addTeamBtn";
    final String TEAM_NAME_FLD = "#teamName";
    final String TEAMOWNER_USERNAME_FLD = "#teamOwnerUsername";
    final String OKAY_BTN = "#okayButton";
    final String BACK_BTN = "#backToMenuBtn";
    final String CLEAR_FIELDS_BTN = "#clearFieldsBtn";

    @Test
    public void testScreen() throws Exception {
        robot.clickOn(CONNECT_AS_GUEST);
        sleep(500);
        robot.clickOn(MENU);
        sleep(500);
        robot.clickOn(MANAGE_TEAMS_BTN);
        sleep(500);
        robot.clickOn(CREATE_TEAM_BTN);
        sleep(500);
        robot.clickOn(ADD_TEAM_BTN);
        sleep(500);
        robot.clickOn(TEAM_NAME_FLD);
        write("Hapoel Be'er Sheva");
        sleep(500);
        robot.clickOn(TEAMOWNER_USERNAME_FLD);
        write("Dvir");
        sleep(500);
        robot.clickOn(OKAY_BTN);
        sleep(2000);
        alert_dialog_has_header_and_content(
                "Succeeded", "New team was added successfully");
        verifyThat(TEAM_NAME_FLD,(JFXTextField textField)-> {
            String text = textField.getText();
            return text.contains("Hapoel Be'er Sheva");
        });
        robot.clickOn(BACK_BTN);
    }
}
