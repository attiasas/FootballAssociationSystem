package PL.AssociationUI;

import PL.main.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.io.IOException;

import static PL.AlertUtil.showSimpleAlert;


public class AssociationController {

    //LeagueSeason controller functions
    public void createNewLeague() {
        loadScreen("LeagueFXML");
    }

    public void addNewLeagueSeason() {
        LeagueSeasonController lsController = (LeagueSeasonController) loadScreen("LeagueSeasonFXML");
        if (lsController != null) {
            lsController.initCreateLeagueComboBoxOptions();
        }
    }

    public void scheduleMatches(){
        LeagueSeasonController lsController = (LeagueSeasonController) loadScreen("ScheduleMatchesFXML");
        if (lsController != null) {
            lsController.initScheduleComboBoxOptions();
        }
    }

    public void setRefereesInMatches(){
        LeagueSeasonController lsController = (LeagueSeasonController) loadScreen("SetRefereesInMatchesFXML");
        if (lsController != null) {
            lsController.initScheduleComboBoxOptions();
        }
    }

    //Policies Functions
    public void createNewGamePolicy() {
        loadScreen("GamePolicyFXML");
    }

    public void createNewScorePolicy() {
        loadScreen("ScorePolicyFXML");
    }

    public void changeScorePolicy() {
        ScorePolicyController lsController = (ScorePolicyController) loadScreen("ChangeScorePolicyFXML");
        if (lsController != null) {
            lsController.initComboBoxOptions();
        }
    }

    //Referees functions
    public void setRefereesInLeagueSeason(){
        RefereeController lsController = (RefereeController) loadScreen("RefereeInLeagueSeasonFXML");
        if (lsController != null) {
            lsController.initRefereesInLeagueComboBoxOptions();
        }
    }

    public void addNewReferee(){

    }

    public void removeReferee(){

    }

    //Teams functions
    public void addNewTeam(){

    }

    public void addTeamToLeagueSeason(){
        TeamController lsController = (TeamController) loadScreen("SetTeamInLeagueSeasonFXML");
        if (lsController != null) {
            lsController.initTeamInLeagueSeasonsComboBoxOptions();
    public void createTeam() { loadScreen("CreateTeam"); }

    public void addOrRemoveReferee() {
        RefereeController rController = (RefereeController) loadScreen("AddOrRemoveReferee");
        if (rController != null) {
            rController.initCreateRefereeComboBoxOptions();

        }
    }

    //Financial functions
    public void setFinancialRules(){

    }

    public void manageAssociationFinancial(){

    }

    public static Object loadScreen(String fxmlFileName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AssociationController.class.getResource(String.format("/Window/%s.fxml", fxmlFileName)));
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
        loadScreen("MainApp");
    }

}
