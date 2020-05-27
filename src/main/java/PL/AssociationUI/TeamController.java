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
        if (!initSeasonChoices(seasons) || !initTeamChoices(teams)){
            closeWindow();
            return;
        }

        seasons.setOnAction((e) -> {
            if (seasons.getValue() != null) {
                leagueSeasons.setItems(null);
                if(!initLeagueSeasonsChoices(leagueSeasons, seasons.getValue()))
                    closeWindow();
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

            if (App.clientSystem.leagueSeasonUnit.addTeamToLeagueSeason(leagueSeason,team)){
                showSimpleAlert("Success", "Team added to the league successfully!");
            } else {
                showSimpleAlert("Error", "There was a problem with the server. Please try again later");
            }
            closeWindow();

        } catch (Exception e) {
            showSimpleAlert("Error", e.getMessage());
        }

    }

    public void closeWindow() {
        AssociationController.loadScreen("AssociationManageTeamsFXML");
    }
}
