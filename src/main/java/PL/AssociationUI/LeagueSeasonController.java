package PL.AssociationUI;

import DL.Game.LeagueSeason.League;
import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Policy.GamePolicy;
import DL.Game.Policy.ScorePolicy;
import PL.main.App;
import com.jfoenix.controls.*;
import javafx.fxml.FXML;
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
        initLeagueChoices(leagueNames);
        initGamePolicyChoices(gamePolicies);
        initScorePolicyChoices(scorePolicies);
    }

    public void initScheduleComboBoxOptions() {
        initSeasonChoices(seasons);
        seasons.setOnAction((e) -> {
            if (seasons.getValue() != null) {
                leagueSeasons.setItems(null);
                initLeagueSeasonsChoices(leagueSeasons, seasons.getValue());
            }
        });
    }

    public void createNewLeague(){
        try {
            if (leagueName.getText() != null && !leagueName.getText().equals("")) {
                App.clientSystem.leagueSeasonUnit.addNewLeague(leagueName.getText());
                showSimpleAlert("Succeeded", "New league added successfully");
            } else {
                showSimpleAlert("Error", "League name can not be empty.");
            }
        } catch (Exception e){
            showSimpleAlert("Error",e.getMessage());
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

            LocalDate localDate = startDate.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            start = Date.from(instant);

            seasonYear = Integer.parseInt(season.getText());
            App.clientSystem.leagueSeasonUnit.addNewSeason(seasonYear);
            newSeason = App.clientSystem.leagueSeasonUnit.getSeason(seasonYear);

            App.clientSystem.leagueSeasonUnit.addLeagueSeason(league, newSeason, gamePolicy, scorePolicy, start);
            showSimpleAlert("Success", "League Season added successfully!");

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
                App.clientSystem.policiesUnit.scheduleMatches(leagueSeason);
                closeWaitStage();
                showSimpleAlert("Successful!", "The league matches scheduled successfully.");
            }
        } catch (Exception e) {
            showSimpleAlert("Error", e.getMessage());
        }
    }

    public void showLeagueTable() {

    }

    public void showLeagueScheduler() {

    }

    private void showWaitStage(){

    }

    private void closeWaitStage(){

    }

}
