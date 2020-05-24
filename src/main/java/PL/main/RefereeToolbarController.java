package PL.main;

import BL.Client.ClientSystem;
import PL.RefereeUI.RefereeController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static PL.AlertUtil.showSimpleAlert;

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

    private void closeStage() {
        ((Stage) vbox.getScene().getWindow()).close();
    }

    private Object loadStage(String fxmlFileName) {
        try {
            App.scenes.push(App.mainStage.getScene());
            FXMLLoader fxmlLoader = new FXMLLoader();
            StackPane stackPane = fxmlLoader.load(getClass().getResource(String.format("/Window/%s.fxml", fxmlFileName)));
            Scene scene = new Scene(stackPane);
            Object controller = fxmlLoader.getController();
            App.mainStage.setScene(scene);
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            showSimpleAlert("Error", "Can't load screen. Please try again");
            return null;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        b_refInfo.setText(ClientSystem.getLoggedUser().getUsername());
    }
}
