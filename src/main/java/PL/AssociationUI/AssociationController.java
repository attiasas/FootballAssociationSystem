package PL.AssociationUI;

import BL.Client.Handlers.LeagueSeasonUnit;
import BL.Client.Handlers.PoliciesUnit;
import BL.Communication.ClientServerCommunication;
import PL.main.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.io.IOException;

import static PL.AlertUtil.showSimpleAlert;


public class AssociationController {

    public static LeagueSeasonUnit leagueSeasonUnit;
    public static PoliciesUnit policiesUnit;

    public AssociationController() {
        leagueSeasonUnit = new LeagueSeasonUnit(new ClientServerCommunication());
        policiesUnit = new PoliciesUnit(new ClientServerCommunication());
    }

    public void createTeam() { loadScreen("CreateTeam"); }

    public void addOrRemoveReferee() {
        RefereeController rController = (RefereeController) loadScreen("AddOrRemoveReferee");
        if (rController != null) {
            rController.initCreateRefereeComboBoxOptions();

        }
    }

    private Object loadScreen(String fxmlFileName) {
        try {
            App.scenes.push(App.mainStage.getScene());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(String.format("/Window/%s.fxml", fxmlFileName)));
            StackPane stackPane = fxmlLoader.load();
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

    public void closeWindow() {
        App.mainStage.setScene(App.scenes.pop());
    }

}
