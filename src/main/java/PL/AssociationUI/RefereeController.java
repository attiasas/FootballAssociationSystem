package PL.AssociationUI;

import DL.Game.LeagueSeason.LeagueSeason;
import DL.Game.LeagueSeason.Season;
import DL.Game.Referee;
import PL.main.App;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;

import static PL.AlertUtil.showSimpleAlert;

public class RefereeController extends AInitComboBoxObjects {

    @FXML
    private JFXComboBox<Season> seasons;

    @FXML
    private JFXComboBox<LeagueSeason> leagueSeasons;

    @FXML
    private JFXComboBox<Referee> referees;

    public void initRefereesInLeagueComboBoxOptions() {
        if (!initSeasonChoices(seasons) || !initRefereesChoices(referees)) {
            closeWindow();
            return;
        }

        seasons.setOnAction((e) -> {
            if (seasons.getValue() != null) {
                leagueSeasons.setItems(null);
                if (!initLeagueSeasonsChoices(leagueSeasons, seasons.getValue()))
                    closeWindow();
            }
        });

    }

    public void setRefereesInLeagueSeason() {
        LeagueSeason leagueSeason;
        Referee referee;

        try {
            if (seasons.getValue() == null || leagueSeasons.getValue() == null || referees.getValue() == null) {
                throw new Exception("Please fill all the required fields.");
            }

            //get fields values
            leagueSeason = leagueSeasons.getValue();
            referee = referees.getValue();

            if (App.clientSystem.leagueSeasonUnit.setRefereeInLeagueSeason(leagueSeason, referee)) {
                showSimpleAlert("Success", "Referee added successfully to the required LeagueSeason!");
            } else {
                showSimpleAlert("Error", "There was a problem with the server. Please try again later");
            }
            closeWindow();
        } catch (Exception e) {
            showSimpleAlert("Error", e.getMessage());
        }
    }

    public void addNewReferee() {

    }

    public void removeReferee() {

    }

    public void closeWindow() {
        AssociationController.loadScreen("AssociationManageRefereesFXML");
    }
}
