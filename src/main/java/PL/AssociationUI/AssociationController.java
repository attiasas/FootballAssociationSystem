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

    public void createNewLeague() {
        loadScreen("LeagueFXML");
    }

    public void createNewGamePolicy() {
        loadScreen("GamePolicyFXML");
    }

    public void createNewScorePolicy() {
        loadScreen("ScorePolicyFXML");
    }

    public void addNewLeagueSeason() {
        LeagueSeasonController lsController = (LeagueSeasonController) loadScreen("LeagueSeasonFXML");
        if (lsController != null) {
            lsController.initCreateLeagueComboBoxOptions();
        }
    }

    public void changeScorePolicy() {
        ScorePolicyController lsController = (ScorePolicyController) loadScreen("ChangeScorePolicyFXML");
        if (lsController != null) {
            lsController.initComboBoxOptions();
        }
    }

    public void setRefereesInLeagueSeason(){

    }

    public void scheduleMatches(){
        LeagueSeasonController lsController = (LeagueSeasonController) loadScreen("ScheduleMatchesFXML");
        if (lsController != null) {
            lsController.initScheduleComboBoxOptions();
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
