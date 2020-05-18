package PL.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import static PL.AlertUtil.showSimpleAlert;


public class AssociationToolbarController {

    @FXML
    public VBox vbox;

    @FXML
    private void loadManageTeams(ActionEvent actionEvent){
        loadStage("ManageTeams");
    }

    @FXML
    public void loadManageReferees(ActionEvent actionEvent)
    {
        loadStage("ManageReferees");
    }

    @FXML
    private void loadManageLeagues(ActionEvent event) {
        loadStage("AssociationManageLeaguesFXML");
    }

    @FXML
    private void loadManagePolicies(){
        loadStage("AssociationManagePoliciesFXML");
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
}
