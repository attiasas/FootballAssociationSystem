package PL.AssociationUI;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Team.Team;
import PL.main.App;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;

import static PL.AlertUtil.showSimpleAlert;

public class TeamController extends AInitComboBoxObjects{

    @FXML
    JFXComboBox<Season> seasons;

    @FXML
    JFXComboBox<LeagueSeason> leagueSeasons;

    @FXML
    JFXComboBox<Team> teams;

    public void initTeamInLeagueSeasonsComboBoxOptions(){
        initSeasonChoices(seasons);
        initTeamChoices(teams);
        seasons.setOnAction((e) -> {
            if (seasons.getValue() != null) {
                leagueSeasons.setItems(null);
                initLeagueSeasonsChoices(leagueSeasons, seasons.getValue());
            }
        });
    }

    private void initTeamChoices(JFXComboBox<Team> teams) {
    }

    public void setTeamInLeagueSeason(){

        LeagueSeason leagueSeason;
        Team team;

        try {

            if (seasons.getValue() == null || leagueSeasons.getValue() == null || teams.getValue() == null){
                throw new Exception("Please fill all the required fields.");
            }

            //get fields values
            leagueSeason = leagueSeasons.getValue();
            team = teams.getValue();

           // App.clientSystem.leagueSeasonUnit.addTeamToLeagueSeason(leagueSeason,team);
            showSimpleAlert("Success", "Team added successfully!");

        } catch (Exception e) {
            showSimpleAlert("Error", e.getMessage());
        }
    }
}
