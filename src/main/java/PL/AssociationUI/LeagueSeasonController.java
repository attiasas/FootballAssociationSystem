package PL.AssociationUI;

import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import PL.main.App;
import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static PL.AlertUtil.showSimpleAlert;

public class LeagueSeasonController extends AInitComboBoxObjects {

    //LeagueSeason fxml
    @FXML
    private JFXComboBox<League> leagueNames;
    @FXML
    private JFXTextField season;
    @FXML
    private JFXComboBox<GamePolicy> gamePolicies;
    @FXML
    private JFXComboBox<ScorePolicy> scorePolicies;
    @FXML
    private JFXDatePicker startDate;

    //ScheduleMatches fxml
    @FXML
    private JFXComboBox<Season> seasons;
    @FXML
    private JFXComboBox<LeagueSeason> leagueSeasons;

    //LeagueFXML
    @FXML
    private JFXTextField leagueName;


    public void initCreateLeagueComboBoxOptions() {
        if (!initLeagueChoices(leagueNames) || !initGamePolicyChoices(gamePolicies) || !initScorePolicyChoices(scorePolicies)) {
            closeWindow();
        }
    }

    public void initScheduleComboBoxOptions() {
        //inits seasons combo box
        if (!initSeasonChoices(seasons)) {
            closeWindow();
            return;
        }

        //inits leagueSeason combo box after choosing season
        seasons.setOnAction((e) -> {
            if (seasons.getValue() != null) {
                leagueSeasons.setItems(null);
                if (!initLeagueSeasonsChoices(leagueSeasons, seasons.getValue()))
                    closeWindow();
            }
        });
    }

    public void createNewLeague() {
        try {
            if (leagueName.getText() != null && !leagueName.getText().equals("")) {

                if (App.clientSystem.leagueSeasonUnit.addNewLeague(leagueName.getText())) {
                    showSimpleAlert("Succeeded", "New league added successfully");

                } else {
                    showSimpleAlert("Error", "There was a problem with the server. Please try again later");
                }

            } else {
                showSimpleAlert("Error", "League name can not be empty.");
            }
            closeWindow();

        } catch (Exception e) {
            showSimpleAlert("Error", e.getMessage());
        }
    }

    public void createLeagueSeason() {
        League league;
        Season newSeason;
        GamePolicy gamePolicy;
        ScorePolicy scorePolicy;
        Date start;
        int seasonYear;

        try {

            if (leagueNames.getValue() == null || gamePolicies.getValue() == null || scorePolicies.getValue() == null
                    || startDate.getValue() == null || season.getText().equals("")) {
                throw new Exception("Please fill all the required fields.");
            }

            //get fields values
            league = leagueNames.getValue();
            gamePolicy = gamePolicies.getValue();
            scorePolicy = scorePolicies.getValue();

            //prepare and set date
            LocalDate localDate = startDate.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            start = Date.from(instant);
            seasonYear = Integer.parseInt(season.getText());

            //add new season (if does'nt exist) and league season
            if (App.clientSystem.leagueSeasonUnit.addNewSeason(seasonYear)) {

                newSeason = App.clientSystem.leagueSeasonUnit.getSeason(seasonYear);

                if (App.clientSystem.leagueSeasonUnit.addLeagueSeason(league, newSeason, gamePolicy, scorePolicy, start)) {
                    showSimpleAlert("Success", "League Season added successfully!");

                } else {
                    showSimpleAlert("Error", "There was a problem with the server. Please try again later");
                }

            } else {
                showSimpleAlert("Error", "There was a problem with the server. Please try again later");
            }
            closeWindow();

        } catch (NumberFormatException e) {
            showSimpleAlert("Error", "Please insert only numbers.");

        } catch (Exception e) {
            showSimpleAlert("Error", e.getMessage());
        }


    }

    public void scheduleMatches() {
        try {
            LeagueSeason leagueSeason = leagueSeasons.getValue();
            if (leagueSeason == null)
                throw new Exception("Please select valid league season.");

            else {
                showWaitStage();

                if (App.clientSystem.policiesUnit.scheduleMatches(leagueSeason)) {
                    closeWaitStage();
                    showSimpleAlert("Successful!", "The league matches scheduled successfully.");

                } else {
                    closeWaitStage();
                    showSimpleAlert("Error", "There was a problem with the server. Please try again later");
                }

            }
            closeWindow();
        } catch (Exception e) {
            showSimpleAlert("Error", e.getMessage());
        }
    }

    public void setRefereeInMatches(){
        try {
            LeagueSeason leagueSeason = leagueSeasons.getValue();
            if (leagueSeason == null)
                throw new Exception("Please select valid league season.");

            else {
                showWaitStage();

                if (App.clientSystem.policiesUnit.setRefereeInMatches(leagueSeason)) {
                    closeWaitStage();
                    showSimpleAlert("Successful!", "Referees set in league matches successfully.");

                } else {
                    closeWaitStage();
                    showSimpleAlert("Error", "There was a problem with the server. Please try again later");
                }

            }
            closeWindow();
        } catch (Exception e) {
            showSimpleAlert("Error", e.getMessage());
        }
    }

    public void showLeagueTable() {

    }

    public void showLeagueScheduler() {

    }

    private void showWaitStage() {

    }

    private void closeWaitStage() {

    }

    public void closeWindow() {
        AssociationController.loadScreen("AssociationManageLeaguesFXML");
    }
}
