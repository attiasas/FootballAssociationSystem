package PL.AssociationUI;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Referee;
import PL.main.App;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;

import static PL.AlertUtil.showSimpleAlert;

public class RefereeController extends AInitComboBoxObjects{

    @FXML
    private JFXComboBox<Season> seasons;

    @FXML
    private JFXComboBox<LeagueSeason> leagueSeasons;

    @FXML
    private JFXComboBox<Referee> referees;

    public void initRefereesInLeagueComboBoxOptions(){
        initSeasonChoices(seasons);
        initRefereesChoices(referees);
        seasons.setOnAction((e) -> {
            if (seasons.getValue() != null) {
                leagueSeasons.setItems(null);
                initLeagueSeasonsChoices(leagueSeasons, seasons.getValue());
            }
        });

    }

    public void setRefereesInLeagueSeason(){
        LeagueSeason leagueSeason;
        Referee referee;

        try {
            if (seasons.getValue() == null || leagueSeasons.getValue() == null || referees.getValue() == null){
                throw new Exception("Please fill all the required fields.");
            }

            //get fields values
            leagueSeason = leagueSeasons.getValue();
            referee = referees.getValue();

            App.clientSystem.leagueSeasonUnit.setRefereeInLeagueSeason(leagueSeason,referee);
            showSimpleAlert("Success", "Referee added successfully to the required LeagueSeason!");

        } catch (Exception e) {
            showSimpleAlert("Error", e.getMessage());
        }
    }

    public void addNewReferee(){

    }

    public void removeReferee(){

    }
}
