package PL.main;

import BL.Client.ClientSystem;
import PL.RefereeUI.RefereeController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static PL.AlertUtil.showSimpleAlert;

/**
 * Created By: Assaf, On 26/05/2020
 * Description:
 */
public class FanToolbarController implements Initializable {
    @FXML
    public Button b_fanInfo;

    public void loadNotifyScreen()
    {
        App.loadScreen("NotificationsFXML");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        b_fanInfo.setText("Hello, " + ClientSystem.getLoggedUser().getUsername());
    }
}
