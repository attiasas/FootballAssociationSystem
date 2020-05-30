import PL.TestFXBase;
import com.jfoenix.controls.JFXTextField;
import org.junit.jupiter.api.Test;

import static org.testfx.api.FxAssert.verifyThat;

public class RefereeControllerTest extends TestFXBase {

    final String CONNECT_AS_GUEST = "#guestButton";
    final String MENU = "#hamburger";
    final String MANAGE_REFEREES_BTN = "#ManageRefereesBtn";
    final String ADD_OR_REFEREE_BTN = "#addOrRemoveRefereeBtn";
    final String REFEREE_USERNAME_FLD = "#refereeUsername";
    final String REFEREE_NAME_FLD = "#refereeName";
    final String REFEREE_QUALIFICATION_FLD = "#refereeQualification";
    final String OKAY_BTN = "#createButton";
    final String COMBOBOX_REFEREE = "#comboboxReferee";
    final String REMOVE_REFEREE_BTN = "#removeReferee";
    final String BACK_BTN = "#backToMenuBtn";

    @Test
    public void testScreen() throws Exception {
        robot.clickOn(CONNECT_AS_GUEST);
        sleep(500);
        robot.clickOn(MENU);
        sleep(500);
        robot.clickOn(MANAGE_REFEREES_BTN);
        sleep(500);
        robot.clickOn(ADD_OR_REFEREE_BTN);
        sleep(500);
        robot.clickOn(REFEREE_USERNAME_FLD);
        write("dvir");
        sleep(500);
        robot.clickOn(REFEREE_NAME_FLD);
        write("dvirsim");
        sleep(500);
        robot.clickOn(REFEREE_QUALIFICATION_FLD);
        write("Pro");
        sleep(500);
        verifyThat(REFEREE_NAME_FLD,(JFXTextField textField)-> {
            String text = textField.getText();
            return text.contains("dvir");
        });
        robot.clickOn(OKAY_BTN);
        sleep(500);
        robot.clickOn(COMBOBOX_REFEREE);
        sleep(500);
        robot.clickOn(REMOVE_REFEREE_BTN);
        sleep(500);
        sleep(2000);
        alert_dialog_has_header_and_content(
                "Succeeded", "New referee was added and removed successfully");
        robot.clickOn(BACK_BTN);
    }

}
