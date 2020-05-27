package PL.main;

import BL.Client.ClientSystem;
import PL.RefereeUI.RefereeController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created By: Assaf, On 23/05/2020
 * Description:
 */
public class RefereeToolbarController implements Initializable {

    @FXML
    public VBox vbox;

    @FXML
    public Button b_refInfo;

    @FXML
    private void loadMyMatches() {
        RefereeController controller = (RefereeController)App.loadScreen("refereeScreen");
        controller.init(App.clientSystem.matchEventUnit);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        b_refInfo.setText(ClientSystem.getLoggedUser().getUsername());
    }
}
