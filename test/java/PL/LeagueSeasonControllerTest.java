package java.PL;

import com.jfoenix.controls.JFXTextField;
import org.junit.jupiter.api.Test;

import static org.testfx.api.FxAssert.verifyThat;

public class LeagueSeasonControllerTest extends TestFXBase{

    final String CONNECT_AS_GUEST = "#guestButton";
    final String MENU = "#hamburger";
    final String MANAGE_LEAGUES = "#manageLeaguesButton";
    final String CREATE_LEAGUE = "#createLeagueButton";
    final String LEAGUE_NAME_FIELD = "#leagueName";
    final String OKAY_BUTTON = "#okayButton";

    @Test
    public void testScreen() throws Exception {
        robot.clickOn(CONNECT_AS_GUEST);
        sleep(500);
        robot.clickOn(MENU);
        sleep(500);
        robot.clickOn(MANAGE_LEAGUES);
        sleep(500);
        robot.clickOn(CREATE_LEAGUE);
        sleep(500);
        robot.clickOn(LEAGUE_NAME_FIELD);
        write("check League");
        robot.clickOn(OKAY_BUTTON);
        sleep(2000);
        alert_dialog_has_header_and_content(
                "Succeeded", "New league added successfully");
        verifyThat(LEAGUE_NAME_FIELD,(JFXTextField textField)-> {
            String text = textField.getText();
            return text.contains("check League");
        });
        robot.clickOn("OK");
    }


}
