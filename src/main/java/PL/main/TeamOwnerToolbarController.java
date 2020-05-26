package PL.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class TeamOwnerToolbarController implements Initializable {

    @FXML
    public VBox vbox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void loadAddTeam(ActionEvent event) {
    }

    @FXML
    private void loadAddPlayer(ActionEvent event) {
    }

    @FXML
    private void loadAddAsset(ActionEvent event) {
    }

    @FXML
    private void loadDatabase(ActionEvent event) {
    }

    @FXML
    private void loadManageSettings(ActionEvent event) {
        loadStage("Settings");
    }

    @FXML
    private void loadNotification(ActionEvent event)
    {
        loadStage("NotificationsFXML");
    }

    private void closeStage() {
        ((Stage) vbox.getScene().getWindow()).close();
    }

    public void loadStage(String fxmlFileName) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(String.format("/Window/%s.fxml", fxmlFileName)));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setResizable(false);
            stage.setTitle("Sportify");

            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ignored) {
        }
    }

}
